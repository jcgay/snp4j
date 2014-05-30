package fr.jcgay.snp4j.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * A sound that can be played when displaying a notification.
 */
@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Sound {

    private final String value;

    /**
     * Create a sound from a fully qualified sound path (can be a {@code wav} or a {@code mp3}.
     *
     * @param path a fully qualified sound path.
     * @return a sound as {@link fr.jcgay.snp4j.request.Sound}.
     */
    public static Sound path(@NonNull String path) {
        return new Sound(path);
    }

    /**
     * Create a sound from a Windows sound name.
     *
     * @param name a windows sound name.
     * @return a sound as {@link fr.jcgay.snp4j.request.Sound}.
     */
    public static Sound windows(@NonNull String name) {
        return new Sound(name);
    }
}
