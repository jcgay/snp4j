package com.github.jcgay.snp4j.impl;

import com.github.jcgay.snp4j.Server;
import com.github.jcgay.snp4j.SnpException;
import com.github.jcgay.snp4j.SnpSocket;
import com.github.jcgay.snp4j.request.Request;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SnpSocketImpl implements SnpSocket {

    @NonNull
    private final PrintWriter out;
    @NonNull
    private final BufferedReader in;
    @NonNull
    private final Socket socket;
    @NonNull
    private final RequestSerializer serializer;

    public static SnpSocketImpl of(@NonNull Server server) {
        return of(server, new RequestSerializer());
    }

    private static SnpSocketImpl of(@NonNull Server server, @NonNull RequestSerializer serializer) {
        try {
            Socket socket = new Socket(server.getHost(), server.getPort());
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            return new SnpSocketImpl(writer, reader, socket, serializer);
        } catch (IOException e) {
            throw new SnpException("Cannot create notifier, an error occurs while creating the socket.", e);
        }
    }

    @Override
    public boolean send(@NonNull Request request) {
        String toSend = serializer.stringify(request);
        out.print(toSend);
        out.flush();

        String s;
        try {
            while ((s = in.readLine()) != null) {
                System.out.println(s);
                if ("END".equals(s)) {
                    break;
                }
            }
        } catch (IOException e) {
            throw new SnpException("Error while reading response.", e);
        }

        return true;
    }

    @Override
    public void close() throws IOException {
        out.close();
        in.close();
        socket.close();
    }
}
