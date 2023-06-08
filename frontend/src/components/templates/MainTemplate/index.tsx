import styled from 'styled-components';
import { HomeList } from '../../organisms/HomeList';
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

export type MainTemplateProps = {
  myRoom: { data: Array<RoomType> };
  newRoom: { data: Array<RoomType> };
  popularRoom: { data: Array<RoomType> };
};

export const MainTemplate = ({
  myRoom,
  newRoom,
  popularRoom,
}: MainTemplateProps) => {
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
        <HomeList
          id="newRoom"
          title="새로운 스터디"
          description="최근에 만들어진 스터디 목록입니다."
          imgMaxWidth="400px"
          slidesToShow={3}
          list={newRoom.data}
        />
        <HomeList
          id="popularRoom"
          title="추천 스터디"
          description="가장 인기있는 스터디 목록입니다."
          imgMaxWidth="400px"
          slidesToShow={3}
          list={popularRoom.data}
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


