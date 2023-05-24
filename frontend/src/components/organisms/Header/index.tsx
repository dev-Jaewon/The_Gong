import { Link as LinkButton } from 'react-router-dom';
import styled from 'styled-components';
import { SearchBar } from '../../moecules/SearchBar';
import { BsPencilSquare } from 'react-icons/bs';
import { BiUser } from 'react-icons/bi';
import { useQuery } from '@tanstack/react-query';
import { api } from '../../../util/api';
import { useEffect, useState } from 'react';

export const Header = () => {
  const { data } = useQuery(
    ['auth'],
    () => api.get('https://ec2-13-209-93-6.ap-northeast-2.compute.amazonaws.com:8443/auth').then((res) => res.data),
    { enabled: Boolean(localStorage.getItem('access_token')) }
  );

  const [nickname, setNickname] = useState()

  useEffect(() => {
  const userNickname = localStorage.getItem('nickname');

  if (userNickname) {
    setNickname(JSON.parse(userNickname));
    console.log(setNickname);
  } else {
    console.log('스토리지 값 없음');
  }
  }, []);

  return (
    <Container>
      <div className="content">
        <div className="auth_container">
          {!nickname ? (
            <>
              <Link to="/signin">로그인</Link>
              <div className="divider"></div>
              <Link to="/signup">회원가입</Link>
            </>
          ) : (
            <div className='user'>
              <div>
                <span className='userName'>{nickname}</span>님 환영합니다.
              </div>
              <button>로그아웃</button>
            </div>
          )}
        </div>
        <div className="service_container">
          <Link className="logo" to="/">
            THE GONG
          </Link>
          <div className="search">
            <SearchBar />
          </div>
          <div className="icons">
            {/* <Link to="/my" className="my">
              <BiUser size={30} color="#4a4a4a" />
            </Link> */}
            <Link to="/createRoom">
              <BsPencilSquare size={30} color="#4a4a4a" />
            </Link>
          </div>
        </div>
      </div>
    </Container>
  );
};

const Container = styled.div`
  display: flex;
  padding-bottom: 15px;
  justify-content: center;
  width: 100%;
  user-select: none;
  box-shadow: rgba(0, 0, 0, 0.07) 0px 3px 4px 0px;
  font-family: Noto Sans KR;
  position: relative;
  z-index: 10;

  .user{
    display: flex;
    justify-content: space-between;
    align-items: center;

    width: 12rem;
    font-size: 0.9rem;
    color: rgb(61, 61, 61);

    .userName{
      color: #4FAFB1
    }

    button{
      padding: 0.3rem;
      border: 1px solid #4FAFB1;
      border-radius: 0.2rem;
    }
  }

  > div {
    width: 100%;
  }

  .content {
    display: flex;
    flex-direction: column;
    align-items: center;
    max-width: 1050px;
  }

  .auth_container {
    padding: 10px 0;
    width: 100%;
    display: flex;
    justify-content: end;

    .divider {
      width: 1px;
      height: 13px;
      margin: 0px 12px;
      background-color: rgb(217, 217, 217);
    }
  }

  .service_container {
    position: relative;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 15px 0;
    width: 100%;

    .logo {
      color: #4fafb1;
      font-size: 20px;
      font-weight: 700;
    }

    .search {
      position: absolute;
      left: 50%;
      top: 50%;
      width: 400px;
      transform: translate(-50%, -50%);
    }

    .my {
      padding: 0 10px;
      font-size: 25px;
      font-weight: 700;
    }

    .icons {
      svg {
        margin-left: 30px;
      }
    }

  }
`;

const Link = styled(LinkButton)`
  color: #333333;
  font-size: 12px;
`;
