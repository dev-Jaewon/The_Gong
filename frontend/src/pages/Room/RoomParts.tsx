import styled, { CSSProperties } from "styled-components";

interface RoomPartsProps extends RoomPartsContainerProps {
  children?: React.ReactNode;
}

interface RoomPartsContainerProps {
  width?: number;
  height?: number;
  flex?: number;
  radius?: number;
  bgColor?: string;
  color?: string;
  gap?: number;
  topLeft?: number;
  topRight?: number;
  bottomLeft?: number;
  bottomRight?: number;
}

const RoomPartsContainer = styled.div<RoomPartsContainerProps>`
  width: ${({ width }) => width + 'rem' || '100%'};
  height: ${({ height }) => height + 'rem' || '100%'};
  display: flex;
  justify-content: center;
  align-items: center;
  flex: ${({ flex }) => flex || 'none'};
  gap: ${({ gap }) => gap || 2.5}rem;

  background-color: ${({ bgColor }) => bgColor || 'white'};
  border-radius: ${({ radius }) => radius || 0.5}rem;
  box-shadow: rgba(0, 0, 0, 0.15) 0px 2px 8px;

  font-size: 1.5rem;
  font-weight: bold;
  color: ${({ color }) => color || 'white'};
  overflow: hidden;

  border-top-left-radius: ${({ topLeft, radius }) => topLeft || radius}rem;
  border-top-right-radius: ${({ topRight, radius }) => topRight || radius}rem;
  border-bottom-left-radius: ${({ bottomLeft, radius }) => bottomLeft || radius}rem;
  border-bottom-right-radius: ${({ bottomRight, radius }) => bottomRight || radius}rem;
`;

const RoomParts: React.FC<RoomPartsProps> = ({
  children,
  width,
  height,
  flex,
  radius,
  bgColor,
  color,
  gap,
  topLeft,
  topRight,
  bottomLeft,
  bottomRight
}) => {
  return (
    <RoomPartsContainer
      width={width}
      height={height}
      flex={flex}
      radius={radius}
      bgColor={bgColor}
      color={color}
      gap={gap}
      topLeft={topLeft}
      topRight={topRight}
      bottomLeft={bottomLeft}
      bottomRight={bottomRight}
    >
      {children}
    </RoomPartsContainer>
  );
};

export default RoomParts;
