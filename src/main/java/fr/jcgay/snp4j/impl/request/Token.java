package fr.jcgay.snp4j.impl.request;

import fr.jcgay.snp4j.SnpException;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Data
@ToString(exclude = { "password", "salt" })
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Token {

    private static final Token NONE = new Token(null, null) {
        @Override
        public String hash() {
            return "";
        }
    };

    private final String password;
    private final String salt;

    public static Token none() {
        return NONE;
    }

    public static Token create(String password) {
        if (password == null) {
            return NONE;
        }
        return new Token(password, UUID.randomUUID().toString().replace("-", ""));
    }

    static Token of(String password, String salt) {
        return new Token(password, salt);
    }

    public String hash() {
        return "SHA-256:" + passwordWithSalt() + "." + salt;
    }

    private String passwordWithSalt() {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new SnpException("Cannot hash token with SHA-256 algorithm.", e);
        }

        md.update((password + salt).getBytes());
        return DatatypeConverter.printHexBinary(md.digest());
    }
}
