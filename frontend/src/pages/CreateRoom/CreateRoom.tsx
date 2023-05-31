import styled from 'styled-components';
import RoomForm from '../../components/atoms/Room/RoomForm';
import axios from 'axios';
import { Header } from '../../components/organisms/Header';
import { useEffect, useRef, useState } from 'react';
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
  const imgUrl = useRef('')

  useEffect(() => {
    // 페이지 진입 시 로컬 스토리지 값 확인
    // 페이지 진입 시 로컬 스토리지 값 확인
    const userInfoString = localStorage.getItem('access_token');
    const usermemberId = localStorage.getItem('member_id');

    if (userInfoString && usermemberId) {
      setToken(JSON.parse(userInfoString));
      setMemberId(JSON.parse(usermemberId));
      console.log(userInfoString);
      console.log(usermemberId);
    } else {
      console.log('스토리지 값 없음');
    }
  }, []);

  const sendFormData = async (data: any) => {
    const requestData = {
      ...data,
      image_url: imgUrl.current,
      admin_member_id: memberId + '',
    };

    console.log('@@@이거 보냅니다@@@');
    console.log(requestData);
    console.log(imgUrl);

    api
      .post(
        `${import.meta.env.VITE_BASE_URL}rooms/${memberId}/add`,
        requestData,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      )
      .then((response) => {
        // 요청 성공 시 처리
        console.log('@@@이거 받았습니다@@@');
        console.log(response.data);
        console.log(imgUrl);
        navigate('/');
        window.location.reload(); // 페이지 리로드
      })
      .catch((error) => {
        // 요청 실패 시 처리
        console.error(error);
      });
  };

  const handleSubmit = (data: any) => {
    sendFormData(data);
  };
  const isLoading = false;

  const [selectedFile, setSelectedFile] = useState('');
  const [formError, setError] = useState(false);

  const handleFileUpload = (e: any) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append('image', selectedFile);
    console.log(formData);
    console.log(memberId);

    api
      .post(`${import.meta.env.VITE_BASE_URL}thumbnail`, formData, {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'multipart/form-data',
        },
      })
      .then((response) => {
        // 요청 성공 시 처리
        console.log('성공');
        console.log(response.data);
        setError(false);
        imgUrl.current = response.data
      })
      .catch((error) => {
        // 요청 실패 시 처리
        console.log('애러');
        console.log(error);
        setError(true);
      });
  };

  return (
    <CreateRoomPageContainer>
      <Header></Header>
      <Container>
        <div>
          <h1>스터디 만들기</h1>
          <RoomForm
            formError={formError}
            setError={setError}
            onSubmit={handleSubmit}
            isLoading={isLoading}
            setSelectedFile={setSelectedFile}
            handleFileUpload={handleFileUpload}
          ></RoomForm>
        </div>
      </Container>
    </CreateRoomPageContainer>
  );
};

export default CreateRoomPage;
