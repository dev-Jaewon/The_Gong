import styled from 'styled-components';
import { Banner } from '../../organisms/Banner';
import { HomeList } from '../../organisms/HomeList';
import { IoMdAddCircle } from 'react-icons/io';
import { useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';

export type RoomType = {
  info: string;
  title: string;
  room_id: number;
  is_private: true;
  image_url: string;
  favorite_count: number;
  member_max_count: number;
  member_current_count: number;
  tags: Array<{ tag_id: number; name: string }>;
  favorite_status: 'NONE' | 'LIKE';
  created_at: string;
};

export type MyStudyTemplateProps = {
  myRoom: { data: Array<RoomType> };
  newRoom: { data: Array<RoomType> };
  popularRoom: { data: Array<RoomType> };
  historyRoom: { data: Array<RoomType> };
};

export const MyStudyTemplate = ({
  myRoom,
  newRoom,
  popularRoom,
  historyRoom,
}: MyStudyTemplateProps) => {
  const navigate = useNavigate();
  const [memberId, setMemberId] = useState('');

  useEffect(() => {
    // 페이지 진입 시 로컬 스토리지 값 확인
    const usermemberId = localStorage.getItem('member_id');
    if ( usermemberId) {
      setMemberId(JSON.parse(usermemberId));
    } else {
      console.log('스토리지 값 없음');
    }
  }, []);

  return (
    <Container>
      <Content>
        {myRoom?.data?.length ? (
          <HomeList
            id="createRoom"
            title="내가 만든 스터디"
            description="내가 만든 스터디 목록입니다."
            imgMaxWidth="550px"
            slidesToShow={2}
            list={myRoom.data}
          />
        ) : (
          <NoneValueContainer>
            <h2>내가 만든 스터디</h2>
            <p className="subject_describe">스터디를 만들어주세요!.</p>
            <CreateButton onClick={() => {
                if(memberId){
                  navigate('/createRoom');
                } else {
                  alert('로그인이 필요한 서비스 입니다.')
                  navigate(`/signin`);
                }
            }}>
              <IoMdAddCircle size={35} color={'rgb(138, 138, 138)'} />
              <p>스터디 만들기</p>
            </CreateButton>
          </NoneValueContainer>
        )}

        <HomeList
          id="newRoom"
          title="참여한 스터디"
          description="최근에 만들어진 스터디 목록입니다."
          imgMaxWidth="400px"
          slidesToShow={3}
          list={newRoom.data}
        />
      </Content>
    </Container>
  );
};

const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
`;

const Content = styled.div`
  display: flex;
  flex-direction: column;
  gap: 8rem;
  width: 100%;
  padding: 4rem 3rem 1rem 3rem;
  max-width: 72rem;

`;

const NoneValueContainer = styled.div`
  display: flex;
  position: relative;
  flex-direction: column;
  width: 100%;

  h2 {
    font-size: 22px;
    font-weight: 700;
    margin-bottom: 15px;
  }

  .subject_describe {
    font-size: 15px;
    font-weight: 400;
    color: #8a8a8a;
    margin-bottom: 15px;
  }
`;

const CreateButton = styled.button`
  font-size: 12px;
  font-weight: 500;
  border-radius: 8px;
  padding: 10px;
  color: rgb(138, 138, 138);
  box-shadow: rgba(100, 100, 111, 0.2) 0px 7px 29px 0px;
`;
