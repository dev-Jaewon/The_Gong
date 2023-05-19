import { useState } from 'react';
import { SignupData, SignupForm } from '../../organisms/SignupForm';
import { StepProgressBar } from '../../atoms/StepProgressBar';
import { SignupComplete } from '../../organisms/SignupComplete';
import { useMutation } from '@tanstack/react-query';
import { SIGNUP_STEP } from '../../../constans';
import { api } from '../../../util/api';
import styled from 'styled-components';

export const SignupTemplate = () => {
  const [step, setStep] = useState<number>(SIGNUP_STEP.SET_INFO);
  const [errors, setErrors] = useState<SignupData | null>(null);

  const mutation = useMutation({
    mutationFn: async (data: SignupData) => api.post(`https://a5fa-211-193-143-25.ngrok-free.app/members/add`, data),
    onSuccess: () => setStep(SIGNUP_STEP.DONE),
    onError: (err: any) => {
      const message = err.response.data.message as string;
      const obj = {} as SignupData;

      if (message.match(/닉네임/)) obj['nickname'] = message;
      if (message.match(/이메일/)) obj['email'] = message;

      setErrors(obj);
    },
  });

  const handleSubmit = (data: SignupData) => {
    mutation.mutate(data);
  };

  const stepComponent = [
    <SignupForm
      onSubmit={handleSubmit}
      isLoading={mutation.isLoading}
      errors={errors}
    />,
    <SignupComplete />,
  ];

  return (
    <Container>
      <InputContainer>
        <h1>회원가입</h1>
        <StepProgressBar steps={['정보입력', '완료']} currentStep={step} />
        {stepComponent[step - 1]}
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
  width: 400px;
  height: 100%;
  flex-direction: column;
  justify-content: center;
`;
