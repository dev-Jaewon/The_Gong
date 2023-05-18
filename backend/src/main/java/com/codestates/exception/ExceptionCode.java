package com.codestates.exception;

import lombok.Getter;

public enum ExceptionCode {
    MEMBER_NOT_FOUND(404, "Member not found"),
    MEMBER_EXIST(409, "Member exists"),
    ROOM_NOT_FOUND(404, "Room not found"),
    ROOM_EXIST(409,"Already exists"),
    NEED_PASSWORD(404, "This room needs a password"),
    NO_PASSWORD_REQUIRED(400, "No password required"),
    TAG_NOT_FOUND(404, "Tag not found"),
    PLEASE_VOTE(404, "Please vote"),
    DOUBLE_VOTE(409, "You cannot vote in duplicate"),
    ONLY_ADMIN(404, "Administrator only"),
    PROVIDER_NOT_FOUND(404, "지원하지 않는 OAuth 입니다.");



    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

}
