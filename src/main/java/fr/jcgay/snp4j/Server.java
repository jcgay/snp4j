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
    private final String password;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String host;
        private String password;
        private Integer port;
        private Long timeout;

        private Builder() {}

        public Builder withHost(@NonNull String host) {
            this.host = host;
            return this;
        }

        public Builder withPassword(@NonNull String password) {
            this.password = password;
            return this;
        }

        public Builder withPort(int port) {
            this.port = port;
            return this;
        }

        public Builder withTimeout(long timeout) {
            this.timeout = timeout;
            return this;
        }

        public Server build() {
            return new Server(
                    host != null ? host : "localhost",
                    port != null ? port : 9887,
                    timeout != null ? timeout : TimeUnit.SECONDS.toMillis(1L),
                    password
            );
        }
    }
}
