import styled from 'styled-components';
import { Header } from '../../components/organisms/Header';
import { MainTemplate } from '../../components/templates/MainTemplate';
import { Footer } from '../../components/moecules/Footer';

export const Main = () => {
  return (
    <Container>
      <Header />
      <MainTemplate />
      <Footer />
    </Container>
  );
};

const Container = styled.div``;
