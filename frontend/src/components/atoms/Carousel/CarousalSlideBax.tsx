import styled from "styled-components";

interface CarousalSlideBoxProps {
  width: number;
  length: number;
  currentBanner: number;
  move: number;
  transitionTime: number;
  children: React.ReactNode;
};

const CarousalSlideBoxContainer = styled.div<{
  width: number;
  length: number;
  currentBanner: number;
  move: number;
  transitionTime: number;
}>`
  // 전체 너비
  // 슬라이드 한 개의 너비 * 총 콘텐츠의 길이
  width: calc(${({ width }) => width}% * ${({ length }) => length});
  display: flex;
  position: relative;

  // 슬라이드 이동
  // 이동하길 원하는 슬라이드의 순번 * 슬라이드 한 개의 너비 
  left: ${({ currentBanner, move }) => -currentBanner * move}%;
  transition: ${({ transitionTime }) => transitionTime}s ease-in-out;

`;

function CarousalSlideBox({
  children,
  width,
  length,
  currentBanner,
  move,
  transitionTime,
}: CarousalSlideBoxProps) {
  return (
    <CarousalSlideBoxContainer
      width={width}
      length={length}
      currentBanner={currentBanner}
      move={move}
      transitionTime={transitionTime}
    >
      {children}
    </CarousalSlideBoxContainer>
  );
}

export default CarousalSlideBox;
