package com.codestates.exception;

import lombok.Getter;

public enum ExceptionCode {
    MEMBER_NOT_FOUND(404, "Member not found"),
    MEMBER_EXIST(409, "Member exists"),

    ROOM_NOT_FOUND(404, "Room not found"),
    ROOM_EXIST(409,"Already exists"),
    NEED_PASSWORD(404, "This room needs a password"),

    PLEASE_VOTE(404, "Please vote"),
    DOUBLE_VOTE(409, "You cannot vote in duplicate"),

    ONLY_ADMIN(404, "Administrator only");


    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

}
