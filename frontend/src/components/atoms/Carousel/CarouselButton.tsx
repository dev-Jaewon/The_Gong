import styled from "styled-components";

type CarouselButtonProps = {
  children: React.ReactNode;
  direction: string;
  func: (direction: string) => void;
};

const CarouselButtonContainer = styled.button`
  font-size: 3rem;
  font-weight: bold;
  color: gray;

  border: none;
  background-color: transparent;

  @media screen and (max-width: 36rem) {
    font-size: 1.5rem;
  } 
  
  cursor: pointer;
`;

function CarouselButton({ children, direction, func }: CarouselButtonProps) {
  return (
    <CarouselButtonContainer onClick={() => func(direction)}>
     {children}
    </CarouselButtonContainer>
  );
}

export default CarouselButton;
