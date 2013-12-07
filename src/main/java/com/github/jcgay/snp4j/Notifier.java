package com.github.jcgay.snp4j;

import lombok.NonNull;

public interface Notifier {

    boolean send(@NonNull Notification notification);
}
