package com.codestates.rtc.config;

import com.codestates.rtc.KurentoRoomManager;
import com.codestates.rtc.KurentoUserRegistry;
import org.kurento.client.KurentoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;


@Configuration
@EnableWebSocket
public class WebRtcConfig implements WebSocketConfigurer {
    @Bean
    public KurentoUserRegistry registry() {
        return new KurentoUserRegistry();
    }

    @Bean
    public KurentoRoomManager roomManager() {
        return new KurentoRoomManager();
    }
    @Bean
    public KurentoHandler kurentoHandler() {
        return new KurentoHandler();
    }

    @Bean
    public KurentoClient kurentoClient() {
        return KurentoClient.create("ws://3.39.250.63:8888/kurento");
    }

    @Bean
    public ServletServerContainerFactoryBean createServletServerContainerFactoryBean() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(32768);
        return container;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(kurentoHandler(), "/groupcall")
                .setAllowedOrigins("http://localhost:3000")
                .withSockJS();
    }
}
