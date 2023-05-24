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


const Test1 = ({room, name, edge, mainColor}) => {
  let participants = useRef({});
  let view = true;
  const rtcSocket = useRef(null);
  const navigate = useNavigate();

  useEffect(() => {
    rtcSocket.current = new SockJS(`${import.meta.env.VITE_BASE_URL}groupcall`);

    rtcSocket.current.onopen = () => {
      console.log('연결됨');

      var message = {
        id: 'joinRoom',
        name: name,
        room: room,
      };

      // if (rtcSocket.current && rtcSocket.current.readyState === SockJS.OPEN) {
      //   sendMessage(message);
      // }

      sendMessage(message);
      
    };

    // 연결이 끊어졌을 때 실행되는 코드
    return () => {
        // 예상하지 못한 오류를 방지하기위해 명시적으로 연결을 끊어주는 코드
        rtcSocket.current.close();
        console.log('========== 화상채팅 연결 종료 ==========');
    };
  }, []);

  window.onbeforeunload = function() {
     rtcSocket.current.close();
  };

  // 컴포넌트가 렌더링 될 때 마다 실행되는 코드
  useEffect(() => {
    
    rtcSocket.current.onmessage = (message) => {
      var parsedMessage = JSON.parse(message.data);

      console.log('========== 이거 받았다 ==========');
      console.log(parsedMessage);

      switch (parsedMessage.id) {
        case 'existingParticipants':
          onExistingParticipants(parsedMessage);
          break;

        case 'newParticipantArrived':
          onNewParticipant(parsedMessage);
          break;

        case 'participantLeft':
          onParticipantLeft(parsedMessage);;
          break;

        case 'receiveVideoAnswer':
          receiveVideoResponse(parsedMessage);
          break;

        case 'iceCandidate':
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

  //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@


function onParticipantLeft(request) {  
  const element = document.getElementById(request.name);
  element.parentNode.removeChild(element);
	delete participants.current[request.name];
}

  function onNewParticipant(request) {
    receiveVideo(request.name);
  }


  function receiveVideoResponse(result) {
    participants.current[result.name].rtcPeer.processAnswer(
      result.sdpAnswer,
      function (error) {
        if (error) return console.error(error);
      }
    );
  }


  function sendMessage(message) {
    if (rtcSocket.current && rtcSocket.current.readyState === SockJS.OPEN) {
      const jsonMessage = JSON.stringify(message);
      rtcSocket.current.send(jsonMessage);
    } else {
      console.error('Connection is not established yet.');
    }
  }


  // function callResponse(message) {
  //   if (message.response != 'accepted') {
  //     // console.info("Call not accepted by peer. Closing call");
  //     // stop();
  //   } else {
  //     kurentoUtils.processAnswer(message.sdpAnswer, function (error) {
  //       if (error) return console.error(error);
  //     });
  //   }
  // }


  function onExistingParticipants(msg) {
    let constraints = {
      audio: true,
      video: {
        mandatory: {
          maxWidth: 2000,
          maxFrameRate: 15,
          minFrameRate: 15,
        },
      },
    };


    let participant = new Participant(name);
    participants.current[name] = participant;
    let video = participant.getVideoElement();

    let options = {
      localVideo: video,
      mediaConstraints: constraints,
      onicecandidate: participant.onIceCandidate.bind(participant),
    };
    participant.rtcPeer = new kurentoUtils.WebRtcPeer.WebRtcPeerSendonly(
      options,
      function (error) {
        if (error) {
          return console.error(error);
        }
        this.generateOffer(participant.offerToReceiveVideo.bind(participant));
      }
    );

    msg.data.forEach(receiveVideo);
  }


  function receiveVideo(sender) {
    let participant = new Participant(sender);
    participants.current[sender] = participant;
    let video = participant.getVideoElement();

    let options = {
      remoteVideo: video,
      onicecandidate: participant.onIceCandidate.bind(participant),
    };

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


  //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@


  function Participant(name) {
    this.name = name;
    let container = document.createElement('div');
    container.id = name;
    container.classList.add('container');
    let span = document.createElement('span');
    let video = document.createElement('video');
    span.classList.add('name');
    video.classList.add('video');
    video.classList.add('roomCam');
    let rtcPeer;

    container.appendChild(video);
    container.appendChild(span);

    if (view) {
      document.getElementById('participants1').appendChild(container);
      view = !view;
    } else {
      document.getElementById('participants2').appendChild(container);
      view = !view;
    }

    span.appendChild(document.createTextNode(name));

    video.id = 'video-' + name;
    video.autoplay = true;
    video.controls = false;

    this.getElement = function () {
      return container;
    };

    this.getVideoElement = function () {
      return video;
    };

    this.offerToReceiveVideo = function (error, offerSdp, wp) {
      if (error) return console.error('sdp offer error');
      // console.log("Invoking SDP offer callback function");

      let msg = {
        id: 'receiveVideoFrom',
        sender: name,
        sdpOffer: offerSdp,
      };

      sendMessage(msg);
    };

    this.onIceCandidate = function (candidate, wp) {
      // console.log("Local candidate" + JSON.stringify(candidate));

      let message = {
        id: 'onIceCandidate',
        candidate: candidate,
        name: name,
      };
      sendMessage(message);
    };

    Object.defineProperty(this, 'rtcPeer', { writable: true });
  }

  
 //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@


 const [isCameraOn, setCameraOn] = useState(true);
 const [isMicOn, setMicOn] = useState(true);

 const videoOn = () => {
   setCameraOn((prev) => !prev);
   console.log(participants.current)
   participants.current[name].rtcPeer.videoEnabled =
     !participants.current[name].rtcPeer.videoEnabled;
 };

 const audioOn = () => {
   setMicOn((prev) => !prev);
   participants.current[name].rtcPeer.audioEnabled =
     !participants.current[name].rtcPeer.audioEnabled;
 };

 const roomLeave = () => {

    sendMessage({
      id: 'leaveRoom',
    });

    rtcSocket.current.close();

    navigate(`/`);
    // for ( var key in participants) {
    //   participants.current[key].dispose();
    // }

    console.log('========== 화상채팅 연결 종료 ==========');
  
 };

  //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  return (
    <WebcamContainer>
      <RoomCamContainer>
        <div id="participants1"></div>
        <div id="participants2"></div>
      </RoomCamContainer>

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
    max-height: 375px;
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

export default Test1;