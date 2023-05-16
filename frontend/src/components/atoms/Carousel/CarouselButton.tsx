import styled from "styled-components";

type CarouselButtonProps = {
children: React.ReactNode;
dr: string;
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

function CarouselButton({ children, dr, func }: CarouselButtonProps) {
return (
<CarouselButtonContainer onClick={() => func(dr)}>
{children}
</CarouselButtonContainer>
);
}

export default CarouselButton;
