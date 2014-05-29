package fr.jcgay.snp4j.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Sound {

    private final String value;

    public static Sound path(@NonNull String path) {
        return new Sound(path);
    }

    public static Sound windows(@NonNull String name) {
        return new Sound(name);
    }
}
