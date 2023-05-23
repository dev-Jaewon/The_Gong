import styled from "styled-components";
import { useRef, useState } from "react";
import { useEffect } from "react";
import SockJS from "sockjs-client";
import RoomParts from "./RoomParts";
import * as kurentoUtils from 'kurento-utils';
import { useNavigate } from 'react-router-dom';

import { MdScreenShare } from "react-icons/md"
import { BsCameraVideoOffFill } from "react-icons/bs"
import { BsFillMicFill } from "react-icons/bs"
import { BsFillMicMuteFill } from "react-icons/bs"
import { BsFillCameraVideoFill } from "react-icons/bs"
import { IoLogOut } from "react-icons/io5";

interface WebcamProps {
  roomId: string;
  userName: string;
  edge: number;
  mainColer: string;
}

const Webcam: React.FC<WebcamProps> = ({ roomId, userName, edge, mainColer }) => {
  let participants = useRef<{ [key: string]: any }>({});
  let view: boolean = true;
  let rtcSocket = useRef<WebSocket | null>(null);
  const navigate = useNavigate();


  // 처음 렌더링 될 때 실행되는 코드
  // 언마운트 될 때 실행되는 클린 업 함수
  useEffect(() => {
    rtcSocket.current = new SockJS(
      "https://ec2-13-209-93-6.ap-northeast-2.compute.amazonaws.com:8443/groupcall"
    );

    // 연결될 때 실행되는 이벤트 핸들러 코드
    rtcSocket.current.onopen = () => {
      console.log('========== 화상채팅 연결됨 ==========')

      var message = {
        id: "joinRoom",
        name: userName,
        room: roomId,
      };

      sendMessage(message);
    };

    // 연결이 끊어졌을 때 실행되는 코드
    return () => {

      if (rtcSocket.current && rtcSocket.current.readyState === SockJS.OPEN) {

        // 예상하지 못한 오류를 방지하기위해 명시적으로 연결을 끊어주는 코드
        rtcSocket.current.close();
        console.log('========== 화상채팅 연결 종료 ==========');
      }
    };

  }, []);

  function sendMessage(message:any) {
    if (rtcSocket.current && rtcSocket.current.readyState === SockJS.OPEN) {
      const jsonMessage = JSON.stringify(message);
      
      console.log('========== 이거 보낸다 ==========')
      console.log(jsonMessage)

      rtcSocket.current.send(jsonMessage);
    } else {
      console.error("Connection is not established yet.");
    }
  }

  // 컴포넌트가 렌더링 될 때 마다 실행되는 코드
  useEffect(() => {

  // 서버로부터 응답 메시지를 받았을 때 실행되는 코드 
  if (rtcSocket.current !== null) {
    rtcSocket.current.onmessage = (message) => {
      var parsedMessage = JSON.parse(message.data);
    
      console.log('========== 이거 받았다 ==========')
      console.log(parsedMessage)

      switch (parsedMessage.id) {
        case "existingParticipants":
          console.log('========== existingParticipants ==========');
          onExistingParticipants(parsedMessage);
          break;

        case "newParticipantArrived":
          console.log('========== newParticipantArrived ==========');
          onNewParticipant(parsedMessage);
          break;

        case "receiveVideoAnswer":
          console.log('========== receiveVideoAnswer ==========');
          receiveVideoResponse(parsedMessage);
          break;
          
        case "iceCandidate":
          console.log('========== iceCandidate ==========');
          participants.current[participants.current.name].rtcPeer.addIceCandidate(
            parsedMessage.candidate,
            function (error: Error) {
              if (error) {
                console.error("Error adding candidate: " + error);
                return;
              }
              // changeParticipants()

            }
          );
          break;

        default:
          console.error("알 수 없는 메시지 : ", parsedMessage);
      }
    };
  }
  });
  
  //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@


  function onExistingParticipants(msg:any) {

    var constraints = {
      audio: true,
      video: {
        mandatory: {
          maxWidth: 2000,
          maxFrameRate: 15,
          minFrameRate: 15,
        },
      },
    };

    var participant = new Participant(userName);
    participants.current[userName] = participant;
    var video = participant.getVideoElement();

    var options = {
      localVideo: video,
      mediaConstraints: constraints,
      onicecandidate: participant.onIceCandidate.bind(participant),
    };
    
    participant.rtcPeer = new (kurentoUtils.WebRtcPeer.WebRtcPeerSendonly as any)(
      options,
      (error: string | undefined) => {
        if (error) {
          return console.error(error);
        }
      }
    );

    participant.rtcPeer.generateOffer(participant.offerToReceiveVideo.bind(participant));

    msg.data.forEach(receiveVideo);
  }


  function onNewParticipant(request:any) {
    receiveVideo(request.name);
  }

  function receiveVideoResponse(result:any) {
 
    
    // participants[result.name].rtcPeer 
    // 해당하는 참가자의 WebRTC Peer 객체

    // processAnswer
    // 응답 SDP를 처리하고, 연결 설정을 수행합니다

    // result.sdpAnswer,
    // 이 응답 SDP에는 상대방이 선택한 미디어 형식, 코덱, IP 주소, 포트 번호 등이 포함되어 있습니다. 
    // 응답 SDP를 처리하여 자신의 WebRTC Peer 객체에 연결 설정을 수행하고, 
    // 이를 통해 미디어 스트림의 전송이 이루어집니다.

    participants.current[result.name].rtcPeer.processAnswer(
      result.sdpAnswer,
      function (error:Error) {
        if (error) return console.error(error);
      }
    );
  }

  // function callResponse(message:any) {
  //   if (message.response != "accepted") {
  //     // console.info("Call not accepted by peer. Closing call");
  //     // stop();
  //   } else {
  //     kurentoUtils.processAnswer(message.sdpAnswer, function (error:Error) {
  //       if (error) return console.error(error);
  //     });
  //   }
  // }


  function receiveVideo(sender:any) {

    var participant = new Participant(sender);
    participants.current[sender] = participant;
    var video = participant.getVideoElement();

    var options = {
      remoteVideo: video,
      onicecandidate: participant.onIceCandidate.bind(participant),
    };

    participant.rtcPeer = new (kurentoUtils.WebRtcPeer.WebRtcPeerRecvonly as any)(
      options,
      (error: string | undefined) => {
        if (error) {
          return console.error(error);
        }
      }
    );    

    participant.rtcPeer.generateOffer(participant.offerToReceiveVideo.bind(participant));

    }

  //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@


  class Participant {
    name: string;
    container: HTMLDivElement;
    span: HTMLSpanElement;
    video: HTMLVideoElement;
    rtcPeer: any;

    constructor(name: string) {
      this.name = name;
      this.container = document.createElement("div");
      this.container.id = name;
      this.container.classList.add("container");
      this.span = document.createElement("span");
      this.video = document.createElement("video");
      this.span.classList.add("name");
      this.video.classList.add("video");
      this.video.classList.add("roomCam");
      this.rtcPeer = null;
  
      this.container.appendChild(this.video);
      this.container.appendChild(this.span);
  
      if (view) {
        const participants1 = document.getElementById("participants1");
        if (participants1) {
          participants1.appendChild(this.container);
        }
        view = !view;
      } else {
        const participants2 = document.getElementById("participants2");
        if (participants2) {
          participants2.appendChild(this.container);
        }
        view = !view;
      }
  
      this.span.appendChild(document.createTextNode(name));
  
      this.video.id = "video-" + name;
      this.video.autoplay = true;
      this.video.controls = false;
    }
  
    getElement(): HTMLDivElement {
      return this.container;
    }
  
    getVideoElement(): HTMLVideoElement {
      return this.video;
    }
  
    offerToReceiveVideo(error: any, offerSdp: any, wp: any) {
      if (error) return console.error("sdp offer error");
      // console.log("Invoking SDP offer callback function");
  
      console.log('========== offer ==========');
      console.log(offerSdp);

      const msg = {
        id: "receiveVideoFrom",
        sender: this.name,
        sdpOffer: offerSdp,
      };

      sendMessage(msg);
    }
  
    onIceCandidate(candidate: any, wp: any) {
      // console.log("Local candidate" + JSON.stringify(candidate));
  
      const message = {
        id: "onIceCandidate",
        candidate: candidate,
        name: this.name,
      };
      sendMessage(message);
    }
  }

  
  //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@


  const [isCameraOn, setCameraOn] = useState(true);
  const [isMicOn, setMicOn] =  useState(true);
  

  const videoOn = () => {
    setCameraOn(prev => !prev)
    participants.current[userName].rtcPeer.videoEnabled = !participants.current[userName].rtcPeer.videoEnabled
  }

  const audioOn = () => {
    setMicOn(prev => !prev)
    participants.current[userName].rtcPeer.audioEnabled = !participants.current[userName].rtcPeer.audioEnabled
  }

  const roomLeave = () => {
    if (rtcSocket.current && rtcSocket.current.readyState === SockJS.OPEN) {
      navigate(`/`);
      rtcSocket.current.close();

    sendMessage({
      id : 'leaveRoom'
    });
  
    // for ( var key in participants) {
    //   participants.current[key].dispose();
    // }
  
      console.log('========== 화상채팅 연결 종료 ==========');
    }
  }

  return (
    <WebcamContainer>
      <RoomCamContainer>
        <div id="participants1"></div>
        <div id="participants2"></div>
      </RoomCamContainer>

      <RoomController>
          
          <RoomParts radius={edge} bottomLeft={edge} width={4} bgColor={mainColer} >The</RoomParts>
          
          <RoomParts width={20} color={mainColer}>

            { isCameraOn 
            ? <BsFillCameraVideoFill className="ControllerIcon" onClick={()=>{videoOn()}}></BsFillCameraVideoFill>
            : <BsCameraVideoOffFill className="ControllerIcon off" onClick={()=>{videoOn()}}></BsCameraVideoOffFill>
            }

            { isMicOn 
            ? <BsFillMicFill className="ControllerIcon" onClick={()=>{audioOn()}}></BsFillMicFill>
            : <BsFillMicMuteFill className="ControllerIcon off" onClick={()=>{audioOn()}}></BsFillMicMuteFill>
            }

            <MdScreenShare className="ControllerIcon"></MdScreenShare>
            <IoLogOut className="ControllerIcon off logOut" onClick={()=>{roomLeave()}}></IoLogOut>
          </RoomParts>
          
          <div className="dummy"></div>

        </RoomController>
    </WebcamContainer>
  );
};

const WebcamContainer = styled.div`
  flex: 1;
  width: 100%;
`;

const RoomCamContainer = styled.div`
  display: flex;
  flex-direction: column;
  height: calc(100% - 4rem);
  /* max-height: 45rem;
	border: 2px solid red; */

  #participants1,
  #participants2 {
    flex: 1;
    display: flex;
    justify-content: center;
    gap: 2rem;
  }

  .roomCam {
    flex: 1;
    border-radius: 2.5rem;
    box-shadow: rgba(0, 0, 0, 0.15) 0px 2px 8px;
    overflow: hidden;
    max-width: 40rem;
  }

  .video {
    width: 100%;
    max-height:375px;
  
  }

  .container {
    position: relative;
  }

  .name {
    position: absolute;
    top: 3%;
    left: 3%;
    padding: 0.5rem;
    background-color: white;
    border-radius: 0.5rem;
  }
`;

const RoomController = styled.div`
  display: flex;
  justify-content: space-between;
  gap: 0.5rem;

  height: 4rem;

  .ControllerIcon{
    cursor: pointer;
  }

  .off{
    color: tomato;
  }

  .logOut{
    font-size: 1.7rem;
  }
`


export default Webcam;