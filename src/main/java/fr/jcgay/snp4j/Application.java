package fr.jcgay.snp4j;

import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

import java.util.UUID;

/**
 * Describe the application interacting with Snarl.
 */
@Data(staticConstructor = "of")
@ToString(exclude = "password")
public class Application {

    /**
     * A signature is used to uniquely identify the application. <br />
     * In order to ensure your application's signature is unique, the recommendation is to follow the Internet Media
     * type (also known as MIME content type) format as defined in IETF <a href="http://tools.ietf.org/html/rfc2046">RFC 2046</a>
     * which contains the application vendor's name - specifically: {@code application/x-vnd-some_vendor.some_app}. <br />
     * The signature must not contain spaces. <br />
     * Some examples of acceptable signatures:
     * <ul>
     *     <li>{@code application/x-vnd-acme.hello_world}</li>
     *     <li>{@code application/x-vnd-fullphat.snaRSS}</li>
     * </ul>
     *
     * @see <a href="https://sites.google.com/site/snarlapp/developers/developer-guide#TOC-Registering">Registering an application</a>
     */
    @NonNull
    private final String appSig;
    /**
     * The title identifies the application to the user.
     */
    @NonNull
    private final String title;
    /**
     * A random password is generated when creating an application. <br />
     * It is then used to prevent other applications from creating notifications by masquerading as an already registered application.
     *
     * @see <a href="https://sites.google.com/site/snarlapp/developers/developer-guide#TOC-Application-Passwords">Application passwords</a>
     */
    @NonNull
    private final String password = UUID.randomUUID().toString();
}
