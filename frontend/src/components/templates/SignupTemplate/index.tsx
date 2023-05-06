import styled from 'styled-components';
import { InputLabel } from '../../moecules/InputLabel';
import { FormEvent } from 'react';

export type SignupFormProps = {
  onSubmit: (e: FormEvent<HTMLFormElement>) => void;
};

export const SignupTemplate = ({ onSubmit }: SignupFormProps) => {
  return (
    <Container>
      <SignupForm onSubmit={onSubmit}>
        <InputContainer>
          <Header>
            <h1>회원가입</h1>
          </Header>
          <InputLabel label="닉네임" placeholder="닉네임을 입력해주세요." />
          <InputLabel label="이메일" placeholder="이메일을 입력해주세요." />
          <InputLabel
            label="비밀번호"
            placeholder="영문, 숫자 8자이상의 비밀번호를 입력해주세요."
          />
          <InputLabel
            label="비밀번호 확인"
            placeholder="비밀번호를 재확인해주세요."
          />
          <button>회원가입</button>
        </InputContainer>
      </SignupForm>
    </Container>
  );
};

const Container = styled.div`
  display: flex;
  width: 100%;
  height: 100vh;
  justify-content: center;
`;

const SignupForm = styled.form`
  display: flex;
  justify-content: center;
  min-width: 400px;
  font-family: Noto Sans KR;
`;

const Header = styled.div`
  padding-bottom: 15px;

  h1 {
    font-size: 25px;
    font-weight: 500;
  }
`;

const InputContainer = styled.div`
  padding: 20px;
  display: flex;
  gap: 20px;
  width: 400px;
  height: 100%;
  flex-direction: column;
  justify-content: center;
`;
