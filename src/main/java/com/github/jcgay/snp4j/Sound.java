package com.github.jcgay.snp4j;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Sound {

    @Getter
    private final String path;

    private Sound(String path) {
        this.path = path;
    }

    public Sound path(@NonNull String path) {
        return new Sound(path);
    }

    public Sound windows(@NonNull String name) {
        return new Sound(name);
    }
}
