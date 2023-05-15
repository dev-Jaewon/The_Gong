import styled from 'styled-components';
import useForm from '../../../hooks/useForm';
import { api } from '../../../util/api';
import { Button } from '../../atoms/Button';
import { InputLabel } from '../../moecules/InputLabel';
import { useMutation } from '@tanstack/react-query';
import { SIGNIN_VALID_MESSAGE } from '../../../constans';
import { useState } from 'react';

type SigninData = {
  email: string;
  password: string;
};

export const SigninForm = () => {
  const [fetchErrorMessage, setFetchErrorMessage] = useState<string>('');
  const { data, errors, handleChange, handleSubmit } = useForm<SigninData>({
    validations: {
      email: {
        pattern: {
          value: /[a-z0-9]+@[a-z]+\.[a-z]{2,3}/,
          message: SIGNIN_VALID_MESSAGE.email,
        },
      },
      password: {
        required: {
          value: true,
          message: 'password를 입력해주세요.',
        },
      },
    },
    onSubmit: handleSigninFetch,
  });

  const mutation = useMutation({
    mutationFn: async (data: SigninData) => api.post('/signin', data),
    onError: () => setFetchErrorMessage(SIGNIN_VALID_MESSAGE.login),
  });

  function handleSigninFetch() {
    mutation.mutate(data);
  }

  return (
    <ContainerForm onSubmit={handleSubmit}>
      <InputLabel
        placeholder="email"
        errorMessage={errors.email}
        onChange={handleChange('email')}
        isValid={fetchErrorMessage || errors.email ? false : true}
      />
      <InputLabel
        type="password"
        placeholder="password"
        errorMessage={errors.password}
        onChange={handleChange('password')}
        isValid={fetchErrorMessage || errors.password ? false : true}
      />
      {fetchErrorMessage && <ErrorMessage>{fetchErrorMessage}</ErrorMessage>}
      <Button fillColor>로그인</Button>
    </ContainerForm>
  );
};

const ContainerForm = styled.form`
  gap: 20px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  width: 100%;
  max-width: 400px;
  font-family: Noto Sans KR;

  button {
    margin-top: 20px;
  }
`;

const ErrorMessage = styled.p`
  position: relative;
  font-size: 13px;
  color: red;
  transform: translateY(-10px);
`;
