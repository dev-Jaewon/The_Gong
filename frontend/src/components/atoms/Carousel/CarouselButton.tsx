import styled from "styled-components";

type CarouselButtonProps = {
children: React.ReactNode;
dr: string;
func: (direction: string) => void;
};

const CarouselButtonContainer = styled.button`
font-size: 2rem;
font-weight: bold;

border: none;
background-color: transparent;

cursor: pointer;
`;

function CarouselButton({ children, dr, func }: CarouselButtonProps) {
return (
<CarouselButtonContainer onClick={() => func(dr)}>
{children}
</CarouselButtonContainer>
);
}

export default CarouselButton;
