import styled from "styled-components";

interface CarousalSlideProps {
  length: number;
  children: React.ReactNode;
  contentheight: number;
};

const CarousalSlideContainer = styled.div<{ length: number, contentheight: number }>`
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;

  // 한 개의 슬라이드 너비
  // 슬라이드 박스 너비 / 슬라이드 갯수
  width: calc(100% / ${({ length }) => length});
  /* height: ${({contentheight}) => contentheight ? `${contentheight}rem` : 'none'}; */
  height:100%;

  img{
    object-fit: cover;
    width: 100%;
  }
`;

const Container = styled.div`
`;

function CarousalSlide({ children, length, contentheight }: CarousalSlideProps) {
  return <CarousalSlideContainer length={length} contentheight={contentheight}><Container/>{children}</CarousalSlideContainer>;
}

export default CarousalSlide;