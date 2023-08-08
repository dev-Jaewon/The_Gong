import { Link as LinkButton } from 'react-router-dom';
import styled from 'styled-components';
import { BiHome } from 'react-icons/bi';
import { IoMdCreate } from 'react-icons/io';
import { BiBook } from 'react-icons/bi';
import { FiUserPlus } from 'react-icons/fi';

export const BottomHeader = () => {

  return (
    <Container>
      <div className='headerContainer'>
          <Link className="logo" to="/">
            <BiHome className='icon'/>
            홈
          </Link>

          <Link className="logo" to="/my">
            <BiBook className='icon'/>
            마이스터디
          </Link>

          <Link className='logo' to="/createRoom">
            <IoMdCreate className='icon'/>
            스터디 만들기
          </Link>
          
          <Link className='logo' to="/signin">
            <FiUserPlus className='icon'/>
            로그인
          </Link>
      </div>
    </Container>
  );
};

const Container = styled.div`
  position: fixed;
  bottom: 0;
  left: 0;
  z-index: 20;
  width: 100%;
  background-color: white;
  display: none;
  border-top: 1px solid #eee;


  .headerContainer{
    display: flex;
    justify-content: space-between;
    max-width: 36rem;
    width: 100%;
    padding: 0.5rem 2rem;
  }

  .logo{
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 0.5rem;
  }

  .icon{
    font-size: 1.5rem;
  }

  @media screen and (max-width: 64rem) {
    display: flex;
    justify-content: center;
  }


`;

const Link = styled(LinkButton)`
  color: #555555;
  font-size: 0.7rem;
`;
