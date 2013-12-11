package com.github.jcgay.snp4j.impl;

import com.github.jcgay.snp4j.Application;
import com.github.jcgay.snp4j.Icon;
import com.github.jcgay.snp4j.impl.request.Action;
import com.github.jcgay.snp4j.impl.request.Request;
import com.github.jcgay.snp4j.request.Sound;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestSerializerTest {

    private RequestSerializer serializer = new RequestSerializer();
    private Application application = Application.of("application/x-vnd-test", "Test");

    @Test
    public void should_serialize_a_request() throws Exception {

        Request request = Request.builder(application)
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

        String result = serializer.stringify(request);

        BufferedReader reader = new BufferedReader(new StringReader(result));
        assertThat(reader.readLine()).isEqualTo("SNP/3.0");
        assertThat(reader.readLine()).isEqualTo("action?key1=value1");
        assertThat(reader.readLine()).isEqualTo("notify?key2=icon&key3=sound");
        assertThat(reader.readLine()).isEqualTo("hello");
        assertThat(reader.readLine()).isEqualTo("sanitize?key4=line'\\n' break, carriage'\\n' return&key5=and&&equal==");
        assertThat(reader.readLine()).isEqualTo("END");
    }
}