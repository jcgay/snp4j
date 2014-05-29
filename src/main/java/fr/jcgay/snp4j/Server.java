package fr.jcgay.snp4j;

import lombok.Data;
import lombok.NonNull;

import java.util.concurrent.TimeUnit;

@Data
public class Server {

    @NonNull
    private final String host;
    private final int port;
    private final long timeout;

    public Server(String host, int port, long timeout) {
        this.host = host;
        this.port = port;
        this.timeout = timeout;
    }

    public Server(String host, int port) {
        this(host, port, TimeUnit.SECONDS.toMillis(1L));
    }
}
