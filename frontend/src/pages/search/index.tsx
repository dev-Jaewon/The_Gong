import styled from 'styled-components';
import { Header } from '../../components/organisms/Header';

export const Search = () => {
  return (
    <Container>
      <Header />
    </Container>
  );
};

const Container = styled.div`
  display: flex;
  flex-direction: column;
`;
