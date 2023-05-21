import styled from 'styled-components';
import { Banner } from '../../organisms/Banner';
import { HomeList } from '../../organisms/HomeList';

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
  myRoom: Array<RoomType>;
  newRoom: Array<RoomType>;
  popularRoom: Array<RoomType>;
};

export const MainTemplate = ({
  myRoom,
  newRoom,
  popularRoom,
}: MainTemplateProps) => {
  return (
    <Container>
      <Banner />
      <Content>
        {/* <HomeList
          title="내가 만든 스터디"
          description="내가 만든 스터디 목록입니다."
          imgMaxWidth="550px"
          slidesToShow={2}
          list={myRoom}
        /> */}
        <HomeList
          title="내가 만든 스터디"
          description="내가 만든 스터디 목록입니다."
          imgMaxWidth="400px"
          slidesToShow={3}
          list={newRoom}
        />
        <HomeList
          title="내가 만든 스터디"
          description="내가 만든 스터디 목록입니다."
          imgMaxWidth="400px"
          slidesToShow={3}
          list={popularRoom}
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
  gap: 50px;
  width: 100%;
  padding-top: 50px;
  max-width: 1050px;
`;
