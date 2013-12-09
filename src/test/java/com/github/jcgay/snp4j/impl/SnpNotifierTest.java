package com.github.jcgay.snp4j.impl;

import com.github.jcgay.snp4j.Application;
import com.github.jcgay.snp4j.Icon;
import com.github.jcgay.snp4j.SnpException;
import com.github.jcgay.snp4j.assertion.SnpAssertions;
import com.github.jcgay.snp4j.impl.request.Parameter;
import com.github.jcgay.snp4j.impl.request.Request;
import com.github.jcgay.snp4j.impl.response.*;
import com.github.jcgay.snp4j.impl.response.Error;
import com.github.jcgay.snp4j.request.Notification;
import com.github.jcgay.snp4j.request.Priority;
import com.github.jcgay.snp4j.request.Sound;
import com.github.jcgay.snp4j.response.NotificationResult;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.assertj.core.api.Assertions.not;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SnpNotifierTest {

    @Mock
    private SnpSocket socket;

    private SnpNotifier notifier;

    private Application application = Application.of("application/x-vnd-test", "Test");

    @Captor
    private ArgumentCaptor<Request> requestCapture;

    @Before
    public void setUp() throws Exception {
        notifier = new SnpNotifier(application, socket);
    }

    @Test
    public void should_send_notification() throws Exception {

        // Given
        Notification notification = new Notification();
        notification.setClassId("class-id");
        notification.setIcon(Icon.path("/path"));
        notification.setLog(true);
        notification.setPriority(Priority.HIGH);
        notification.setSound(Sound.windows("name"));
        notification.setText("a-text");
        notification.setTimeout(10);
        notification.setTitle("a-title");

        when(socket.send(isA(Request.class))).thenReturn(Response.builder(Status.OK, new Date(), "daemon", "host").build());

        // When
        NotificationResult result = notifier.send(notification);

        // Then
        assertThat(result.getUuid()).isNotNull();

        verify(socket).send(requestCapture.capture());
        SnpAssertions.assertThat(requestCapture.getValue())
                .hasApplication(application)
                .containsEntry("notify", Parameter.of("id", "class-id"))
                .containsEntry("notify", Parameter.of("title", "a-title"))
                .containsEntry("notify", Parameter.of("text", "a-text"))
                .containsEntry("notify", Parameter.of("timeout", 10))
                .containsEntry("notify", Parameter.of("icon", Icon.path("/path")))
                .containsEntry("notify", Parameter.of("sound", Sound.windows("name")))
                .containsEntry("notify", Parameter.of("priority", Priority.HIGH))
                .containsEntry("notify", Parameter.of("uuid", result.getUuid()));
    }

    @Test
    public void should_fail_when_sending_notification() throws Exception {

        // Given
        Response response = Response.builder(Status.ARG_MISSING, new Date(), "daemon", "host")
                .withError(new Error(Status.ARG_MISSING, "name", "hint"))
                .build();
        when(socket.send(isA(Request.class))).thenReturn(response);

        Notification notification = new Notification();
        notification.setText("text");

        try {
            // When
            notifier.send(notification);
            failBecauseExceptionWasNotThrown(SnpException.class);
        } catch (SnpException e) {
            // Then
            assertThat(e).hasMessage("hint");
            assertThat(e.getStatus()).isEqualTo(Status.ARG_MISSING);
        }
    }
}
