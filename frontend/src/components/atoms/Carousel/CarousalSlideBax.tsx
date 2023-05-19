import styled from "styled-components";

type CarousalSlideBoxProps = {
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
  width: calc(${({ width }) => width}% * ${({ length }) => length});
  display: flex;
  position: relative;
  left: ${({ currentBanner, move }) => -currentBanner * move}%;
  transition: ${({ transitionTime }) => transitionTime}s;
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
