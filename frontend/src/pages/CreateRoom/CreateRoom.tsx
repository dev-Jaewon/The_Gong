import styled from 'styled-components';
import RoomForm from '../../components/atoms/Room/RoomForm';
import axios from 'axios';
// import Header from "../Main/Header";
import { Header } from '../../components/organisms/Header';

const CreateRoomPageContainer = styled.div`
  h1 {
    font-size: 25px;
    font-weight: 500;
    text-align: center;
    padding-bottom: 15px;
  }
`;

const Container = styled.div`
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
`;

const CreateRoomPage = () => {
  const sendFormData = async (data: any) => {
    try {
      const response = await axios.post('http://localhost:3001/', data);
      return response.data;
    } catch (error) {
      console.error('Error sending data to server:', error);
      throw error;
    }
  };

  const handleSubmit = (data: any) => {
    sendFormData(data);
  };
  const isLoading = false;

  return (
    <CreateRoomPageContainer>
      <Header></Header>
      <Container>
        <div>
          <h1>스터디 만들기</h1>
          <RoomForm onSubmit={handleSubmit} isLoading={isLoading}></RoomForm>
        </div>
      </Container>
    </CreateRoomPageContainer>
  );
};

export default CreateRoomPage;
