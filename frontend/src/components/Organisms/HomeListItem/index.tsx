import styled from 'styled-components';
import { AiFillHeart, AiOutlineHeart } from 'react-icons/ai';

export type HomeListItemProps = {};

export const HomeListItem = (props: HomeListItemProps) => {
  return (
    <Container>
      <h2>내가 만든 스터디</h2>
      <p className="describe">내가 만든 스터디입니다.</p>
      <List>
        <ImageContaienr>
          <img src="" alt={''} />
          <i>
            <AiOutlineHeart />
          </i>
        </ImageContaienr>
        <h3>제목</h3>
        <p>동해물과 백두산이</p>
        <InfoContainer>
          <div className="participants">
            <p>인원</p>
            <p>1/10</p>
          </div>
          <div className="favorites">
            <AiFillHeart />0
          </div>
        </InfoContainer>
        <div className="divider" />
        <Tags></Tags>
      </List>
    </Container>
  );
};

const Container = styled.div`
  display: flex;
  flex-direction: column;
  width: 100%;

  .divider {
    width: 100%;
    height: 1px;
  }
`;

const ImageContaienr = styled.div`
  position: relative;

  img {
    border-radius: 8px;
  }
`;

const InfoContainer = styled.div`
  display: flex;
  justify-content: space-between;

  .participants {
  }

  .favorites {
  }
`;

const Tags = styled.div`
  display: flex;
`;

const List = styled.div``;
