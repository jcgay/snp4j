package fr.jcgay.snp4j;

import fr.jcgay.snp4j.request.Notification;
import fr.jcgay.snp4j.response.NotificationResult;
import lombok.NonNull;

import java.io.Closeable;

public interface Notifier extends Closeable {

    NotificationResult send(@NonNull Notification notification);
}
