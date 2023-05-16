package com.codestates.rtc.config;

import java.io.IOException;

import com.codestates.rtc.KurentoRoom;
import com.codestates.rtc.KurentoRoomManager;
import com.codestates.rtc.KurentoUserRegistry;
import org.kurento.client.IceCandidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 *
 * @author Ivan Gracia (izanmail@gmail.com)
 * @since 4.3.1
 */
public class KurentoHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(KurentoHandler.class);

    private static final Gson gson = new GsonBuilder().create();

    @Autowired
    private KurentoRoomManager kurentoRoomManager;

    @Autowired
    private KurentoUserRegistry registry;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        final JsonObject jsonMessage = gson.fromJson(message.getPayload(), JsonObject.class);

        final KurentoUserSession user = registry.getBySession(session);

        if (user != null) {
            log.debug("Incoming message from user '{}': {}", user.getName(), jsonMessage);
        } else {
            log.debug("Incoming message from new user: {}", jsonMessage);
        }

        switch (jsonMessage.get("id").getAsString()) {
            case "joinRoom":
                joinRoom(jsonMessage, session);
                break;
            case "receiveVideoFrom":
                final String senderName = jsonMessage.get("sender").getAsString();
                final KurentoUserSession sender = registry.getByName(senderName);
                final String sdpOffer = jsonMessage.get("sdpOffer").getAsString();
                user.receiveVideoFrom(sender, sdpOffer);
                break;
            case "leaveRoom":
                leaveRoom(user);
                break;
            case "onIceCandidate":
                JsonObject candidate = jsonMessage.get("candidate").getAsJsonObject();

                if (user != null) {
                    IceCandidate cand = new IceCandidate(candidate.get("candidate").getAsString(),
                            candidate.get("sdpMid").getAsString(), candidate.get("sdpMLineIndex").getAsInt());
                    user.addCandidate(cand, jsonMessage.get("name").getAsString());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        KurentoUserSession user = registry.removeBySession(session);
        kurentoRoomManager.getRoom(user.getRoomName()).leave(user);
    }

    private void joinRoom(JsonObject params, WebSocketSession session) throws IOException {
        final String roomName = params.get("room").getAsString();
        final String name = params.get("name").getAsString();
        log.info("PARTICIPANT {}: trying to join room {}", name, roomName);

        KurentoRoom kurentoRoom = kurentoRoomManager.getRoom(roomName);
        final KurentoUserSession user = kurentoRoom.join(name, session);
        registry.register(user);
    }

    private void leaveRoom(KurentoUserSession user) throws IOException {
        final KurentoRoom kurentoRoom = kurentoRoomManager.getRoom(user.getRoomName());
        kurentoRoom.leave(user);
        if (kurentoRoom.getParticipants().isEmpty()) {
            kurentoRoomManager.removeRoom(kurentoRoom);
        }
    }
}
