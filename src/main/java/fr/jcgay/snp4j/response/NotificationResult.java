package fr.jcgay.snp4j.response;

import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

@Data
public class NotificationResult {

    @NonNull
    private final UUID uuid;
}
