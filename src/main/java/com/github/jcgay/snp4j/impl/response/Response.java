package com.github.jcgay.snp4j.impl.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Response {

    @NonNull
    private final Status status;
    @NonNull
    private final Date time;
    @NonNull
    private final String daemon;
    @NonNull
    private final String host;

    private final Error error;

    public static MapBuilder mapBuilder(@NonNull Map<String, String> responseElements) {
        return new MapBuilder(responseElements);
    }

    public static Builder builder(Status status, Date time, String daemon, String host) {
        return new Builder(status, time, daemon, host);
    }

    public boolean hasSucceeded() {
        return status == Status.OK;
    }

    public boolean hasNotSucceeded() {
        return !hasSucceeded();
    }

    public static class Builder {

        private final Status status;
        private final Date time;
        private final String daemon;
        private final String host;
        private Error error;

        protected Builder(Status status, Date time, String daemon, String host) {
            this.status = status;
            this.time = time;
            this.daemon = daemon;
            this.host = host;
        }

        public Builder withError(Error error) {
            this.error = error;
            return this;
        }

        public Response build() {
            return new Response(status, time, daemon, host, error);
        }
    }

    public static class MapBuilder {

        private static enum Key {
            HEADER("SNP/3.0"),
            TIMESTAMP("x-timestamp:"),
            HOST("x-host:"),
            DAEMON("x-daemon:"),
            RESULT("result:"),
            ERROR_CODE("error-code:"),
            ERROR_NAME("error-name:"),
            ERROR_HINT("error-hint:");

            private String value;

            private Key(String value) {
                this.value = value;
            }
        }

        private final Map<String, String> responseElements;

        protected MapBuilder(Map<String, String> responseElements) {
            this.responseElements = responseElements;
        }

        public Response build() {
            return new Response(
                    getStatus(responseElements),
                    parseTime(responseElements.get(Key.TIMESTAMP.value)),
                    responseElements.get(Key.DAEMON.value),
                    responseElements.get(Key.HOST.value),
                    buildError(responseElements)
            );
        }

        private Error buildError(Map<String, String> responseElements) {
            Error error = parseResult(responseElements.get(Key.RESULT.value));
            if (error != null) {
                return error;
            }

            if (responseElements.containsKey(Key.ERROR_CODE.value)) {
                return new Error(
                        Status.fromCode(responseElements.get(Key.ERROR_CODE.value)),
                        responseElements.get(Key.ERROR_NAME.value),
                        responseElements.get(Key.ERROR_HINT.value)
                );
            }

            return null;
        }

        private Error parseResult(String result) {
            if (result == null) {
                return null;
            }
            String[] elements = result.split(" ", 3);
            Status status = Status.fromCode(elements[1]);
            if (status == Status.OK) {
                return null;
            }
            return new Error(status, elements[2], status.getHint());
        }

        private Date parseTime(String s) {
            try {
                return new SimpleDateFormat("d MMM yyyy HH:mm:ss").parse(s);
            } catch (ParseException e) {
                return new Date();
                //throw new SnpException("Cannot parse response timestamp.", e);
            }
        }

        private Status getStatus(Map<String, String> elements) {
            if (elements.containsKey(Key.RESULT.value)) {
                Error error = parseResult(elements.get(Key.RESULT.value));
                if (error != null) {
                    return error.getStatus();
                }
            }

            if (elements.get(Key.HEADER.value).contains("OK")) {
                return Status.OK;
            }
            return Status.fromCode(elements.get(Key.ERROR_CODE.value));
        }
    }
}
