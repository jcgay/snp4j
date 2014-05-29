package fr.jcgay.snp4j.impl.response;

import lombok.Data;
import lombok.NonNull;

@Data
public class Error {

    @NonNull
    private final Status status;
    @NonNull
    private final String name;
    private final String hint;
}
