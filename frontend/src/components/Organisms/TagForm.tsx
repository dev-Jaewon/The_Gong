import styled from "styled-components";
import TagBox from "../Molecules/TagBox";
import BorderBox from "../atoms/Tag/BorderBox";
import XMark from "../atoms/Tag/XMark";

const TagFormContainer = styled.div`
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  width: 600px;
  box-shadow: rgba(0, 0, 0, 0.16) 0px 1px 4px;
  padding: 1rem 2rem;

 h2{
  color: #4A5056;
  font-size: 1.2rem;
  font-weight: bolder;
  margin: 1rem 0;
  margin-top: 3rem;
 }

 .closeButtonContainer{
  display: flex;
  justify-content: end;
 }
`;


function TagForm() {

  //임시 데이터
  const tagData = [
    {
      content:'급성장 중',
      color:'#F0FAF9'
    },
    {
      content:'50인 이하',
      color:'#F0FAF9'
    },
    {
      content:'50인 이상',
      color:'#F0FAF9'
    },
    {
      content:'자고싶다',
      color:'#F0FAF9'
    },
    {
      content:'병역특례',
      color:'#F0FAF9'
    },
  ]

  // 임시 기능
  const consolelog = () => {
    console.log('되냐?')
  }

  return(
    <TagFormContainer>

      <div className="closeButtonContainer">
        <XMark func={consolelog} />
      </div>

      <h2>전체 태그</h2>
      <BorderBox>
        <TagBox tagData={tagData} fontSize={1} func={consolelog} isDeleteTag={false}></TagBox>
      </BorderBox>

      <h2>관심 태그</h2>
      <BorderBox>
        <TagBox tagData={tagData} fontSize={1} func={consolelog} isDeleteTag={true}></TagBox>
      </BorderBox>
    </TagFormContainer>
  );
}

export default TagForm;
