package com.github.jcgay.snp4j;

import com.github.jcgay.snp4j.request.Notification;
import com.github.jcgay.snp4j.response.NotificationResult;
import lombok.NonNull;

public interface Notifier {

    NotificationResult send(@NonNull Notification notification);
}
