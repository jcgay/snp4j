package com.github.jcgay.snp4j.impl;

import com.github.jcgay.snp4j.Application;
import com.github.jcgay.snp4j.Icon;
import com.github.jcgay.snp4j.SnpException;
import com.github.jcgay.snp4j.impl.request.Parameter;
import com.github.jcgay.snp4j.impl.request.Request;
import com.github.jcgay.snp4j.impl.response.Error;
import com.github.jcgay.snp4j.impl.response.Response;
import com.github.jcgay.snp4j.impl.response.Status;
import com.github.jcgay.snp4j.request.Notification;
import com.github.jcgay.snp4j.request.Priority;
import com.github.jcgay.snp4j.request.Sound;
import com.github.jcgay.snp4j.response.NotificationResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static com.github.jcgay.snp4j.assertion.SnpAssertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
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

        when(socket.send(isA(Request.class))).thenReturn(successfullResponse());

        // When
        NotificationResult result = notifier.send(notification);

        // Then
        assertThat(result.getUuid()).isNotNull();

        verify(socket).send(requestCapture.capture());
        assertThat(requestCapture.getValue())
                .hasApplication(application)
                .containsEntry("notify", Parameter.of("app-sig", application.getAppSig()))
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
    public void should_send_notification_with_base64_icon() throws Exception {

        // Given
        Notification notification = new Notification();
        notification.setIcon(Icon.base64("icon".getBytes()));

        when(socket.send(isA(Request.class))).thenReturn(successfullResponse());

        // When
        notifier.send(notification);

        // Then
        verify(socket).send(requestCapture.capture());
        assertThat(requestCapture.getValue())
                .hasApplication(application)
                .containsEntry("notify", Parameter.of("icon-base64", Icon.base64("icon".getBytes())));
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

    @Test
    public void should_fail_when_sending_a_malformed_notification() throws Exception {

        Notification notification = new Notification();
        notification.setIcon(null);
        notification.setText(null);
        notification.setTitle(null);

        try {
            notifier.send(notification);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (IllegalArgumentException e) {
            assertThat(e).hasMessage("At least one of <title>, <text> or <icon> must be supplied for the command to succeed.");
        }
    }

    @Test
    public void should_not_fail_when_sending_notification_with_icon() throws Exception {

        Notification notification = new Notification();
        notification.setIcon(Icon.path("/icon"));
        notification.setText(null);
        notification.setTitle(null);

        when(socket.send(isA(Request.class))).thenReturn(successfullResponse());

        NotificationResult result = notifier.send(notification);

        assertThat(result).isNotNull();
    }

    @Test
    public void should_not_fail_when_sending_notification_with_text() throws Exception {

        Notification notification = new Notification();
        notification.setIcon(null);
        notification.setText("text");
        notification.setTitle(null);

        when(socket.send(isA(Request.class))).thenReturn(successfullResponse());

        NotificationResult result = notifier.send(notification);

        assertThat(result).isNotNull();
    }

    @Test
    public void should_not_fail_when_sending_notification_with_title() throws Exception {

        Notification notification = new Notification();
        notification.setIcon(null);
        notification.setText(null);
        notification.setTitle("title");

        when(socket.send(isA(Request.class))).thenReturn(successfullResponse());

        NotificationResult result = notifier.send(notification);

        assertThat(result).isNotNull();
    }

    @Test
    public void should_unregister_application_when_closing() throws Exception {

        notifier.close();

        verify(socket).send(requestCapture.capture());
        verify(socket).close();

        assertThat(requestCapture.getValue())
                .hasApplication(application)
                .containsEntry("unregister", Parameter.of("app-sig", application.getAppSig()));
    }

    @Test
    public void should_set_default_timeout_when_not_set() throws Exception {

        // Given
        Notification notification = new Notification();
        notification.setTitle("title");
        when(socket.send(isA(Request.class))).thenReturn(successfullResponse());

        // When
        NotificationResult result = notifier.send(notification);

        // Then
        assertThat(result.getUuid()).isNotNull();

        verify(socket).send(requestCapture.capture());
        assertThat(requestCapture.getValue())
                .containsEntry("notify", Parameter.of("timeout", -1));
    }

    private static Response successfullResponse() {
        return Response.builder(Status.OK, new Date(), "daemon", "host").build();
    }
}
