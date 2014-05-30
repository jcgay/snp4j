package fr.jcgay.snp4j.impl.request;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TokenTest {

    @Test
    public void should_hash_password() throws Exception {
        assertThat(Token.of("password", "salt").hash())
                .isEqualTo("SHA-256:7A37B85C8918EAC19A9089C0FA5A2AB4DCE3F90528DCDEEC108B23DDF3607B99.salt");
    }

    @Test
    public void should_be_empty() throws Exception {
        assertThat(Token.none().hash()).isEmpty();
    }

    @Test
    public void should_return_null_when_password_is_null() throws Exception {
        assertThat(Token.create(null)).isSameAs(Token.none());
    }
}