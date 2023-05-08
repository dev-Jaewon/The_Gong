import styled from "styled-components"
import TagButton from "../Atoms/TagButton";
import XMark from "../Atoms/XMark";

const TagBoxContainer = styled.div`
 display: flex;
 flex-wrap: wrap;
 //fontSize에 따른 너비 조절
 gap: ${({fontSize}) => fontSize/2}rem;
`

function TagBox({tagData, fontSize, func, isDeleteTag}){

  // 리스트에서의 태그 역할을 하는 변수
  const basicsTagBox = tagData.map((el, idx) => <TagButton key={idx} fontSize={fontSize} bg={el.color} content={el.content} func={func} />);
  
  // 모달에서의 태그 수정 역할을 하는 변수
  const deleteTagBox = tagData.map((el, idx) => (
    <TagButton key={idx} fontSize={fontSize} bg={el.color} content={el.content} >
      <XMark func={func} />
    </TagButton>
  ));

  return (
    <TagBoxContainer fontSize={fontSize}>
      {isDeleteTag ? deleteTagBox : basicsTagBox}
    </TagBoxContainer>
  )
}

export default TagBox;