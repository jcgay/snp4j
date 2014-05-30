package fr.jcgay.snp4j;

import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

import java.util.concurrent.TimeUnit;

/**
 * Describe the Snarl instance we will talk to.
 */
@Data
@ToString(exclude = "password")
public class Server {

    /**
     * Host. By default: {@code localhost}.
     */
    @NonNull
    private final String host;
    /**
     * Port. By default: {@code 9887}.
     */
    private final int port;
    /**
     * Timeout. By Default: {@code 1 second}.
     */
    private final long timeout;
    /**
     * The password (if any) defined in Snarl > Settings > Options > Network > Security > Password.
     */
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

        public Builder withPassword(String password) {
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
