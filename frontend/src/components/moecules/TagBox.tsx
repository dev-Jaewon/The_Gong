import styled from "styled-components";
import TagButton from "../atoms/Tag/TagButton";
import XMark from "../atoms/Tag/XMark";

interface TagData {
  content: string;
  color: string;
}

interface TagBoxProps {
  tagData: TagData[];
  fontSize: number;
  func: (el:TagData) => void;
  isDeleteTag: boolean;
}

const TagBoxContainer = styled.div<{ fontSize: number }>`
  display: flex;
  flex-wrap: wrap;
  gap: ${({ fontSize }) => fontSize / 2}rem;
`;

function TagBox({
  tagData,
  fontSize,
  func,
  isDeleteTag,
}: TagBoxProps): JSX.Element {
  // 리스트에서의 태그 역할을 하는 변수
  const basicsTagBox = tagData.map((el, idx) => (
    <TagButton
      key={idx}
      fontSize={fontSize}
      bg={el.color}
      content={el.content}
      func={() => { func(el)}}
      />
  ));

  // 모달에서의 태그 수정 역할을 하는 변수
  const deleteTagBox = tagData.map((el, idx) => (
    <TagButton key={idx} fontSize={fontSize} bg={el.color} content={el.content}>
      <XMark func={() => { func(el)}} />
    </TagButton>
  ));

  return (
    <TagBoxContainer fontSize={fontSize}>
      {isDeleteTag ? deleteTagBox : basicsTagBox}
    </TagBoxContainer>
  );
}

export default TagBox;
