import styled from 'styled-components';
import { ButtonHTMLAttributes } from 'react';
import { DEFAULT_BUTTON_HEIGHT, DEFAULT_BUTTON_WIDTH } from '../../../constans';

type CustomType = {
  width?: string;
  height?: string;
  outline?: boolean;
  fillColor?: boolean;
  isLoading?: boolean;
  children?: React.ReactNode;
};

export type ButtonProps = ButtonHTMLAttributes<HTMLButtonElement> &
  CustomType & {
    children?: React.ReactNode;
  };

export const Button = ({ isLoading, children, ...props }: ButtonProps) => {
  return (
    <Container {...props}>
      {isLoading ? (
        <LoadingSpinner fillColor={props.fillColor} aria-label="spinner" />
      ) : (
        children
      )}
    </Container>
  );
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

const LoadingSpinner = styled.div<CustomType>`
  width: 20px;
  height: 20px;
  border: 4px solid transparent;
  border-radius: 50%;
  animation: button-loading-spinner 1s ease infinite;
  border-top-color: ${({ fillColor }) => (fillColor ? '#ffffff' : '#4fafb1')};

  @keyframes button-loading-spinner {
    from {
      transform: rotate(0turn);
    }
    to {
      transform: rotate(1turn);
    }
  }
`;
