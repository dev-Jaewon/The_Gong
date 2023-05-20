import styled from "styled-components";
import TagBox from "../Molecules/TagBox";
import BorderBox from "../atoms/Tag/BorderBox";
import XMark from "../atoms/Tag/XMark";
import { useEffect, useState } from "react";
import { api } from "../../util/api";


// TagFormContainer 컴포넌트의 props 타입 정의
interface TagFormProps {
  isPopupOpen: boolean;
  ChangeisPopupOpen?: (event: any) => void; 
  ChangeTag?: (event: any) => void; 
}

interface TagData {
  content: string;
  color: string;
}

const TagFormContainer = styled.div<TagFormProps>`
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  width: 600px;
  box-shadow: rgba(0, 0, 0, 0.16) 0px 1px 4px;
  padding: 1rem 2rem;
  z-index: 10;
  background-color: white;
  display: ${({isPopupOpen}) => isPopupOpen ? 'block' : 'none'};

  .none{
    display: none;
  }

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


function TagForm({ isPopupOpen, ChangeisPopupOpen, ChangeTag }: TagFormProps) {


  const fetchData = async () => {
    try {
      const response = await api.get('https://4b38-211-193-143-25.ngrok-free.app/tags?page=1&size=10');
      const colorData = response.data.data.map((el:any) => {
        console.log(el)
        return {
          'content':el.name,
          'color': '#4FAFB1'
        }
      })
      setTagData(colorData)
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  //임시 데이터
  const [tagData, setTagData ] = useState<TagData[]>([]);;

  const [deleteTagData, setDeleteTagData] = useState<TagData[]>([]);

  // 태그 추가 기능
  const changeTagData = (tagContent: TagData) => {
    const updatedTagData = tagData.filter((el:any) => el.content !== tagContent.content);
    setTagData(updatedTagData);
    setDeleteTagData([...deleteTagData, tagContent]);
    ChangeTag(tagContent.content)
  }

  // 삭제 태그 기능
  const changeDeleteTagData= (tagContent: TagData) => {
    const updatedDeleteTagData = deleteTagData.filter((el:any) => el.content !== tagContent.content);
    setDeleteTagData(updatedDeleteTagData);
    setTagData([...tagData, tagContent]);
  }
  

  return(
    <TagFormContainer isPopupOpen={isPopupOpen}>

      <div >
        <div className="closeButtonContainer ">
          <XMark func={ChangeisPopupOpen} />
        </div>

        <h2>전체 태그</h2>
        <BorderBox>
          <TagBox tagData={tagData} fontSize={1} func={changeTagData} isDeleteTag={false}></TagBox>
        </BorderBox>

        <h2>스터디 태그</h2>
        <BorderBox>
          <TagBox tagData={deleteTagData} fontSize={1} func={changeDeleteTagData} isDeleteTag={true}></TagBox>
        </BorderBox>
      </div>

    </TagFormContainer>
  );
}

export default TagForm;
