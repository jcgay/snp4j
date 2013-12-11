package com.github.jcgay.snp4j.impl;

import com.github.jcgay.snp4j.Application;
import com.github.jcgay.snp4j.Notifier;
import com.github.jcgay.snp4j.Server;
import com.github.jcgay.snp4j.SnpException;
import com.github.jcgay.snp4j.impl.request.Action;
import com.github.jcgay.snp4j.impl.request.Request;
import com.github.jcgay.snp4j.impl.response.Response;
import com.github.jcgay.snp4j.request.Notification;
import com.github.jcgay.snp4j.response.NotificationResult;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class SnpNotifier implements Notifier {

    private final Application application;
    private final SnpSocket socket;

    @Override
    public NotificationResult send(@NonNull Notification notification) {

        if (notification.getIcon() == null && notification.getText() == null && notification.getTitle() == null) {
            throw new IllegalArgumentException("At least one of <title>, <text> or <icon> must be supplied for the command to succeed.");
        }

        UUID uuid = UUID.randomUUID();
        Action.Builder builder = Action.name("notify")
                .withParameter("id", notification.getClassId())
                .withParameter("title", notification.getTitle())
                .withParameter("text", notification.getText())
                .withParameter("timeout", notification.getTimeout())
                .withParameter("sound", notification.getSound())
                .withParameter("priority", notification.getPriority())
                .withParameter("uuid", uuid);
        if (notification.getIcon() != null) {
            builder
                    .withParameter(notification.getIcon().isBase64() ? "icon-base64" : "icon", notification.getIcon());
        }

        Request request = Request.builder(application)
                .addAction(builder.build())
                .build();

        Response response = socket.send(request);
        if (response.hasNotSucceeded()) {
            throw new SnpException(response.getError());
        }

        return new NotificationResult(uuid);
    }

    public static SnpNotifier of(@NonNull Application application, @NonNull Server server) {
        SnpSocketImpl socket = SnpSocketImpl.of(server);

        Action register = Action.name("register")
                .withParameter("app-sig", application.getAppSig())
                .withParameter("uid", UUID.randomUUID())
                .withParameter("title", application.getTitle())
                .build();

        Request request = Request.builder(application)
                .addAction(register)
                .build();

        socket.send(request);

        return new SnpNotifier(application, socket);
    }

    @Override
    public void close() throws IOException {

        Request request = Request.builder(application)
                .addAction(Action.name("unregister").build())
                .build();

        socket.send(request);

        socket.close();
    }
}
