import styled from "styled-components";
import RoomParts from "./RoomParts";
import RoomHorizontal from "./RoomHorizontal"
import RoomVerticality from "./RoomVerticality";
import Chat from './Chat';
// import Webcam from './Webcam';

import { MdScreenShare } from "react-icons/md"
import { BsFillMicFill } from "react-icons/bs"
import { BsFillCameraVideoFill } from "react-icons/bs"
import { BsTelephoneXFill } from "react-icons/bs"

import { AiFillSetting } from "react-icons/ai"
import { RiMessage2Fill } from "react-icons/ri"
import { HiPencil } from "react-icons/hi"
import { FaUserCircle } from "react-icons/fa"

import { MdViewModule } from "react-icons/md"
import { MdViewQuilt } from "react-icons/md"

import { FaUser } from "react-icons/fa"
import { useState } from "react";

type RoomPageProps = {
  socket: {
    chatSocket: any; 
    rtcSocket: any; 
  }
  roomId?: string | null;
  userName: string;
};

const RoomPage1: React.FC<RoomPageProps> = ({ socket, roomId, userName }) => {
  // const [participants, setParticipants] = useState<any>(null);

  // const videoOn = () => {
  //   participants[userName].rtcPeer.videoEnabled = !participants[userName].rtcPeer.videoEnabled
  // }

  // const audioOn = () => {
  //   participants[userName].rtcPeer.audioEnabled = !participants[userName].rtcPeer.audioEnabled
  // }


  const edge = 1.5;
  const mainColer = 'rgb(79, 175, 177)';


  return(
    <RoomPageContainer1>
      <div className=""></div>
      <RoomPageLeft>
        {/* 제목 부분 */}
        <RoomPageTitle>
          <RoomHorizontal gap={0.5}>
            <RoomParts topLeft={edge} flex={1} bgColor={mainColer}>
              <div className="spanDiv">
                <span className="icon">{roomId}</span>
                <span className="icon"> | </span>
                <span className="icon"><FaUser/>6</span>
              </div>
            </RoomParts>
            <RoomParts flex={4} color={'rgb(75, 75, 75)'}>
              <span className="icon">공지사항 : 2주 남았습니다 다들 더 일하십쇼</span> 
            </RoomParts>
            <RoomParts flex={0.3} color={mainColer} gap={1}>
              <MdViewModule></MdViewModule>
              <MdViewQuilt></MdViewQuilt>
            </RoomParts>
          </RoomHorizontal>
        </RoomPageTitle>

        {/* 캠 부분 */}
        <CamContainer>
          {/* <Webcam rtcSocket={socket.rtcSocket} roomId={roomId} userName={userName} setParticipants={setParticipants}/> */}
        </CamContainer>

        {/* 컨트롤러 부분 */}
        <RoomController>
          
          <RoomParts radius={edge} bottomLeft={edge} width={4} bgColor={mainColer} >The</RoomParts>
          
          <RoomParts width={20} color={mainColer}>
            {/* <BsFillCameraVideoFill onClick={()=>{videoOn()}}></BsFillCameraVideoFill>
            <BsFillMicFill onClick={()=>{audioOn()}}></BsFillMicFill> */}
            <MdScreenShare></MdScreenShare>
            <BsTelephoneXFill></BsTelephoneXFill>
          </RoomParts>
          
          <div className="dummy"></div>

        </RoomController>
      </RoomPageLeft>

      <RoomPageRight>
        <RoomVerticality gap={0.5}>

          <RoomParts topRight={edge} flex={1} bgColor={mainColer}></RoomParts>
          
          <RoomParts flex={4}>
            <Chat chatSocket={socket.chatSocket} roomId={roomId} userName={userName} />
          </RoomParts>

          <RoomParts bottomRight={edge} height={4} bgColor={mainColer}>
            <FaUserCircle></FaUserCircle>
            <RiMessage2Fill></RiMessage2Fill>
            <HiPencil></HiPencil>
            <AiFillSetting></AiFillSetting>
          </RoomParts>
        
        </RoomVerticality>
      </RoomPageRight>

    </RoomPageContainer1>
  )
}

const RoomPageContainer1 = styled.div`
@font-face {
  font-family: 'BMDOHYEON';
  src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_one@1.0/BMDOHYEON.woff') format('woff');
  font-weight: normal;
  font-style: normal;
}

 display: flex;
 gap: 0.5rem;

 padding: 3rem;
 width: 100vw;
 height: 100vh;
 background-color: rgb(215, 234, 234);

 .dummy{
  width: 4rem;
 }
 
 .icon{
  font-weight: normal;
  font-size: 1rem;
    line-height: 1.5rem;

 }

 .spanDiv{
  width: 70%;
  display: flex;
  justify-content: space-between;
  line-height: 2rem;
 }

 font-family: 'BMDOHYEON';


`

const RoomPageRight = styled.div`
  display: flex;
  width: 20rem;
`
const RoomPageLeft = styled.div`
  flex: 4;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
`

const RoomPageTitle = styled.div`
  height: 3rem;
`

const CamContainer = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 2rem;
  padding:1.5rem 1.5rem 1.5rem 0 ;

  img{
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
`
const RoomController = styled.div`
  display: flex;
  justify-content: space-between;
  gap: 0.5rem;

  height: 4rem;
`


export default RoomPage1;