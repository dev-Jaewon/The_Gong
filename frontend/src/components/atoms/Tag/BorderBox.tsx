import styled from "styled-components";

const BorderBoxContainer = styled.div`
  padding: 1.5rem;
  margin-bottom: 3rem;
  border: 1px solid #4FAFB1;
  border-radius: 0.2em;

  @media screen and (max-width: 36rem) {
    padding: 1rem;

  }
  `;

function BorderBox({ children }: { children: React.ReactNode }) {
    return <BorderBoxContainer>{children}</BorderBoxContainer>;
  }
  
  export default BorderBox;