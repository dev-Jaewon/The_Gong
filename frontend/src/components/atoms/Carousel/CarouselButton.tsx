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
