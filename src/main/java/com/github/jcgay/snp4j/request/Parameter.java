package com.github.jcgay.snp4j.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Parameter {

    @NonNull
    private final String key;
    private final Object value;

    public static Parameter of(String key, Object value) {
        if (value == null) {
            return EmptyParameter.empty();
        }
        return new Parameter(key, value);
    }
}
