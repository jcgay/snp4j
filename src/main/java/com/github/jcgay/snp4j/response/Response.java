package com.github.jcgay.snp4j.response;

import com.github.jcgay.snp4j.SnpException;
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

    public static Builder builder(@NonNull Map<String, String> responseElements) {
        return new Builder(responseElements);
    }

    public static class Builder {

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

        protected Builder(Map<String, String> responseElements) {
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
            if (responseElements.containsKey(Key.ERROR_CODE.value)) {
                return new Error(
                        responseElements.get(Key.ERROR_NAME.value),
                        responseElements.get(Key.ERROR_HINT.value)
                );
            }

            return null;
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
            if (elements.get(Key.HEADER.value).contains("OK")) {
                return Status.OK;
            }
            return Status.fromCode(elements.get(Key.ERROR_CODE.value));
        }
    }
}
