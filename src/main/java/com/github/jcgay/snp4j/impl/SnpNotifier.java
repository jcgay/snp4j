package com.github.jcgay.snp4j.impl;

import com.github.jcgay.snp4j.Application;
import com.github.jcgay.snp4j.Notification;
import com.github.jcgay.snp4j.Notifier;
import com.github.jcgay.snp4j.Server;
import com.github.jcgay.snp4j.SnpSocket;
import com.github.jcgay.snp4j.impl.request.Action;
import com.github.jcgay.snp4j.impl.request.Request;
import com.github.jcgay.snp4j.impl.response.Response;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class SnpNotifier implements Notifier {

    private final Application application;
    private final SnpSocket socket;

    @Override
    public boolean send(@NonNull Notification notification) {

        // controle de la notification

        Action notify = Action.name("notify")
                .withParameter("id", notification.getClassId())
                .withParameter("title", notification.getTitle())
                .withParameter("text", notification.getText())
                .withParameter("timeout", notification.getTimeout())
                .withParameter("icon", notification.getIcon())
                .withParameter("icon-base64", notification.getIconBase64())
                .withParameter("sound", notification.getSound())
                .withParameter("priority", notification.getPriority())
                .build();

        Request request = Request.builder(application)
                .addAction(notify)
                .build();

        Response send = socket.send(request);
        return true;
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
