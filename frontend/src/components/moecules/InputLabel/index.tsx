import styled from 'styled-components';
import { Input } from '../../atoms/Input';
import { ChangeEvent } from 'react';

export type InputLabelProps = {
  id?: string;
  type?: string;
  label?: string;
  value?: string;
  isValid?: boolean;
  placeholder?: string;
  errorMessage?: string;
  onChange?: (value: string) => void;
};

export const InputLabel = ({
  id,
  type,
  value,
  label,
  isValid,
  placeholder,
  errorMessage,
  onChange,
}: InputLabelProps) => {
  const handleInputChange = (value: ChangeEvent<HTMLInputElement> | string) => {
    if (typeof value === 'string' && onChange) onChange(value);
  };

  return (
    <Container>
      {label && <Label htmlFor={id}>{label}</Label>}
      <Input
        id={id}
        type={type || 'text'}
        value={value}
        isValid={isValid}
        placeholder={placeholder}
        onChange={handleInputChange}
      />
      {!isValid && errorMessage && <ErrorMessage>{errorMessage}</ErrorMessage>}
    </Container>
  );
};

const Container = styled.div`
  display: flex;
  flex-direction: column;
  font-family: Noto Sans KR;
`;

const Label = styled.label`
  font-size: 13px;
  color: #4a5056;
  font-weight: 500;
  margin-bottom: 10px;
`;

const ErrorMessage = styled.p`
  margin-top: 10px;
  font-size: 13px;
  color: red;
`;
