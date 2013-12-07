package com.github.jcgay.snp4j.impl;

import com.github.jcgay.snp4j.request.Action;
import com.github.jcgay.snp4j.request.Parameter;
import com.github.jcgay.snp4j.request.Request;
import lombok.NonNull;

public class RequestSerializer {

    private static final String CR = "\r";
    private static final String LF = "\n";
    private static final String CRLF = CR + LF;

    String stringify(@NonNull Request request) {

        StringBuilder builder = new StringBuilder();
        builder.append(buildHeader());
        builder.append(CRLF);
        for (Action action : request.getActions()) {
            builder.append(buildAction(action));
            builder.append(CRLF);
        }
        builder.append(buildFooter());
        builder.append(CRLF);

        return builder.toString();
    }

    private String buildFooter() {
        return "END";
    }

    private String buildHeader() {
        return "SNP/3.0";
    }

    private String buildAction(Action action) {
        StringBuilder builder = new StringBuilder();
        builder.append(sanitize(action.getName()));
        if (!action.getParameters().isEmpty()) {
            builder.append('?');
            for (Parameter parameter : action.getParameters()) {
                builder.append(sanitize(parameter.getKey()));
                builder.append('=');
                builder.append(sanitize(parameter.getValue().toString()));
                builder.append('&');
            }
        }
        return builder.toString();
    }

    private String sanitize(String string) {
        return string
                .replace(CR, "'\n'")
                .replace(LF, "'\n'")
                .replace("&", "&&")
                .replace("=", "==");
    }
}
