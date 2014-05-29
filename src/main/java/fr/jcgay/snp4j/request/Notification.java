package fr.jcgay.snp4j.request;

import fr.jcgay.snp4j.Icon;
import lombok.Data;

@Data
public class Notification {

    /**
     * Notification title.
     */
    private String title;
    /**
     * Notification body text.
     */
    private String text;
    /**
     * The identifier of the class to use.
     */
    private String classId;
    /**
     * The amount of time, in seconds, the notification should remain on screen. <br />
     * A timeout of zero means the notification should remain on screen until the user dismisses it (behaviour commonly
     * referred to as "sticky"). <br />
     * A timeout of {@code -1} (the default) indicates that the notification should use the default duration (this is the
     * recommended practice).
     */
    private Integer timeout;
    /**
     * The icon to use. <br />
     */
    private Icon icon;
    /**
     * A sound file to play (can be a WAV or MP3 file).
     */
    private Sound sound;
    /**
     * The urgency of the notification.
     */
    private Priority priority;
    /**
     * If set to {@code 1}, the notification is not logged in the missed list or history.
     */
    private boolean log = true;
}
