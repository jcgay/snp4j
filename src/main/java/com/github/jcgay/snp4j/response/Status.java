package com.github.jcgay.snp4j.response;

import lombok.NonNull;

public enum Status {

    OK(0),
    FAILED(101),
    UNKNOWN_COMMAND(102),
    TIMED_OUT(103),
    BAD_SOCKET(106),
    BAD_PACKET(107),
    INVALID_ARG(108),
    ARG_MISSING(109),
    SYSTEM(110),
    ACCESS_DENIED(121),
    NOT_RUNNING(201),
    NOT_REGISTERED(202),
    ALREADY_REGISTERED(203),
    CLASS_ALREADY_EXISTS(204),
    CLASS_BLOCKED(205),
    CLASS_NOT_FOUND(206),
    NOTIFICATION_NOT_FOUND(207),
    FLOODING(208),
    DO_NOT_DISTURB(209),
    COULD_NOT_DISPLAY(210),
    AUTH_FAILURE(211),
    DISCARDED(212),
    NOT_SUBSCRIBED(213),
    GONE(301),
    CLICK(302),
    EXPIRED(303),
    INVOKED(304),
    MENU(305),
    EX_CLICK(306),
    CLOSED(307),
    ACTION(308),
    UNKNOWN(-1);

    private int code;

    private Status(int i) {
        code = i;
    }

    public static Status fromCode(@NonNull String code) {
        Integer statusCode = Integer.valueOf(code);
        for (Status status : Status.values()) {
            if (status.code == statusCode) {
                return status;
            }
        }
        return Status.UNKNOWN;
    }
}
