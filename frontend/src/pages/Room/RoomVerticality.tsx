import styled from "styled-components";

interface VerticalityProps {
  children: React.ReactNode;
  gap?: number;
}

const VerticalityContainer = styled.div<VerticalityProps>`
  width: 100%;
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: ${({ gap }) => gap || 0}rem;
`;

function Verticality({ children, gap }: VerticalityProps) {
  return <VerticalityContainer gap={gap}>{children}</VerticalityContainer>;
}

export default Verticality;
