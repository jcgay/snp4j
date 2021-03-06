package fr.jcgay.snp4j.impl;

import fr.jcgay.snp4j.Application;
import fr.jcgay.snp4j.Icon;
import fr.jcgay.snp4j.impl.request.Action;
import fr.jcgay.snp4j.impl.request.Request;
import fr.jcgay.snp4j.request.Sound;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestSerializerTest {

    private RequestSerializer serializer = new RequestSerializer();
    private Application protectedApplication = Application.withPassword("application/x-vnd-test", "Test");
    private Application unprotectedApplication = Application.withoutPassword("application/x-vnd-test", "Test");

    @Test
    public void should_serialize_a_request() throws Exception {

        Request request = Request.builder(protectedApplication)
                .addAction(
                        Action.name("action")
                                .withParameter("key1", "value1")
                                .build()
                )
                .addAction(
                        Action.name("notify")
                                .withParameter("key2", Icon.path("icon"))
                                .withParameter("key3", Sound.path("sound"))
                                .build()
                )
                .addAction(
                        Action.name("hello").build()
                )
                .addAction(
                        Action.name("sanitize")
                                .withParameter("key4", "line\n break, carriage\r return")
                                .withParameter("key5", "and&equal=")
                                .build()
                )
                .build();

        String result = serializer.stringify(request, "password-with-salt");

        BufferedReader reader = new BufferedReader(new StringReader(result));
        assertThat(reader.readLine()).isEqualTo("SNP/3.0 password-with-salt");
        assertThat(reader.readLine()).isEqualTo("action?password=" + protectedApplication.getPassword() + "&key1=value1");
        assertThat(reader.readLine()).isEqualTo("notify?password=" + protectedApplication.getPassword() + "&key2=icon&key3=sound");
        assertThat(reader.readLine()).isEqualTo("hello?password=" + protectedApplication.getPassword());
        assertThat(reader.readLine()).isEqualTo("sanitize?password=" + protectedApplication.getPassword() + "&key4=line\\n break, carriage\\n return&key5=and&&equal==");
        assertThat(reader.readLine()).isEqualTo("END");
    }

    @Test
    public void should_serialize_a_request_without_password() throws Exception {

        Request request = Request.builder(unprotectedApplication)
                .addAction(
                        Action.name("notify")
                                .withParameter("key1", "value1")
                                .build()
                )
                .build();

        String result = serializer.stringify(request, "password-with-salt");

        BufferedReader reader = new BufferedReader(new StringReader(result));
        assertThat(reader.readLine()).isEqualTo("SNP/3.0 password-with-salt");
        assertThat(reader.readLine()).isEqualTo("notify?key1=value1");
        assertThat(reader.readLine()).isEqualTo("END");
    }
}
