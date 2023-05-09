package com.codestates.socket.controller;

import com.codestates.room.service.RoomService;
import com.codestates.socket.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StompChatController {

    //특정 메세지를 전달
    private final SimpMessageSendingOperations template;
    private final RoomService roomService;
    /*
     *  1. 방 입장
     *  2. 메시지 전송
     *  3. 방 퇴장(세션만 종료)
     *
     * */

    //Client 가 SEND할 수 있는 경로
    //stompWebSocketConfig 에서 설정한 applicationDestinationPrefixes 와 @MessageMapping 경로가 병합됨
    //"/pub/chat/enter"
    @MessageMapping("/chat/enter")
    public void enterMember(@Payload ChatMessageDto message, SimpMessageHeaderAccessor headerAccessor) {
        //DB, 방 인원수 +1 증가 & 유저추가
        Long roomId = Long.parseLong(message.getRoomId());
        roomService.enterMember(roomId);

        //Session, 새로운 멤버 정보 소켓세션에 저장
        headerAccessor.getSessionAttributes().put("nickname", message.getWriter());
        headerAccessor.getSessionAttributes().put("roomId", message.getRoomId());
        log.info("{} info is saved in session", message.getWriter());

        //새로운 멤버 입장을 Sub 에게 전달
        message.setMessage(message.getMessage() + "님이 채팅방에 입장하였습니다");
        template.convertAndSend("/sub/chat.room/" + message.getRoomId(), message);
    }

    @MessageMapping("/chat/sendMessage")
    public void sendMessage(@Payload ChatMessageDto message) {
        // !왜하는지 모름, 나중에 한번 뺴보자
        message.setMessage(message.getMessage());
        template.convertAndSend("/sub/chat/room" + message.getRoomId(), message);
    }

    @EventListener
    public void webSocketDisconnectListener(SessionDisconnectEvent event) {

        //이벤트 발생을 STOMP 객체로 받음
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        //세션에 있던 내용을 불러옴
        String nickname = (String) headerAccessor.getSessionAttributes().get("nickname");
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");

        log.info("headAccessor {}", headerAccessor);

        //세션 내용을 이용하여 DB 에서 값을 지우지는 않고, 현재 인원 수만 1 감소시킴
        roomService.leaveSession(roomId);

        //세션 변경 내용을 sub에게 전달
        if (nickname != null) {
            log.info("User Disconnected : " + nickname);

            ChatMessageDto chat = ChatMessageDto.builder()
                    .writer(nickname)
                    .message(nickname + " 님 퇴장!!")
                    .build();

            template.convertAndSend("/sub/chat/room/" + roomId, chat);
        }
    }
}
