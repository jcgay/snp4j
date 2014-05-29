package fr.jcgay.snp4j;

import lombok.Data;
import lombok.NonNull;

@Data(staticConstructor = "of")
public class Application {

    @NonNull
    private final String appSig;
    @NonNull
    private final String title;
}
