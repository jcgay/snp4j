package com.github.jcgay.snp4j.impl;

import com.github.jcgay.snp4j.Application;
import com.github.jcgay.snp4j.impl.request.Action;
import com.github.jcgay.snp4j.impl.request.Request;
import com.github.jcgay.snp4j.impl.response.Response;
import com.github.jcgay.snp4j.impl.response.Status;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.UUID;

import static com.github.jcgay.snp4j.assertion.SnpAssertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SnpSocketImplTest {

    @Mock
    private RequestSerializer serializer;
    @Mock
    private Socket socket;
    @Mock
    private BufferedReader reader;

    private SnpSocketImpl underTest;

    private StringWriter writer;
    private Application application = Application.of("application/x-vnd-test", "Test");

    @Before
    public void setUp() throws Exception {
        writer = new StringWriter();
        underTest = new SnpSocketImpl(
                new PrintWriter(writer),
                reader,
                socket,
                serializer
        );
    }

    @Test
    public void should_send_message_success_response() throws Exception {

        // Given
        Action register = Action.name("register")
                .withParameter("app-sig", application.getAppSig())
                .withParameter("uid", UUID.randomUUID())
                .withParameter("title", application.getTitle())
                .build();

        Request request = Request.builder(application)
                .addAction(register)
                .build();

        when(reader.readLine())
                .thenReturn("SNP/3.0 OK")
                .thenReturn("result: register 0 Ok")
                .thenReturn("x-timestamp: 8 Dec 2013 01:52:14")
                .thenReturn("x-daemon: Snarl 3.0")
                .thenReturn("x-host: ie10win7")
                .thenReturn("END");

        when(serializer.stringify(request)).thenReturn("a.request");

        // When
        Response result = underTest.send(request);

        // Then
        assertThat(writer.toString()).isEqualTo("a.request");

        assertThat(result)
                .hasStatus(Status.OK)
                .hasHost("ie10win7")
                .hasDaemon("Snarl 3.0")
                .hasNoError();
    }

    @Test
    public void should_send_message_error_response() throws Exception {

        // Given
        Action register = Action.name("register")
                .build();

        Request request = Request.builder(application)
                .addAction(register)
                .build();

        when(reader.readLine())
                .thenReturn("SNP/3.0 OK")
                .thenReturn("result: register 109 ArgMissing")
                .thenReturn("x-timestamp: 8 Dec 2013 09:11:07")
                .thenReturn("x-daemon: Snarl 3.0")
                .thenReturn("x-host: ie10win7")
                .thenReturn("END");

        when(serializer.stringify(request)).thenReturn("a.request");

        // When
        Response result = underTest.send(request);

        // Then
        assertThat(writer.toString()).isEqualTo("a.request");

        assertThat(result)
                .hasStatus(Status.ARG_MISSING)
                .hasHost("ie10win7")
                .hasDaemon("Snarl 3.0")
                .hasError(new com.github.jcgay.snp4j.impl.response.Error(Status.ARG_MISSING, "ArgMissing", "A required argument was missing"));
    }

    @Test
    public void should_send_message_error_response_as_specified_by_the_documentation() throws Exception {

        // Given
        Action register = Action.name("register")
                .withParameter("app-sig", application.getAppSig())
                .withParameter("uid", UUID.randomUUID())
                .withParameter("title", application.getTitle())
                .build();

        Request request = Request.builder(application)
                .addAction(register)
                .build();

        when(reader.readLine())
                .thenReturn("SNP/3.0 FAILED")
                .thenReturn("error-code: 211")
                .thenReturn("error-name: AuthenticationFailure")
                .thenReturn("error-hint: Digest Mismatch")
                .thenReturn("x-timestamp: 8 Dec 2013 09:11:07")
                .thenReturn("x-daemon: Snarl 2.4")
                .thenReturn("x-host: conerstone")
                .thenReturn("END");

        when(serializer.stringify(request)).thenReturn("a.request");

        // When
        Response result = underTest.send(request);

        // Then
        assertThat(writer.toString()).isEqualTo("a.request");

        assertThat(result)
                .hasStatus(Status.AUTH_FAILURE)
                .hasHost("conerstone")
                .hasDaemon("Snarl 2.4")
                .hasError(new com.github.jcgay.snp4j.impl.response.Error(Status.AUTH_FAILURE, "AuthenticationFailure", "Digest Mismatch"));
    }

    @Test
    public void should_close() throws Exception {

        underTest.close();

        verify(socket).close();
    }
}
