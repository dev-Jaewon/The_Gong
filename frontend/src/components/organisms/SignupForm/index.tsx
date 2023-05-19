import styled from 'styled-components';
import { Button } from '../../atoms/Button';
import { useForm } from '../../../hooks/useForm';
import { InputLabel } from '../../moecules/InputLabel';
import { useEffect } from 'react';

export type SignupData = {
  nickname: string;
  email: string;
  password: string;
  passwordRe: string;
};

export type SignupFormProps = {
  errors: SignupData | null;
  isLoading: boolean;
  onSubmit: (value: SignupData) => void;
};

export const SignupForm = (props: SignupFormProps) => {
  const { data, errors, handleChange, handleSubmit, setErrors } =
    useForm<SignupData>({
      validations: {
        nickname: {
          required: {
            value: true,
            message: '닉네임은 필수로 입력하셔야합니다.',
          },
        },
        email: {
          required: {
            value: true,
            message: '이메일은 필수로 입력하셔야합니다.',
          },
        },
        password: {
          required: {
            value: true,
            message: '패스워드는 필수로 입력하셔야합니다.',
          },
        },
        passwordRe: {
          required: {
            value: true,
            message: '패스워드는 필수로 입력하셔야합니다.',
          },
        },
      },
      onSubmit: handleSubmitFormHook,
    });

  function handleSubmitFormHook() {
    props.onSubmit(data);
  }

  useEffect(() => {
    if (props.errors) setErrors(props.errors);
  }, [props.errors]);

  return (
    <ContainerForm onSubmit={handleSubmit}>
      <InputLabel
        label="닉네임"
        onChange={handleChange('nickname')}
        placeholder="닉네임을 입력해주세요."
        errorMessage={errors.nickname}
        isValid={errors.nickname ? false : true}
      />
      <InputLabel
        label="이메일"
        onChange={handleChange('email')}
        placeholder="이메일을 입력해주세요."
        errorMessage={errors.email}
        isValid={errors.email ? false : true}
      />
      <InputLabel
        type="password"
        label="비밀번호"
        onChange={handleChange('password')}
        placeholder="영문, 숫자 8자이상의 비밀번호를 입력해주세요."
        errorMessage={errors.password}
        isValid={errors.password ? false : true}
      />
      <InputLabel
        type="password"
        label="비밀번호 확인"
        onChange={handleChange('passwordRe')}
        placeholder="비밀번호를 재확인해주세요."
        errorMessage={errors.passwordRe}
        isValid={errors.passwordRe ? false : true}
      />
      <Button fillColor isLoading={props.isLoading}>
        회원가입
      </Button>
    </ContainerForm>
  );
};

const ContainerForm = styled.form`
  gap: 20px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  min-width: 400px;
  font-family: Noto Sans KR;

  button {
    margin-top: 20px;
  }
`;
