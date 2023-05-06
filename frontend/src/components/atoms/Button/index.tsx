import styled from 'styled-components';
import { ButtonHTMLAttributes } from 'react';
import { DEFAULT_BUTTON_HEIGHT, DEFAULT_BUTTON_WIDTH } from '../../../constans';

type CustomType = {
  width?: string;
  height?: string;
  outline?: boolean;
  fillColor?: boolean;
};

export type ButtonProps = ButtonHTMLAttributes<HTMLButtonElement> &
  CustomType & {
    children?: React.ReactNode;
  };

export const Button = (props: ButtonProps) => {
  return <Container {...props} />;
};

const Container = styled.button<CustomType>`
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  font-size: 1rem;
  font-weight: 500;
  border-radius: 10px;
  font-family: Noto Sans KR;

  width: ${({ width }) => width || DEFAULT_BUTTON_WIDTH};
  height: ${({ height }) => height || DEFAULT_BUTTON_HEIGHT};
  color: ${({ fillColor }) => (fillColor ? 'white' : 'black')};
  outline: ${({ outline }) => (outline ? '1px solid #4fafb1' : 'unset')};
  background-color: ${({ fillColor }) => (fillColor ? ' #4fafb1' : 'unset')};
`;
