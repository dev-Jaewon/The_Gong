import styled from "styled-components";
import { Link } from 'react-router-dom';

const FooterContainer = styled.div`
  background-color : #F0F0F0;
  width: 100%;
  height: 20rem;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 3rem;
  font-weight: bold;
  margin-top: 20rem;
`


const Footer = () => {
  return (
    <FooterContainer>
      푸-터
    </FooterContainer>
  )
}

export default Footer;