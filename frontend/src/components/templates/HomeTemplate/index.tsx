import styled from 'styled-components';
import { HomeListItem } from '../../organisms/HomeListItem';

export const HomeTemplate = () => {
  return (
    <Container>
      <HomeListItem></HomeListItem>
    </Container>
  );
};

const Container = styled.div`
  display: flex;
  flex-direction: column;
`;
