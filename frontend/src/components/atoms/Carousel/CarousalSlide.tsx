import styled from "styled-components";

type CarousalSlideProps = {
  length: number;
  children: React.ReactNode;
};

const CarousalSlideContainer = styled.div<{ length: number }>`
  display: flex;
  justify-content: center;
  align-items: center;

  width: calc(100% / ${({ length }) => length});
  height: 100%;
`;

function CarousalSlide({ children, length }: CarousalSlideProps) {
  return <CarousalSlideContainer length={length}>{children}</CarousalSlideContainer>;
}

export default CarousalSlide;