import { ChangeEvent, InputHTMLAttributes } from 'react';
import styled from 'styled-components';

export type InputProps = InputHTMLAttributes<HTMLInputElement> & {
  isValid?: boolean;
  onChange?: (value: string) => void;
};

export const Input = ({ onChange, ...props }: InputProps) => {
  const handleTextChange = (e: ChangeEvent<HTMLInputElement>) => {
    onChange && onChange(e.target.value);
  };

  return <Container {...props} onChange={handleTextChange} />;
};

const Container = styled.input<{ isValid?: boolean }>`
  width: 100%;
  padding: 10px;
  border-radius: 3px;
  font-family: Noto Sans KR;

  border: ${({ isValid }) => {
    if (isValid === undefined || isValid) return '1px solid #D3D3D3';
    else return '1px solid red !important';
  }};

  &:focus {
    border: 1px solid #4fafb1;
  }
`;
