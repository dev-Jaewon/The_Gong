import styled from "styled-components";
import { Link } from 'react-router-dom';
import Header from "../Main/Header";

const CreateRoomPageContainer = styled.div`

`

const CreateRoomPage = () => {
  return (
    <CreateRoomPageContainer>
      <Header></Header>
      <h1>스터디 만들기</h1>
    </CreateRoomPageContainer>
  )
}

export default CreateRoomPage;