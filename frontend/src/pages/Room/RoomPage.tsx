import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import { useLocation } from 'react-router-dom';
import Chat from './Chat';
import Horizontal from './RoomHorizontal';
import Verticality from './RoomVerticality';
import RoomParts from './RoomParts';

import { MdViewModule } from 'react-icons/md';
import { MdViewQuilt } from 'react-icons/md';
import Webcam from './Webcam';

const RoomPage = () => {
  const [userName, setUserName] = useState('');
  const [chatOn, setchatON] = useState(true);
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);
  const roomId = searchParams.get('roomId');
  const roomInfo = searchParams.get('roomInfo');
  console.log(roomInfo)

  const edge = 1.5;
  const mainColor = '#4FAFB1';

  useEffect(() => {
    // 페이지 진입 시 로컬 스토리지 값 확인
    const usermemberId = localStorage.getItem('nickname');

    if (usermemberId) {
      setUserName(JSON.parse(usermemberId));
    } else {
      console.log('스토리지 값 없음');
    }
  }, []);


  return (
    <RoomPageContainer>
      {userName && roomId && roomInfo &&(
        <>
          <RoomPageLeft>
            <RoomPageTitle>
              <Horizontal gap={0.5}>
                <RoomParts topLeft={edge} flex={1} bgColor={mainColor}>
                  <div className="spanDiv">
                    <span className="icon">{roomId}</span>
                  </div>
                </RoomParts>
                <RoomParts flex={4} color={'rgb(75, 75, 75)'}>
                  <div className='iconContainer'>
                    <span className="icon left">
                      {roomInfo}
                    </span>
                  </div>
                </RoomParts>
                {/* <RoomParts flex={0.3} color={mainColor} gap={1}>
                  <MdViewModule></MdViewModule>
                  <MdViewQuilt></MdViewQuilt>
                </RoomParts> */}
              </Horizontal>
            </RoomPageTitle>

            {/* 카메라 컨테이너 */}
            {/* <Webcam roomId={roomId} userName={userName} edge={edge} mainColer={mainColer}/> */}
            <Webcam
              room={roomId}
              name={userName}
              edge={edge}
              mainColor={mainColor}
              
            />
          </RoomPageLeft>

          <RoomPageRight>
            {/* 채팅 컨테이너 */}
            <Chat
              roomId={roomId}
              userName={userName}
              edge={edge}
              mainColer={mainColor}
            />
          </RoomPageRight>
        </>
      )}
    </RoomPageContainer>
  );
};

const RoomPageContainer = styled.div`
  @font-face {
    font-family: 'BMDOHYEON';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_one@1.0/BMDOHYEON.woff')
      format('woff');
    font-weight: normal;
    font-style: normal;
  }

  display: flex;
  gap: 0.5rem;

  padding: 3rem;
  width: 100vw;
  height: 100vh;
  background-color: rgb(215, 234, 234);

  .dummy {
    width: 4rem;
  }

  .icon {
    font-weight: normal;
    font-size: 1rem;
    line-height: 1.5rem;
  }

  .iconContainer{
    width: 100%;
  }

  .left{
    margin: 0 2rem;
  }

  .spanDiv {
    width: 70%;
    display: flex;
    justify-content: space-between;
    line-height: 2rem;
  }

  font-family: 'BMDOHYEON';

  @media screen and (max-width: 50rem) {
    .icon {
      font-size: 0.8rem;
    }
  }

`;

const RoomPageRight = styled.div`
  display: flex;
  width: 20rem;

  @media screen and (max-width: 50rem) {
    display: none;
  }
`;
const RoomPageLeft = styled.div`
  flex: 4;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;

  @media screen and (max-width: 64rem) {

  }

  @media screen and (max-width: 36rem) {

  }
`;
const RoomPageTitle = styled.div`
  height: 5%;
`;

export default RoomPage;
