package fr.jcgay.snp4j.impl;

import fr.jcgay.snp4j.Server;
import fr.jcgay.snp4j.SnpException;
import fr.jcgay.snp4j.impl.request.Request;
import fr.jcgay.snp4j.impl.response.Response;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

@Slf4j
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
            Socket socket = new Socket();
            InetSocketAddress destination = new InetSocketAddress(server.getHost(), server.getPort());
            socket.connect(destination, (int) server.getTimeout());
            socket.setSoTimeout((int) server.getTimeout());
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            return new SnpSocketImpl(writer, reader, socket, new RequestSerializer());
        } catch (IOException e) {
            throw new SnpException("Cannot create notifier, an error occurs while creating the socket.", e);
        }
    }

    @Override
    public Response send(@NonNull Request request) {
        String toSend = serializer.stringify(request);
        log.debug("Sending request: \n{}", toSend);
        out.print(toSend);
        out.flush();
        return readResponse();
    }

    private Response readResponse() {
        Map<String, String> responseElements = new HashMap<String, String>();
        log.debug("Reading response:");
        String s;
        try {
            while ((s = in.readLine()) != null) {
                log.debug(s);
                if ("END".equals(s)) {
                    break;
                }
                String[] split = s.split(" ", 2);
                responseElements.put(split[0], split[1]);
            }
            return Response.mapBuilder(responseElements).build();
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
