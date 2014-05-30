package fr.jcgay.snp4j;

import fr.jcgay.snp4j.impl.RequestSerializer;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.xml.bind.DatatypeConverter;
import java.net.URL;

/**
 * An icon that can be displayed in a notification.
 */
@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Icon {

    @Getter
    private final String value;
    /**
     * Indicate if the current Icon is represented as a Base64 String.
     */
    private final boolean base64;

    /**
     * Create an icon from a fully qualified path to an image (local or remote).
     *
     * @param path a fully qualified path to an image (local or remote).
     * @return an icon as {@link fr.jcgay.snp4j.Icon}.
     */
    @NonNull
    public static Icon path(@NonNull String path) {
        return new Icon(path, false);
    }

    /**
     * Create an icon from a URL.
     *
     * @param url a URL.
     * @return an icon as {@link fr.jcgay.snp4j.Icon}.
     */
    @NonNull
    public static Icon url(@NonNull URL url) {
        return new Icon(url.toString(), false);
    }

    /**
     * Create a standard Snarl icon (as defined in Snarl).
     *
     * @param name the icon name.
     * @return an icon as {@link fr.jcgay.snp4j.Icon}.
     */
    @NonNull
    public static Icon stock(@NonNull String name) {
        return new Icon("!" + name, false);
    }

    /**
     * Create an icon based on a binary representation.
     *
     * @param bytes the icon.
     * @return an icon as {@link fr.jcgay.snp4j.Icon}.
     */
    @NonNull
    public static Icon base64(byte[] bytes) {
        return new Icon(sanitize(DatatypeConverter.printBase64Binary(bytes)), true);
    }

    private static String sanitize(String string) {
        return string
                .replace('=', '%')
                .replace(RequestSerializer.CRLF, "#");
    }
}
