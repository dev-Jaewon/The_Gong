package com.codestates.socket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {

    private String roomId;
    private String writer;
    private String message;
    private String time;
    private MessageType type;
    public enum MessageType {

        ENTER, LEAVE,
        TALK
    }

    /*
     * image 첨부(option)
     * */
//    private String s3DataUrl;
//    private String fileName;
//    private String fileDir;
}

