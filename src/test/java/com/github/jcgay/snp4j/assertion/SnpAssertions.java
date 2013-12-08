package com.github.jcgay.snp4j.assertion;

import com.github.jcgay.snp4j.response.Response;
import org.assertj.core.api.Assertions;

public class SnpAssertions extends Assertions {

    public static ResponseAssert assertThat(Response actual) {
        return new ResponseAssert(actual);
    }
}
