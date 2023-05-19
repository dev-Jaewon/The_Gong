import styled from 'styled-components';
import { Banner } from '../../organisms/Banner';
import { HomeList } from '../../organisms/HomeList';

export const MainTemplate = () => {
  return (
    <Container>
      <Banner />
      <Content>
        <HomeList
          title="내가 만든 스터디"
          description="내가 만든 스터디 목록입니다."
          imgMaxWidth="550px"
          slidesToShow={2}
        />
        <HomeList
          title="내가 만든 스터디"
          description="내가 만든 스터디 목록입니다."
          imgMaxWidth="400px"
          slidesToShow={3}
        />
        <HomeList
          title="내가 만든 스터디"
          description="내가 만든 스터디 목록입니다."
          imgMaxWidth="400px"
          slidesToShow={3}
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
