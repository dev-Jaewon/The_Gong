import styled from "styled-components";

type CarouselDotProps = {
  idx: number;
  func: (idx: number) => void;
};

const CarouselDotContainer = styled.span`
  display: inline-block;
  width: 0.5rem;
  height: 0.5rem;
  background-color: gray;
  border-radius: 0.2rem;
  margin: 0 0.2rem;
  cursor: pointer;
`;

function CarouselDot({ idx, func }: CarouselDotProps) {
  return <CarouselDotContainer onClick={() => func(idx)} />;
}

export default CarouselDot;
