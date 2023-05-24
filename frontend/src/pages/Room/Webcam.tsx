import styled from 'styled-components';
import { useRef, useState } from 'react';
import { useEffect } from 'react';
import SockJS from 'sockjs-client';
import RoomParts from './RoomParts';
import * as kurentoUtils from 'kurento-utils';
import { useNavigate } from 'react-router-dom';

import { MdScreenShare } from 'react-icons/md';
import { BsCameraVideoOffFill } from 'react-icons/bs';
import { BsFillMicFill } from 'react-icons/bs';
import { BsFillMicMuteFill } from 'react-icons/bs';
import { BsFillCameraVideoFill } from 'react-icons/bs';
import { IoLogOut } from 'react-icons/io5';

interface WebcamProps {
  roomId: string;
  userName: string;
  edge: number;
  mainColer: string;
}

const Webcam: React.FC<WebcamProps> = ({
  roomId,
  userName,
  edge,
  mainColer,
}) => {
  let participants = useRef<{ [key: string]: any }>({});
  let view: boolean = true;
  let rtcSocket = useRef<WebSocket | null>(null);
  const navigate = useNavigate();

  // 처음 렌더링 될 때 실행되는 코드
  // 언마운트 될 때 실행되는 클린 업 함수
  useEffect(() => {
    rtcSocket.current = new SockJS(
      `${import.meta.env.VITE_BASE_URL}groupcall`
    );

    // 연결될 때 실행되는 이벤트 핸들러 코드
    rtcSocket.current.onopen = () => {
      console.log('========== 화상채팅 연결됨 ==========');

      // 1 - 입장
      var message = {
        id: 'joinRoom',
        name: userName,
        room: roomId,
      };

      if (rtcSocket.current && rtcSocket.current.readyState === SockJS.OPEN) {
        sendMessage(message);
      }

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

    
  function sendMessage(message: any) {
    if (rtcSocket.current && rtcSocket.current.readyState === SockJS.OPEN) {
      const jsonMessage = JSON.stringify(message);

      console.log('========== 이거 보낸다 ==========');
      console.log(jsonMessage);

      rtcSocket.current.send(jsonMessage);
    } else {
      console.error('Connection is not established yet.');
    }
  }

  // 컴포넌트가 렌더링 될 때 마다 실행되는 코드
  useEffect(() => {
    // 서버로부터 응답 메시지를 받았을 때 실행되는 코드
    if (rtcSocket.current !== null) {
      rtcSocket.current.onmessage = (message) => {
        var parsedMessage = JSON.parse(message.data);

        console.log('========== 이거 받았다 ==========');
        console.log(parsedMessage);

        switch (parsedMessage.id) {
          case 'existingParticipants':
            
            // 2 - 현재 참여자들
            console.log('========== existingParticipants ==========');
            onExistingParticipants(parsedMessage);
            break;
            
          case 'newParticipantArrived':
            console.log('========== newParticipantArrived ==========');
            onNewParticipant(parsedMessage);
            break;

          case 'participantLeft':
            onParticipantLeft(parsedMessage);
            break;

          case 'receiveVideoAnswer':
            console.log('========== receiveVideoAnswer ==========');
            receiveVideoResponse(parsedMessage);
            break;

          case 'iceCandidate':
            console.log('========== iceCandidate ==========');      
            participants.current[parsedMessage.name].rtcPeer.addIceCandidate(
              parsedMessage.candidate,
              function (error: Error) {
                if (error) {
                  console.error('아이스캔디데이트 애러 ' + error);
                  return;
                }
              }
            );

 
            break;

          default:
            console.error('알 수 없는 메시지 : ', parsedMessage);
        }
      };
    }
  });

  //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

  // 내 미디어에 대한 오퍼를 보내는 함수
  function onExistingParticipants(msg: any) {
    
    // 나의 미디어 스트림 정보
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

    // 내 이름으로 참가자 객체 생성
    var participant = new Participant(userName);

    // 참가자들 객체에 내 이름으로 참가자 객체 추가
    participants.current[userName] = participant;

    // 설정해 둔 비디오 요소를 저장
    var video = participant.getVideoElement();

    // options 객체는 WebRTC 피어의 생성 옵션을 설정한다.
    // localVideo는 로컬 비디오 요소를 지정한다.
    // mediaConstraints 미디어 스트림의 제약 조건을 설정한다.
    // onicecandidate 콜백 함수를 지정하여 ICE candidate 이벤트를 처리한다.
    var options = {
      localVideo: video,
      mediaConstraints: constraints,
          
      // onicecandidate는 WebRTC에서 ICE 후보자 이벤트를 처리하기 위한 콜백 함수입니다. 
      // ICE 후보자는 피어 간의 네트워크 연결을 수립하기 위해 사용되는 네트워크 정보입니다.
      onicecandidate: participant.onIceCandidate.bind(participant),
    };

    // participant.rtcPeer에 참가자의 피어 객체를 생성하는 작업
    participant.rtcPeer = new (kurentoUtils.WebRtcPeer

      // WebRtcPeerSendonly는 WebRTC 피어 객체를 생성한다.
      // participant.rtcPeer에 WebRTC 피어 객체가 생성되면, 
      // 해당 피어를 사용하여 Offer SDP를 생성하고 상대 피어에게 전송할 수 있습니다.

      // WebRtcPeerSendonly는 다른 피어(예: WebRtcPeerRecvonly, 와 연결된 
      // WebRTC 세션에서 송신 역할을 담당합니다. 
      // 즉, 오디오 및 비디오 스트림을 상대방에게 전송할 수 있습니다.
      .WebRtcPeerSendonly as any)

      //options 객체는 WebRTC 피어의 생성 옵션을 설정한다.
      (options, (error:Error) => {
      if (error) {
        return console.error(error);
      }
    });

    // generateOffer()는 Offer WebRTC 피어 객체를 사용해서 SDP를 생성한다.
    participant.rtcPeer.generateOffer(
      // offerToReceiveVideo() 메서드는 생성된 Offer SDP를 
      // 상대 피어에게 전송하는 등의 작업을 처리할 수 있습니다.
      participant.offerToReceiveVideo.bind(participant)
    );

    // 참가자들이 담겨있는 data 배열에 있는 각 항목에 대해 
    // receiveVideo 함수를 실행하는 코드.
    msg.data.forEach(receiveVideo);

  }


  // 4 - Answere를 만드는 함수 
  function receiveVideo(sender: any) {

    var participant = new Participant(sender);
    participants.current[sender] = participant;
    console.log(participants.current[sender])

    var video = participant.getVideoElement();

    var options = {
      remoteVideo: video,
      onicecandidate: participant.onIceCandidate.bind(participant),
    }


    // WebRtcPeerRecvonly는수신 전용(peerReceive) 역할을 나타내는 클래스이다. 
    // 이 클래스를 사용하여 WebRTC 피어 객체를 생성할 수 있다.
    // WebRtcPeerRecvonly는 다른 피어(예: WebRtcPeerSendonly)와 연결된 WebRTC 세션에서 
    // 수신 역할을 담당합니다. 즉, 상대방으로부터 오디오 및 비디오 스트림을 수신할 수 있습니다. 
    // 따라서, WebRtcPeerRecvonly를 사용하여 수신 역할을 할 수 있는 WebRTC 피어 객체를 생성하고, 
    // 상대방에게 Offer SDP를 전송하여 비디오 및 오디오 스트림을 수신할 수 있습니다.
    participant.rtcPeer = new (kurentoUtils.WebRtcPeer.WebRtcPeerRecvonly as any)
    (options, (error:Error) => {
      if (error) {
        return console.error(error);
      }
    });
    

    // generateOffer() 메서드는 해당 WebRTC 피어 객체를 사용하여 
    // Answer SDP를 생성합니다. Answer SDP는 Offer SDP에 대한 응답으로 생성되며, 
    // 피어 간의 연결 설정에 필요한 정보를 포함합니다.
    participant.rtcPeer.generateOffer(
      participant.offerToReceiveVideo.bind(participant)
    );


  }

  
  function onParticipantLeft(request:any) {
    console.log('Participant ' + request.name + ' left');
    var participant = participants.current[request.name];
    participant.dispose();
    delete participants.current[request.name];
  }


  function onNewParticipant(request: any) {
    receiveVideo(request.name);
  }


  // 6 - 내가 보낸 오퍼에 대한 Answer SDP를 처리하는 작업입니다.
  function receiveVideoResponse(result: any) {
    // 참가자 목록에 Answer SDP를 보낸 참가자의 rtcPeer에 접근
    // processAnswer() 메서드는 Answer SDP를 처리하여 연결을 설정하는 역할을 합니다
    // 첫 번째 인자로는 수신한 Answer SDP(result.sdpAnswer)가 전달되며
    // 두 번째 인자는 처리 과정에서 발생하는 오류를 처리하는 콜백 함수입니다
    // Answer SDP를 수신한 참가자의 WebRTC 피어 객체에 전달되어 연결 설정이 이루어집니다.

    // 따라서, 해당 코드는 이제윤이 보낸 Answer SDP를 
    // 이제윤의 참가자 객체의 WebRTC 피어 객체에 전달하여 연결을 설정하는 역할을 합니다.
    participants.current[result.name].rtcPeer.processAnswer(
      result.sdpAnswer,
      function (error: Error) {
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


  //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

  class Participant {
    name: any;
    container: any;
    span: any;
    video: any;
    rtcPeer: any;

    constructor(name: string) {
      this.name = name;
      this.container = document.createElement('div');
      this.container.id = name;
      this.container.classList.add('container');
      this.span = document.createElement('span');
      this.video = document.createElement('video');
      this.span.classList.add('name');
      this.video.classList.add('video');
      this.video.classList.add('roomCam');
      this.rtcPeer = null;

      this.container.appendChild(this.video);
      this.container.appendChild(this.span);

      if (view) {
        const participants1 = document.getElementById('participants1');
        if (participants1) {
          participants1.appendChild(this.container);
        }
        view = !view;
      } else {
        const participants2 = document.getElementById('participants2');
        if (participants2) {
          participants2.appendChild(this.container);
        }
        view = !view;
      }

      this.span.appendChild(document.createTextNode(name));

      this.video.id = 'video-' + name;
      this.video.autoplay = true;
      this.video.controls = false;
    }

    getElement(): any {
      return this.container;
    }

    getVideoElement(): any {
      return this.video;
    }

    // generateOffer에서 생성된 sdp를 인자로 전달받고
    // 상대 피어에게 sdp를 전송하는 코드
    // 상대 피어는 전달받은 Offer SDP를 기반으로 Answer SDP를 생성하고 다시 응답한다.
    offerToReceiveVideo(error: any, offerSdp: any, wp: any) {
      if (error) return console.error('sdp offer error');

      console.log('========== offer ==========');
      console.log(this.name)
      console.log(offerSdp);

      // 3 - 오퍼를 보낸다.
      const msg = {
        id: 'receiveVideoFrom',
        sender: this.name,
        sdpOffer: offerSdp,
      };

      sendMessage(msg);
    }

    // SDP 교환 후에는 피어가 생성한 ICE candidate를 상대 피어에게 전송하여 
    // 서로의 네트워크 정보를 교환하게 됩니다. 
    // 이를 통해 피어 간의 연결이 수립되고 미디어 스트림의 송수신이 가능해집니다.
    onIceCandidate(candidate: any, wp: any) {
      // console.log("Local candidate" + JSON.stringify(candidate));
      
      

      console.log('========== candidate ==========');
      console.log(this.name)
      const message = {
        id: 'onIceCandidate',
        candidate: candidate,
        name: this.name,
      };
      sendMessage(message);
    }
  }

  //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

  const [isCameraOn, setCameraOn] = useState(true);
  const [isMicOn, setMicOn] = useState(true);

  const videoOn = () => {
    setCameraOn((prev) => !prev);

    console.log(participants.current[userName].rtcPeer );
    console.log(participants.current['판다류'].rtcPeer );

    participants.current[userName].rtcPeer.videoEnabled =
      !participants.current[userName].rtcPeer.videoEnabled;
  };

  const audioOn = () => {
    setMicOn((prev) => !prev);
    participants.current[userName].rtcPeer.audioEnabled =
      !participants.current[userName].rtcPeer.audioEnabled;
  };

  const roomLeave = () => {
    if (rtcSocket.current && rtcSocket.current.readyState === SockJS.OPEN) {
      navigate(`/`);
      rtcSocket.current.close();

      sendMessage({
        id: 'leaveRoom',
      });

      // for ( var key in participants) {
      //   participants.current[key].dispose();
      // }

      console.log('========== 화상채팅 연결 종료 ==========');
    }
  };

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
          bgColor={mainColer}
        >
          The
        </RoomParts>

        <RoomParts width={20} color={mainColer}>
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

export default Webcam;
