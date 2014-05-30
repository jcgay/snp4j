package fr.jcgay.snp4j.response;

import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

/**
 * The result returned when a notification has been sent.
 */
@Data
public class NotificationResult {

    /**
     * A unique identifier for the notification.
     */
    @NonNull
    private final UUID uuid;
}
