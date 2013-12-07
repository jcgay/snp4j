package com.github.jcgay.snp4j;

import java.net.URL;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Icon {

    @Getter
    private final String value;

    private Icon(String value) {
        this.value = value;
    }

    public static Icon path(@NonNull String path) {
        return new Icon(path);
    }

    public static Icon url(@NonNull URL url) {
        return new Icon(url.toString());
    }

    public static Icon stock(@NonNull String name) {
        return new Icon("!" + name);
    }
}
