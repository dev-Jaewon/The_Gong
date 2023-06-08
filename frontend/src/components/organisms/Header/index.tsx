import { Link as LinkButton } from 'react-router-dom';
import styled from 'styled-components';
import { SearchBar } from '../../moecules/SearchBar';
import { BsPencilSquare } from 'react-icons/bs';
import { BiUser } from 'react-icons/bi';
import { useQuery } from '@tanstack/react-query';
import { api } from '../../../util/api';
import { useEffect, useState } from 'react';
import { Button } from '../../atoms/Button';

export const Header = () => {
  const { data } = useQuery(
    ['auth'],
    () =>
      api.get(`${import.meta.env.VITE_BASE_URL}auth`).then((res) => res.data),
    { enabled: Boolean(localStorage.getItem('access_token')) }
  );

  const [nickname, setNickname] = useState();

  useEffect(() => {
    if (data?.nickname) {
      setNickname(data?.nickname);
    } else {
      console.log('스토리지 값 없음');
    }
  }, [data]);

  const logOut = () => {
    localStorage.removeItem('access_token');
    localStorage.clear();
    setNickname(undefined);
  };

  return (
    <Container>
      
      <div className='headerContainer'>
        <div className='rightHeader'>
          <Link className="logo title" to="/">
            THE GONG
          </Link>

          <div className='navContainer'>
            <Link className="logo" to="/">
              HOME
            </Link>

            <Link className="logo" to="/my">
              MYSTUDY
            </Link>
          </div>
          
          <SearchBar />

        </div>

        <div className='leftHeader userContainer'>
          <Link className='hidden' to="/createRoom">
            <button>
              스터디 만들기
            </button>
          </Link>

          <Link to="/signin">
            로그인
          </Link>
          
          <Link className='hidden' to="/signup">
            회원가입
          </Link>
        </div> 
      </div>
    </Container>
  );
};

const Container = styled.div`
  position: sticky;
  top: 0;
  left: 0;
  z-index: 10;
  width: 100%;
  background-color: white;
  border-bottom: 1px solid #eee;

  .headerContainer{
    display: flex;
    justify-content: space-between;
    align-items: center;
    height: 4rem;
    max-width: 70rem;
    margin: 0 auto;
    padding: 1rem;
  }

  .rightHeader{
    flex: 1;
    display: flex;
    align-items: center;
    gap: 2rem;
  }

  .navContainer{
    display: flex;
    gap: 1rem;
    margin-left: 0.5rem;
  }

  .leftHeader{
    display: flex;
    align-items: center;
    gap: 1rem;
    margin-left: 1rem;

    button{
      background-color: #4FAFB1;
      height: 40px;
      color: white;
      padding: 0 1rem;
      border-radius: 0.2rem;
      
    }
  }
  
  .logo{
    color: #404447;
    font-size: 1.2rem;
    font-weight: 700;
  }

  .title{  
    color: #4FAFB1;
    font-size: 1.3rem;
    font-weight: 900;
  }


  @media screen and (max-width: 64rem) {
    .headerContainer{
      padding: 0 1rem;
      height: 3rem;
    }

    .navContainer{
      display: none;
    }

    .logo{
      font-size: 1.2rem;
    }

    .hidden{
      display: none;
    }

    .title{ 
      min-width: fit-content;
    }
  }

  @media screen and (max-width: 36rem) {
    .leftHeader{
      display: none;
    }
  }


`;

const Link = styled(LinkButton)`
  color: #555555;
  font-size: 0.9rem;
`;
