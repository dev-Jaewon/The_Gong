import styled from 'styled-components';
import RoomForm from '../../components/atoms/Room/RoomForm';
import axios from 'axios';
// import Header from "../Main/Header";
import { Header } from '../../components/organisms/Header';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { api } from '../../util/api';



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

  const navigate = useNavigate();
  const [token, setToken] = useState('');
  const [memberId, setMemberId] = useState('');
  const [imgUrl, setImgUrl] = useState('')


  useEffect(() => {
    // 페이지 진입 시 로컬 스토리지 값 확인
  // 페이지 진입 시 로컬 스토리지 값 확인
  const userInfoString = localStorage.getItem('access_token');
  const usermemberId = localStorage.getItem('member_id');

  if (userInfoString && usermemberId) {
    setToken(JSON.parse(userInfoString));
    setMemberId(JSON.parse(usermemberId));
  } else {
    console.log('스토리지 값 없음');
  }
  }, []);

  const sendFormData = async (data: any) => {

    const requestData = {
      ...data,
      'img_url': imgUrl,
      'admin_member_id': memberId + ''
    };
    
    console.log('@@@이거 보냅니다@@@');
    console.log(requestData);

    api.post('https://9af7-211-193-143-25.ngrok-free.app/rooms/add', requestData,{
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
      .then(response => {
        // 요청 성공 시 처리
        console.log('@@@이거 보냈습니다@@@');
        console.log(response.data);
        navigate('/')
      })
      .catch(error => {
        // 요청 실패 시 처리
        console.error(error);
      });
  };

  const handleSubmit = (data: any) => {
    sendFormData(data);
  };
  const isLoading = false;

  const [selectedFile, setSelectedFile] = useState('');

  const handleFileUpload = () => {
    const formData = new FormData();
    formData.append('image', selectedFile);
    console.log(formData)
    console.log(memberId)


    api.post(`https://9af7-211-193-143-25.ngrok-free.app/thumbnail/${memberId}`, formData,{
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'multipart/form-data',
      }
    })
      .then(response => {
        // 요청 성공 시 처리
        console.log('@@@이거 받았습니다@@@');
        console.log(response.data);
        setImgUrl(response.data)
      })
      .catch(error => {
        // 요청 실패 시 처리
        console.error(error);
      });
  }


  return (
    <CreateRoomPageContainer>
      <Header></Header>
      <Container>
        <div>
          <h1>스터디 만들기</h1>
          <RoomForm onSubmit={handleSubmit} isLoading={isLoading} setSelectedFile={setSelectedFile} handleFileUpload={handleFileUpload}></RoomForm>
        </div>
      </Container>
    </CreateRoomPageContainer>
  );
};

export default CreateRoomPage;
