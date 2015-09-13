package fr.jcgay.snp4j.impl;

import fr.jcgay.snp4j.Icon;
import fr.jcgay.snp4j.impl.request.Action;
import fr.jcgay.snp4j.impl.request.Parameter;
import fr.jcgay.snp4j.impl.request.Request;
import fr.jcgay.snp4j.request.Sound;
import lombok.NonNull;

import java.util.Iterator;

public class RequestSerializer {

    private static final String CR = "\r";
    private static final String LF = "\n";
    public static final String CRLF = CR + LF;

    String stringify(@NonNull Request request, String keyHash) {

        StringBuilder builder = new StringBuilder()
                .append(buildHeader(keyHash))
                .append(CRLF);
        for (Action action : request.getActions()) {
            builder.append(buildAction(action, request.getApplication().getPassword()))
                    .append(CRLF);
        }
        builder.append(buildFooter())
                .append(CRLF);

        return builder.toString();
    }

    private String buildFooter() {
        return "END";
    }

    private String buildHeader(String keyHash) {
        return "SNP/3.0 " + keyHash;
    }

    private String buildAction(Action action, String password) {
        StringBuilder builder = new StringBuilder()
                .append(sanitize(action.getName()));
        if (password != null) {
            builder.append('?')
                    .append("password")
                    .append("=")
                    .append(password);
        }
        if (!action.getParameters().isEmpty()) {
            builder.append(password == null ? '?' : '&');
            Iterator<Parameter> iterator = action.getParameters().iterator();
            while (iterator.hasNext()) {
                Parameter parameter = iterator.next();
                builder.append(sanitize(parameter.getKey()))
                        .append('=')
                        .append(sanitize(getValue(parameter)));
                if (iterator.hasNext()) {
                    builder.append('&');
                }
            }
        }
        return builder.toString();
    }

    private String getValue(Parameter parameter) {
        Object value = parameter.getValue();
        if (value instanceof Icon) {
            return ((Icon) value).getValue();
        }
        if (value instanceof Sound) {
            return ((Sound) value).getValue();
        }
        return value.toString();
    }

    private String sanitize(String string) {
        return string
                .replace(CR, "\\n")
                .replace(LF, "\\n")
                .replace("&", "&&")
                .replace("=", "==");
    }
}
