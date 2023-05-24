import styled from 'styled-components';
import { Button } from '../../atoms/Button';
import { FcCheckmark } from 'react-icons/fc';
import { useNavigate } from 'react-router-dom';

export const SignupComplete = () => {
  const navigate = useNavigate();

  return (
    <Container>
      <div className="content">
        <FcCheckmark size={100} />
        <div className="comple_message">회원가입이 완료 되었습니다.</div>
        <div className="sub_message">
          <p></p>님의 회원가입을 축하합니다.
        </div>
        <div className="sub_message">
          알차고 실속있는 서비스로 찾아뵙겠습니다.
        </div>
      </div>
      <Button fillColor onClick={() => navigate('/signin')}>
        로그인
      </Button>
    </Container>
  );
};

const Container = styled.div`
  display: flex;
  align-items: center;
  flex-direction: column;
  justify-content: space-between;
  width: 400px;
  color: #333333;

  p {
    color: #665367;
  }

  div {
    display: flex;
  }

  button {
    margin-top: 30px;
  }

  .content {
    display: flex;
    align-items: center;
    flex-direction: column;
    justify-content: center;
    width: 100%;
    height: 100%;

    &:after {
      content: '';
      margin-top: 30px;
      width: 100%;
      height: 1px;
      border-top: 1px solid #ededed;
    }
  }

  .comple_message {
    font-size: 20px;
    margin: 20px 0;
  }

  .sub_message {
    font-size: 12px;
    margin-bottom: 10px;
  }
`;
