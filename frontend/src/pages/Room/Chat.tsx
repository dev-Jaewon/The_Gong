import React, { useEffect, useRef, useState } from 'react';
import { Stomp } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import styled from 'styled-components';
import RoomParts from './RoomParts';
import Verticality from './RoomVerticality';

import { AiFillSetting } from "react-icons/ai"
import { RiMessage2Fill } from "react-icons/ri"
import { HiPencil } from "react-icons/hi"
import { FaUserCircle } from "react-icons/fa"

interface ChatProps {
  roomId: string;
  userName: string;
  edge: number;
  mainColer: string;
}

const Chat: React.FC<ChatProps> = ({ roomId, userName, edge, mainColer }) => {

  // 받아온 메시지를 관리하는 state
  const [ReceivedMessage, setReceivedMessage] = useState([
    { 
      writer: 'Manager',
      message : ` ${userName}님, ${roomId}방에 오신걸 환영합니다.`
    },
  ]);

  // 보낸 메시지를 관리하는 state
  const [sendMessage, setSendMessage] = useState('');

  // 소캣 객체를 저장하는 변수
  const socketRef = useRef<WebSocket | null>(null);

  // 스톰프 객체를 저장하는 변수
  const stompClientRef = useRef<any>(null);

  // 스크롤을 관리하는 변수
  const chatWindowRef = useRef<any>(null);

  let chatParticipants = 0;

  // 렌더링 시 실행되는 코드
  useEffect(() => {
    socketRef.current = new SockJS(`${import.meta.env.VITE_BASE_URL}stomp`);

    //WebSocket 객체를 Stomp.js의 클라이언트 객체로 변환
    stompClientRef.current = Stomp.over(socketRef.current);
  
    // 연결될 때 실행되는 이벤트 핸들러 코드
    stompClientRef.current.connect({}, onConnected);

    // // 연결이 끊어졌을 때 실행되는 코드
    // return () => {
    //   console.log('========== 채팅방 연결 끊어짐 ==========')
    //   stompClientRef.current.send("/pub/chat/leave",
    //     {},
    //     JSON.stringify({
    //       "roomId": roomId,
    //       writer: userName,
    //       messageType: 'LEAVE'
    //     })
    //   );

    //   // 예상하지 못한 오류를 방지하기위해 명시적으로 연결을 끊어주는 코드
    //   stompClientRef.current.disconnect();
    // };
  }, []);


  // 연결이 되었을 때 실행되는 코드
  const onConnected = () => {
    console.log('========== 채팅방 연결됨 ==========')

    // 특정 방에 구독하는 코드
    stompClientRef.current.subscribe('/sub/chat/room/' + roomId, onMessageReceived);

    // 입장 메시지
    stompClientRef.current.send("/pub/chat/enter",
    {},
    JSON.stringify({
        "roomId": roomId,
        writer: userName,
        messageType: 'ENTER'
    })
  )};
   

  // 메시지를 받았을 때 실행되는 코드
  const onMessageReceived = (message:any) => {
    const chat = JSON.parse(message.body);
    console.log('========== 이거 받았다 ==========')
    console.log(chat)

    if (chat.type === 'ENTER') {
      setReceivedMessage((prevState) => [...prevState, chat]);

    } else if (chat.type === 'LEAVE') {
      setReceivedMessage((prevState) => [...prevState, chat]);

    } else if (chat.type === 'TALK') {
      setReceivedMessage((prevState) => [...prevState, chat]);
    }
     else {
       console.log("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
      chatParticipants = chat.length;
      console.log(chat)
    }


    // 스크롤 내리기
    scrollToBottom();
  }


  // 스크롤을 하단으로 내려주는 코드 
  const scrollToBottom = () => {
    //scrollTop: 요소의 스크롤된 세로 위치
    //scrollHeight: 요소의 콘텐츠의 전체 높이
    chatWindowRef.current.scrollTop = chatWindowRef.current.scrollHeight;
  };

  // 메시지 인풋창의 value를 관리하는 코드
  const handleInputChange = (event:any) => {
    setSendMessage(event.target.value);
  };

  // 메시지를 보내는 코드
  const handleSendMessage = () => {
    if(sendMessage){
      stompClientRef.current.send('/pub/chat/message', 
      {}, 
      JSON.stringify({
        "roomId": roomId,
        writer: userName,
        message : sendMessage,
        messageType: 'TALK'
      }));
    }
    console.log('========== 이거 보낸다 ==========')
    console.log(sendMessage)
    setSendMessage('');
  }  

  // 엔터키를 눌렸을 때 실행되는 코드
  const handleKeyDown = (event:any) => {
    // 한글 두번씩 보내지는 버그 방지
    if (event.nativeEvent.isComposing) return;
    if (event.key === 'Enter') {
      handleSendMessage();
    }
  }

  return(
    <Verticality gap={0.5}>
      <RoomParts topRight={edge} flex={1} bgColor={mainColer}>
        {String(chatParticipants)}
      </RoomParts>

      <RoomParts flex={4}>
        <ChatContainer>
          <ChatWindowContainer ref={chatWindowRef}>
            {ReceivedMessage.map((message, idx) => {

              // 내가 보낸 메시지는 오른쪽으로 
              // 이외의 메시지는 왼쪽으로 
              return( 
              <Chats key={idx} className={message.writer === userName ? 'rightChat' : 'leftChat'}>
                <span className='sender'>
                  {/* 한 사람이 여러번 체팅을 보냈을 때 sender의 값을 안보이게  */}
                  {idx !== 0 && ReceivedMessage[idx-1].writer === message.writer ? '' : message.writer}
                </span>
                <span className='content'>{message.message}</span>
              </Chats>
              )
            })}
          </ChatWindowContainer>
          <ChatInputContainer>
            <ChatInput type="text" placeholder='체팅을 입력하세요:)' value={sendMessage} onChange={handleInputChange} onKeyDown={handleKeyDown}></ChatInput>
            <ChatSendButtun onClick={handleSendMessage}>전송</ChatSendButtun>
          </ChatInputContainer>
        </ChatContainer>
      </RoomParts>

      <RoomParts bottomRight={edge} height={4} bgColor={mainColer}>
        <FaUserCircle></FaUserCircle>
        <RiMessage2Fill></RiMessage2Fill>
        <HiPencil></HiPencil>
        <AiFillSetting></AiFillSetting>
      </RoomParts>
    </Verticality>
  )
};


const ChatContainer = styled.div`
  color: rgb(50, 50, 50);
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  background-color: #fff;
  padding: 2rem 0.5rem 0 0.5rem;
`
const ChatWindowContainer = styled.div`
  flex: 8;
  width: 100%;
  border-radius: 0.5rem;
  box-shadow: rgba(0, 0, 0, 0.15) 0px 2px 8px inset;
  background-color: rgb(215, 234, 234);
  padding: 0.5rem;

  display: flex;
  flex-direction: column;
  align-items: end;
  gap: 1rem;
  overflow-y: auto;

  .rightChat{
    align-items: end;
  }

  .leftChat{
    align-items: start;
  }
`

const Chats = styled.div`
  display: flex;
  flex-direction: column;
  width: 100%;
  gap: 0.3rem;

  .sender{
    font-size: 0.5rem;
    border-radius: 0.5rem;
  }
  
  .content{
    max-width: 80%;
    width: fit-content;
    padding: 0.5rem;
    font-size: 0.8rem;
    border-radius: 0.5rem;
    background-color: #fff;
    box-shadow: rgba(0, 0, 0, 0.1) 0px 2px 8px;
    line-height: 1.1rem;
  }
`

const ChatInputContainer = styled.div`
  display: flex;
  gap: 1rem;
  flex: 1;
  width: 100%;
  background-color: #fff;
  padding: 1rem 0rem 1rem 0;
`

const ChatInput = styled.input`
  flex: 1;
  height: 100%;
  border-radius: 0.5rem;
  background-color: #eee;
  border: none;
  box-shadow: rgba(0, 0, 0, 0.05) 0px 2px 8px inset;
  padding: 0.5rem 0.5rem 3rem 0.5rem ;
`
const ChatSendButtun = styled.button`
  display: flex;
  justify-content: center;
  align-items: center;

  width: 3rem;
  height: 100%;
  border-radius: 0.5rem;
  background-color: rgb(215, 234, 234);
  box-shadow: rgba(0, 0, 0, 0.1) 0px 2px 8px;

  font-size: 1rem;
  font-weight: bold;
  color: rgb(75, 75, 75);
  border: none;

`


export default Chat;