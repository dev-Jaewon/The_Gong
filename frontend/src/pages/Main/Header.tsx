import styled from "styled-components";
import { Link } from 'react-router-dom';

const HeaderContainer = styled.div`
  border-bottom: 2px solid #eee;
  position: sticky;
  top: 0;
  z-index: 10;
  background-color: white;
`
const Container = styled.div`
  max-width: 74rem;
  margin: 0 auto;
  
  display: flex;
  justify-content: space-between;
  font-size: 1.5rem;
  color: #4A5056;
  z-index: 10;
  padding: 0 2rem;

  button{
    display: inline-block;
    background-color: #4FAFB1;
    color: #fff;
    border-radius: 0.2rem;
    font-size: 1rem;
    padding: 0.5rem;
  }

  input{
    border: 1px solid #4FAFB1;
    border-radius: 0.2rem;
    font-size: 1rem;
    padding: 0.5rem;
    color: #4A5056;
    width: 26rem;
  }
`

const NavContainer = styled.div`
  font-weight: bold;
  display: flex;
  align-items: center;
  gap: 1.5rem;
  width: 50rem;

  .title{
    color: #4FAFB1;
  }
`

const UserContainer = styled.div`

  display: flex;
  align-items: center;
  gap: 1.5rem;
  padding: 0.5rem 0;
  font-size: 1rem;
`


const Header = () => {
  return (
    <HeaderContainer>
      <Container>
        <NavContainer>
          <Link className="title" to="/">THE GONG</Link>
          <Link to="/">Home</Link>
          <Link to="/">MyStudy</Link>
          <input></input>
        </NavContainer>

        <UserContainer>
          <button>
            <Link to="/createRoom">스터디 만들기</Link>
          </button>
          <Link to="/">로그인</Link>
          <Link to="/signup">회원 가입</Link>
        </UserContainer>
      </Container>
    </HeaderContainer>
  )
}

export default Header;