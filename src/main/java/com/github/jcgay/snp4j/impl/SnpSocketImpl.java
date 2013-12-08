package com.github.jcgay.snp4j.impl;

import com.github.jcgay.snp4j.Server;
import com.github.jcgay.snp4j.SnpException;
import com.github.jcgay.snp4j.SnpSocket;
import com.github.jcgay.snp4j.request.Request;
import com.github.jcgay.snp4j.response.Response;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
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
        try {
            Socket socket = new Socket(server.getHost(), server.getPort());
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            return new SnpSocketImpl(writer, reader, socket, new RequestSerializer());
        } catch (IOException e) {
            throw new SnpException("Cannot create notifier, an error occurs while creating the socket.", e);
        }
    }

    @Override
    public Response send(@NonNull Request request) {
        out.print(serializer.stringify(request));
        out.flush();
        return readResponse();
    }

    private Response readResponse() {
        Map<String, String> responseElements = new HashMap<String, String>();
        String s;
        try {
            while ((s = in.readLine()) != null) {
                System.out.println(s);
                if ("END".equals(s)) {
                    break;
                }
                String[] split = s.split(" ", 2);
                responseElements.put(split[0], split[1]);
            }
            return Response.builder(responseElements).build();
        } catch (IOException e) {
            throw new SnpException("Error while reading response.", e);
        }
    }

    @Override
    public void close() throws IOException {
        out.close();
        in.close();
        socket.close();
    }
}
