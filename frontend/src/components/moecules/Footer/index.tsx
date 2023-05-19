import styled from 'styled-components';

export const Footer = () => {
  return (
    <Container>
      <Content>
        <h2 className="title">맑은 눈의 광인들 팀</h2>
        <Info>
          <h3>팀원</h3>
          <div></div>
        </Info>
      </Content>
    </Container>
  );
};

const Container = styled.div`
  display: flex;
  justify-content: center;
`;

const Content = styled.div`
  margin-top: 30px;
  padding-top: 30px;
  border-top: 1px solid rgb(247, 247, 247);
  width: 100%;
  max-width: 1050px;
  display: flex;
  flex-direction: column;

  .title {
    font-weight: 500;
    font-size: 20px;
    line-height: 29px;
  }
`;

const Info = styled.div`
  margin-top: 20px;

  div {
    gap: 10px;
    display: flex;
  }
`;
