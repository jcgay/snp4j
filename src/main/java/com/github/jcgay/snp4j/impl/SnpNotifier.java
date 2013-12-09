package com.github.jcgay.snp4j.impl;

import com.github.jcgay.snp4j.Application;
import com.github.jcgay.snp4j.SnpException;
import com.github.jcgay.snp4j.request.Notification;
import com.github.jcgay.snp4j.Notifier;
import com.github.jcgay.snp4j.Server;
import com.github.jcgay.snp4j.impl.request.Action;
import com.github.jcgay.snp4j.impl.request.Request;
import com.github.jcgay.snp4j.impl.response.Response;
import com.github.jcgay.snp4j.response.NotificationResult;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class SnpNotifier implements Notifier {

    private final Application application;
    private final SnpSocket socket;

    @Override
    public NotificationResult send(@NonNull Notification notification) {

        // controle de la notification

        UUID uuid = UUID.randomUUID();
        Action notify = Action.name("notify")
                .withParameter("id", notification.getClassId())
                .withParameter("title", notification.getTitle())
                .withParameter("text", notification.getText())
                .withParameter("timeout", notification.getTimeout())
                .withParameter("icon", notification.getIcon())
                .withParameter("icon-base64", notification.getIconBase64())
                .withParameter("sound", notification.getSound())
                .withParameter("priority", notification.getPriority())
                .withParameter("uuid", uuid)
                .build();

        Request request = Request.builder(application)
                .addAction(notify)
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
}
