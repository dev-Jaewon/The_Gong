import styled from "styled-components";

type CarousalSlideProps = {
  length: number;
  children: React.ReactNode;
};

const CarousalSlideContainer = styled.div<{ length: number }>`
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;

  width: calc(100% / ${({ length }) => length});
  height: fit-content;

`;

const Container = styled.div`
`;

function CarousalSlide({ children, length }: CarousalSlideProps) {
  return <CarousalSlideContainer length={length}><Container/>{children}</CarousalSlideContainer>;
}

export default CarousalSlide;