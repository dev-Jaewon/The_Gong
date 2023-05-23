import styled from "styled-components";

interface HorizontalProps {
  children: React.ReactNode;
  gap?: number;
}

const HorizontalContainer = styled.div<HorizontalProps>`
  height: 100%;
  flex: 1;
  display: flex;
  justify-content: center;
  gap: ${({ gap }) => gap || 0}rem;
`;

function Horizontal({ children, gap }: HorizontalProps) {
  return <HorizontalContainer gap={gap}>{children}</HorizontalContainer>;
}

export default Horizontal;
