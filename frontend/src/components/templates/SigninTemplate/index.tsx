import styled from 'styled-components';
import logo from '../../../assets/image/logo/logo.png';
import { FcGoogle } from 'react-icons/fc';
import { SiNaver } from 'react-icons/si';
import { SigninForm } from '../../organisms/SigninForm';
import { Button } from '../../atoms/Button';
import { useNavigate } from 'react-router-dom';

export const SigninTemplate = () => {
  const navigate = useNavigate();

  return (
    <Container>
      <InputContainer>
        <img src={logo} alt="logo image" />
        <p className="large">함께하는 스터디, 더 공</p>
        <p className="small">
          학업, 자격증, 수능 공부까지
          <br /> 자기계발의 모든 것
        </p>
        <AuthButton href={import.meta.env.VITE_NAVE_AUTH_URL}>
          <SiNaver size={20} color="#04c500" />
          Continue With Naver
        </AuthButton>
        <AuthButton href={import.meta.env.VITE_GOOGLE_AUTH_URL}>
          <FcGoogle size={20} />
          Continue With Google
        </AuthButton>
        <Divider />
        <SigninForm />
        <Button outline onClick={() => navigate('https://a5fa-211-193-143-25.ngrok-free.app/signup')}>
          회원가입
        </Button>
      </InputContainer>
    </Container>
  );
};

const Container = styled.div`
  display: flex;
  width: 100%;
  height: 100vh;
  justify-content: center;

  h1 {
    font-size: 25px;
    font-weight: 500;
    text-align: center;
    padding-bottom: 15px;
  }
`;

const InputContainer = styled.div`
  display: flex;
  gap: 20px;
  width: 350px;
  height: 100%;
  align-items: center;
  flex-direction: column;
  justify-content: center;

  img {
    width: 100px;

    @media (max-width: 600px) {
      width: 50px;
    }
  }

  .large {
    padding-top: 20px;
    font-size: 25px;
    font-weight: 700;
  }

  .small {
    margin-bottom: 40px;
    text-align: center;
    line-height: 25px;
    font-size: 15px;
    font-weight: 400;
    color: gray;
  }
`;

const Divider = styled.div`
  width: 100%;
  height: 1px;
  margin: 10px 0;
  position: relative;
  border-top: 1px solid #bfc7d6;

  &:before {
    content: 'or';
    width: auto;
    height: auto;
    position: absolute;
    line-height: 10px;
    top: -5px;
    left: 50%;
    transform: translate(-50%, 0);
    background-color: white;
    padding: 0 10px;
    color: #bfc7d6;
  }
`;

const AuthButton = styled.a`
  gap: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 40px;
  border: 1px solid #d3d3d3;
  border-radius: 10px;
  font-weight: 500;
  color: #8c98ba;
  user-select: none;
`;
