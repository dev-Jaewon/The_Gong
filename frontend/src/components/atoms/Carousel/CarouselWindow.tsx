import styled from "styled-components";

interface CarouselWindowProps {
  children: React.ReactNode;
}

const CarouselWindowContainer = styled.div`
  position: relative;
  overflow: hidden;
`;

function CarouselWindow({ children }: CarouselWindowProps){
  return(
    <CarouselWindowContainer>
      { children }
    </CarouselWindowContainer>
  );
}

export default CarouselWindow;
