package com.github.jcgay.snp4j;

import com.github.jcgay.snp4j.impl.RequestSerializer;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.xml.bind.DatatypeConverter;
import java.net.URL;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Icon {

    @Getter
    private final String value;
    private final boolean base64;

    public static Icon path(@NonNull String path) {
        return new Icon(path, false);
    }

    public static Icon url(@NonNull URL url) {
        return new Icon(url.toString(), false);
    }

    public static Icon stock(@NonNull String name) {
        return new Icon("!" + name, false);
    }

    public static Icon base64(byte[] bytes) {
        return new Icon(sanitize(DatatypeConverter.printBase64Binary(bytes)), true);
    }

    private static String sanitize(String string) {
        return string
                .replace('=', '%')
                .replace(RequestSerializer.CRLF, "#");
    }
}
