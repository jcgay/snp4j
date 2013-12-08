package com.github.jcgay.snp4j.impl.response;

import lombok.Getter;
import lombok.NonNull;

public enum Status {

    OK(0),
    FAILED(101, "General failure of some sort"),
    UNKNOWN_COMMAND(102, "The command was not recognised"),
    TIMED_OUT(103, "Snarl took too long to respond"),
    BAD_SOCKET(106, "The communication socket was closed unexpectedly"),
    BAD_PACKET(107, "Badly formed SNP request"),
    INVALID_ARG(108, "An invalid parameter was provided"),
    ARG_MISSING(109, "A required argument was missing"),
    SYSTEM(110, "Internal system error"),
    ACCESS_DENIED(121, "The command was not allowed"),
    NOT_RUNNING(201, "Snarl isn't running on the local (or, in the case of a network request, remote) computer"),
    NOT_REGISTERED(202, "An attempt was made to create a notification before the application was registered"),
    ALREADY_REGISTERED(203, "The application is already registered"),
    CLASS_ALREADY_EXISTS(204, "The class specified is already registered"),
    CLASS_BLOCKED(205, "The user has disabled notifications from this class"),
    CLASS_NOT_FOUND(206, "The specified class was not found"),
    NOTIFICATION_NOT_FOUND(207, "The specified notification was not found"),
    FLOODING(208, "The notification was not displayed as it would cause flooding of the display"),
    DO_NOT_DISTURB(209, "The user has enabled Do Not Disturb mode"),
    COULD_NOT_DISPLAY(210, "No enough screen space exists to display the notification"),
    AUTH_FAILURE(211, "Password mismatch"),
    DISCARDED(212, "The notification was discarded, usually because the sending application was in the foreground"),
    NOT_SUBSCRIBED(213, "Subscriber does not exist"),
    GONE(301),
    CLICK(302, "notification was right-clicked"),
    EXPIRED(303, "Notification timed out"),
    INVOKED(304, "Notification was clicked by the user"),
    MENU(305, "item was selected from the notification's menu"),
    EX_CLICK(306, "user clicked the middle mouse button on the notification"),
    CLOSED(307, "User clicked the notification's Close gadget"),
    ACTION(308, "User selected an action from the notification's Actions menu"),
    UNKNOWN(-1);

    private int code;
    @Getter
    private String hint;

    private Status(int code, String hint) {
        this.code = code;
        this.hint = hint;
    }

    private Status(int code) {
        this(code, "");
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
