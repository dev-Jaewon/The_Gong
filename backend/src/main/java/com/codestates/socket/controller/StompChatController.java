package com.codestates.socket.controller;

import com.codestates.room.service.RoomService;
import com.codestates.socket.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StompChatController {

    //특정 메세지를 전달
    private final SimpMessageSendingOperations template;

    private final RoomService roomService;

    @MessageMapping("/chat/enter")
    public void enterMember(@Payload ChatMessageDto message, SimpMessageHeaderAccessor headerAccessor) {
        //DB, 방 인원수 증가 & 유저추가
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





}
