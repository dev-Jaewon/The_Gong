import { useRef, useState } from 'react';
import { useEffect } from 'react';
import SockJS from 'sockjs-client';
import kurentoUtils from 'kurento-utils';
import styled from 'styled-components';
import RoomParts from './RoomParts';
import { useNavigate } from 'react-router-dom';

import { MdScreenShare } from 'react-icons/md';
import { BsCameraVideoOffFill } from 'react-icons/bs';
import { BsFillMicFill } from 'react-icons/bs';
import { BsFillMicMuteFill } from 'react-icons/bs';
import { BsFillCameraVideoFill } from 'react-icons/bs';
import { IoLogOut } from 'react-icons/io5';


const Webcam = ({room, name, edge, mainColor}) => {

  // 참여자들을 관리하는 변수
  let participants = useRef({});

  // 웹소켓 객체를 관리하는 변수
  const rtcSocket = useRef(null);
  const navigate = useNavigate();

  // 컴포넌트가 처음 마운트 되었을 때 실행되는 코드
  useEffect(() => {

    // 웹소켓 객체 생성 후 변수에 저장
    rtcSocket.current = new SockJS(`${import.meta.env.VITE_BASE_URL}groupcall`);

    // 웹소켓이 연결 되었을 때 실행되는 코드
    rtcSocket.current.onopen = () => {
      console.log('연결됨');

      // if (rtcSocket.current && rtcSocket.current.readyState === SockJS.OPEN) {
      //   sendMessage(message);
      // }

      var message = {
        id: 'joinRoom',
        name: name,
        room: room,
      };

      // 서버로 입장 메시지를 보냅니다.
      sendMessage(message);
      
    };

    // 연결이 끊어졌을 때 실행되는 코드
    return () => {
        // 예상하지 못한 오류를 방지하기위해 명시적으로 연결을 끊어주는 코드
        rtcSocket.current.close();
        console.log('========== 화상채팅 연결 종료 ==========');
    };
  }, []);

  // 브라우저를 닫거나 다른 페이지로 이동할 때 소켓 연결을 명시적으로 끊어주는 코드
  window.onbeforeunload = function() {
     rtcSocket.current.close();
  };


  // 컴포넌트가 렌더링 될 때 마다 실행되는 코드
  useEffect(() => {
    
    // 서버로부터 메시지를 받을 실행되는 코드
    rtcSocket.current.onmessage = (message) => {
      var parsedMessage = JSON.parse(message.data);

      console.log('========== 이거 받았다 ==========');
      console.log(parsedMessage);

      // 받은 메시지에 따라 실행되는 함수들
      switch (parsedMessage.id) {

        // case 1번 
        case 'existingParticipants':
          onExistingParticipants(parsedMessage);
          break;

        // case 2번 
        case 'newParticipantArrived':
          onNewParticipant(parsedMessage);
          break;

        // case 3번 
        case 'participantLeft':
          onParticipantLeft(parsedMessage);;
          break;

        // case 4번 
        case 'receiveVideoAnswer':
          receiveVideoResponse(parsedMessage);
          break;

        // case 5번 
        case 'iceCandidate':
          // 상대방의 피어객체에
          // 상대방이 전달한 ICE Candidate를 추가
          participants.current[parsedMessage.name].rtcPeer.addIceCandidate(
            parsedMessage.candidate,
            function (error) {
              if (error) {
                console.error('Error adding candidate: ' + error);
                return;
              }
            }
          );

          break;

        default:
          console.error('알 수 없는 메시지', parsedMessage);
      }
    };
  });


  function sendMessage(message) {
    if (rtcSocket.current && rtcSocket.current.readyState === SockJS.OPEN) {
      const jsonMessage = JSON.stringify(message);
      rtcSocket.current.send(jsonMessage);
    } else {
      console.error('Connection is not established yet.');
    }
  }


  // case 1번 
  //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

  // SFU서버는 클라이언트가 서버로 자신의 영상 데이터를 보내는 방식입니다.
  // 하지만 상대방의 데이터는 상대방 수 만큼 그대로 받습니다.
  // 참여자수: n
  // Uplink: 1
  // Downlin: n


  // Uplink
  // 기존 참여자들에게 내 영상을 송신하기 위해 
  // offer(미디어 스트림을 교환하기 위한 설정 정보)를 서버로 보내는 코드
  function onExistingParticipants(msg) {

    // 나의 정보를 수집하기 위한 객체 생성
    let participant = new Participant(name);

    // 화상채팅 참가자 명단에 나를 추가
    participants.current[name] = participant;

    // 미디어 스트림을 저장한 변수
    let constraints = {
      audio: true,
      video: {
        mandatory: {
          maxWidth: 1500,
          maxFrameRate: 15,
          minFrameRate: 15,
        },
      },
    };

    // 나의 비디오 요소를 담고 있는 변수
    let video = participant.getVideoElement();

    // offer를 보내기 위한 설정을 담고있는 변수
    let options = {
      localVideo: video,
      mediaConstraints: constraints,
      // Offer를 생성한 이후에 ICE Candidate를 수집하기 위한 옵션
      onicecandidate: participant.onIceCandidate.bind(participant),
    };
    
    // rtcPeer를 WebRtcPeerSendonly 객체로 설정하는 코드
    participant.rtcPeer = new kurentoUtils.WebRtcPeer.WebRtcPeerSendonly(
      options,
      function (error) {
        if (error) {
          return console.error(error);
        }
        // 생성된 WebRtcPeerSendonly객체에 대해 Offer를 생성하고 
        // 해당 Offer를 콜백 함수로 전달
        // 이후 콜백 함수에서 상대방에게 Offer를 전송
        this.generateOffer(participant.offerToReceiveVideo.bind(participant));
      }
    );

    // 기존 참여자들의 
    msg.data.forEach(receiveVideo);
  }


  // Downlin
  // 기존 참여자들의 영상을 수신하기 위해 offer를 보내는 코드
  function receiveVideo(sender) {

    // 기존 참여자들의 정보를 수집하기 위한 객체 생성
    let participant = new Participant(sender);

    // 화상채팅 참가자 명단에 기존 참여자를 추가
    participants.current[sender] = participant;

    // 기존 참여자들의 비디오 요소를 담고 있는 변수
    let video = participant.getVideoElement();

    // offer를 보내기 위한 설정을 담고있는 변수
    let options = {
      remoteVideo: video,
      // Offer를 생성한 이후에 ICE Candidate를 수집하기 위한 옵션
      onicecandidate: participant.onIceCandidate.bind(participant),
    };

    // rtcPeer를 WebRtcPeerRecvonly 객체로 설정하는 코드
    participant.rtcPeer = new kurentoUtils.WebRtcPeer.WebRtcPeerRecvonly(
      options,
      function (error) {
        if (error) {
          return console.error(error);
        }
        this.generateOffer(participant.offerToReceiveVideo.bind(participant));
      }
    );
  }

  // Participant 객체 생성 코드
  //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

  
  // 참가자의 비디오 정보를 관리하는 객체
  function Participant(name) {

    // 이름
    this.name = name;
    let rtcPeer;

    // 비디오와 사용자 이름을 저장한 컨테이너
    let container = document.createElement('div');
    container.id = name;
    container.classList.add('container');
    container.classList.add(name);

    let span = document.createElement('span');
    span.classList.add('name');
    span.textContent = name;

    let video = document.createElement('video');
    video.classList.add('video');
    video.classList.add('roomCam');
    video.id = 'video-' + name;
    video.autoplay = true;
    video.controls = false;

    container.appendChild(video);
    container.appendChild(span);

    // 참여자의 수에 따른 화면 배치
    // participants 값이 비동기적으로 할당되기 때문에
    // video요소의 갯수로 판별
    const isOdd = document.querySelectorAll('video').length % 2 === 0;

    if (isOdd) {
      document.getElementById('participants1').appendChild(container);
    } else {
      document.getElementById('participants2').appendChild(container);
    }

    // 컨테이너를 리턴하는 함수
    this.getElement = function () {
      return container;
    };

    // 비디오를 리턴하는 함수
    this.getVideoElement = function () {
      return video;
    };

    // 상대방에게 offer를 보내는 함수
    this.offerToReceiveVideo = function (error, offerSdp) {
      if(error){
        return console.error('sdp offer error');
      } 

      let msg = {
        id: 'receiveVideoFrom',
        sender: name,
        sdpOffer: offerSdp,
      };

      sendMessage(msg);
    };

    this.onIceCandidate = function (candidate) {

      let message = {
        id: 'onIceCandidate',
        candidate: candidate,
        name: name,
      };
      sendMessage(message);
    };

    Object.defineProperty(this, 'rtcPeer', { writable: true });
  }


  // case 2번 
  //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  
  // 새로운 참가자의 영상을 송신하기 위한 코드
  function onNewParticipant(request) {
    receiveVideo(request.name);
  }


  // case 3번 
  //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

  // 참여자가 화상채팅에서 퇴장했을 때 코드
  function onParticipantLeft(request) {  
    const element = document.getElementById(request.name);
    element.parentNode.removeChild(element);
    delete participants.current[request.name];
  }


  // case 4번 
  //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

  // 상대방이 보낸 answer를 받아 자신의 브라우저에서 처리하여 미디어 스트림을 생성하는 코드
  // result.sdpAnswer - answer로 받은 상대방의 SDP 정보
  function receiveVideoResponse(result) {
    participants.current[result.name].rtcPeer.processAnswer(
      result.sdpAnswer,
      function (error) {
        if (error) return console.error(error);
      }
    );
  }


  //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

  // view 코드
  const [isCameraOn, setCameraOn] = useState(true);
  const [isMicOn, setMicOn] = useState(true);

  const videoOn = () => {
    setCameraOn((prev) => !prev);
    participants.current[name].rtcPeer.videoEnabled =
      !participants.current[name].rtcPeer.videoEnabled;
  };

  const audioOn = () => {
    setMicOn((prev) => !prev);
    participants.current[name].rtcPeer.audioEnabled =
      !participants.current[name].rtcPeer.audioEnabled;
  };

  // participants.current에서 참여자 정보를 가져옴
  const party = participants.current; 

  const toggleDiv = (element, className, enabled) => {
    let div = element.querySelector('div');

    if (enabled) {
      if (!div) {
        div = document.createElement('div');
        div.classList.add(className);
        element.appendChild(div);

        // 'TheGong' 텍스트가 추가된 span 요소 생성
        if (className === 'stopVideo') {
          const span = document.createElement('span');
          span.textContent = 'TheGong';
          // CSS 스타일을 위한 클래스 추가
          span.classList.add('additional-text'); 
          div.appendChild(span);
        }
      }
    } else {
      if (div && div.classList.contains(className)) {
        div.remove();

        // 'TheGong' 텍스트를 포함한 span 요소 제거
        const span = element.querySelector('.additional-text');
        if (span) {
          span.remove();
        }
      }
    }
  }

  for (const key in party) {
    if (party.hasOwnProperty(key)) {
      const participant = party[key];
      const element = document.querySelector(`.${key}`);

      toggleDiv(element, 'stopVideo', !participant.rtcPeer.videoEnabled);
      toggleDiv(element, 'stopAudio', !participant.rtcPeer.audioEnabled);
    }
  }


  const roomLeave = () => {

      sendMessage({
        id: 'leaveRoom',
      });

      rtcSocket.current.close();

      navigate(`/`);

      console.log('========== 화상채팅 연결 종료 ==========');
    
  };

  let view = true;

  //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  

  return (
    <WebcamContainer>
      <CamContainer>
        <div id="participants1"></div>
        <div id="participants2"></div>
      </CamContainer>

      <RoomController>
        <RoomParts
          radius={edge}
          bottomLeft={edge}
          width={4}
          bgColor={mainColor}
        >
          The
        </RoomParts>

        <RoomParts width={20} color={mainColor}>
          {isCameraOn ? (
            <BsFillCameraVideoFill
              className="ControllerIcon"
              onClick={() => {
                videoOn();
              }}
            ></BsFillCameraVideoFill>
          ) : (
            <BsCameraVideoOffFill
              className="ControllerIcon off"
              onClick={() => {
                videoOn();
              }}
            ></BsCameraVideoOffFill>
          )}

          {isMicOn ? (
            <BsFillMicFill
              className="ControllerIcon"
              onClick={() => {
                audioOn();
              }}
            ></BsFillMicFill>
          ) : (
            <BsFillMicMuteFill
              className="ControllerIcon off"
              onClick={() => {
                audioOn();
              }}
            ></BsFillMicMuteFill>
          )}

          <MdScreenShare className="ControllerIcon"></MdScreenShare>
          <IoLogOut
            className="ControllerIcon off logOut"
            onClick={() => {
              roomLeave();
            }}
          ></IoLogOut>
        </RoomParts>

        <div className="dummy"></div>
      </RoomController> 
    </WebcamContainer>
  );
};

const WebcamContainer = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 1.5rem 1.5rem 0 0;
`;

const CamContainer = styled.div`
  display: flex;
  flex-direction: column;
  flex: 1;
  gap: 2rem;
  
  #participants1,
  #participants2 {
    flex: 1;
    display: flex;
    justify-content: center;
    height: 50%;
    gap: 2rem;
  }

  .container {
    position: relative;
    flex: 1;
    max-width: 40%;
  }

  .roomCam {
    position: absolute;
    top: 0;
    left: 0;
    border-radius: 2.5rem;
    box-shadow: rgba(0, 0, 0, 0.15) 0px 2px 8px;
    overflow: hidden;
  }

  .video {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }

  .name {
    position: absolute;
    top: 3%;
    left: 3%;
    padding: 0.5rem;
    background-color: white;
    border-radius: 0.5rem;
  }

  .additional-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate( -50%, -50%);
  display: inline-block; 
  color: #4FAFB1; 
  font-size: 2rem; 
}

  .stopVideo{
    position: absolute;
    width: 100%;
    height: 100%;
    background-color: white;
    border-radius: 2.5rem;
    box-shadow: rgba(0, 0, 0, 0.15) 0px 2px 8px;
  }

  .stopAudio{
    position: absolute;
    width: 100%;
    height: 100%;
    border: 0.3rem solid tomato;
    border-radius: 2.5rem;
  }
`;

const RoomController = styled.div`
  display: flex;
  justify-content: space-between;
  gap: 0.5rem;
  height: 4rem;
  margin-top: 2rem;

  .ControllerIcon {
    cursor: pointer;
  }

  .off {
    color: tomato;
  }

  .logOut {
    font-size: 1.7rem;
  }
`;

export default Webcam;