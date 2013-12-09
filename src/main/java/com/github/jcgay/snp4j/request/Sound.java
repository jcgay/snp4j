package com.github.jcgay.snp4j.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Sound {

    private final String path;

    public static Sound path(@NonNull String path) {
        return new Sound(path);
    }

    public static Sound windows(@NonNull String name) {
        return new Sound(name);
    }
}
