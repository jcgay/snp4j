package fr.jcgay.snp4j;

import fr.jcgay.snp4j.request.Notification;
import fr.jcgay.snp4j.response.NotificationResult;
import lombok.NonNull;

import java.io.Closeable;

/**
 * Expose operation(s) available via Snarl protocol: {@code SNP}
 *
 * @see <a href="https://sites.google.com/site/snarlapp/developers/developer-guide">Snarl developer guide</a>
 */
public interface Notifier extends Closeable {

    /**
     * Send a notification.
     *
     * @param notification a notification (text, sound...) to send as {@link fr.jcgay.snp4j.request.Notification}
     * @return a {@link java.util.UUID} representing the notification sent.
     * @throws java.lang.IllegalArgumentException if notification is malformed.
     * @throws fr.jcgay.snp4j.SnpException if an unexpected error occurs while sending the notification
     */
    @NonNull
    NotificationResult send(Notification notification);
}
