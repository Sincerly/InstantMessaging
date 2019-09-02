package com.ysxsoft.imtalk.chatroom.constant;

public enum ErrorCode {
    NETWORK_ERROR(0, "network error"),
    RESULT_FAILED(1, "request result is not success"),
    RESULT_ERROR(2, "request result's content is error"),
    IM_ERROR(3, "IM has some error"),
    RTC_ERROR(4, "RTC has some error"),
    NO_PERMISSION(5, "no permission"),
    ROOM_NOT_JOIN_TO_ROOM(100, "not join to room"),
    ROOM_SEND_MSG_ERROR(101, "send ChatRoom msg error"),
    ROOM_CREATE_ROOM_OVER_LIMIT(102, "create room over limit"),
    ROOM_JOIN_MEMBER_OVER_LIMIT(103,"room member over limit"),
    MIC_POSITION_HAS_BEEN_HOLD(110, "mic position has been hold"),
    MIC_USER_ALREADY_ON_OTHER_POSITION(111, "user already on other position"),
    RTC_JOIN_ROOM_ERROR(400, "join RTC room error"),
    UNKNOWN_ERROR(-999, "unknown error");

    private int code;

    public String getMsg() {
        return msg;
    }

    private String msg;

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }



    public static ErrorCode fromCode(int code) {
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.code == code)
                return errorCode;
        }

        return UNKNOWN_ERROR;
    }

}
