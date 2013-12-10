package com.github.jcgay.snp4j.assertion;

import com.github.jcgay.snp4j.Icon;
import com.github.jcgay.snp4j.impl.request.Request;
import com.github.jcgay.snp4j.impl.response.Response;
import org.assertj.core.api.Assertions;

public class SnpAssertions extends Assertions {

    public static ResponseAssert assertThat(Response actual) {
        return ResponseAssert.assertThat(actual);
    }

    public static RequestAssert assertThat(Request actual) {
        return RequestAssert.assertThat(actual);
    }

    public static IconAssert assertThat(Icon actual) {
        return new IconAssert(actual);
    }
}
