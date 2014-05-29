package fr.jcgay.snp4j;

import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

import java.util.UUID;

@Data(staticConstructor = "of")
@ToString(exclude = "password")
public class Application {

    @NonNull
    private final String appSig;
    @NonNull
    private final String title;
    private final String password = UUID.randomUUID().toString();
}
