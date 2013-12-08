package com.github.jcgay.snp4j.assertion;

import com.github.jcgay.snp4j.response.Response;
import com.github.jcgay.snp4j.response.Status;
import org.assertj.core.api.AbstractAssert;


public class ResponseAssert extends AbstractAssert<ResponseAssert, Response> {

    public ResponseAssert(Response actual) {
        super(actual, ResponseAssert.class);
    }

    public static ResponseAssert assertThat(Response actual) {
        return new ResponseAssert(actual);
    }

    public ResponseAssert hasStatus(Status status) {
        isNotNull();
        if (actual.getStatus() != status) {
            failWithMessage("Expected status to be <%s> but was <%s>.", status, actual.getStatus());
        }
        return this;
    }

    public ResponseAssert hasHost(String host) {
        isNotNull();
        checkNotNull(host);
        if (!host.equals(actual.getHost())) {
            failWithMessage("Expected host to be <%s> but was <%s>.", host, actual.getHost());
        }
        return this;
    }

    public ResponseAssert hasDaemon(String daemon) {
        isNotNull();
        checkNotNull(daemon);
        if (!daemon.equals(actual.getDaemon())) {
            failWithMessage("Expected daemon to be <%s> but was <%s>.", daemon, actual.getHost());
        }
        return this;
    }

    public ResponseAssert hasError(com.github.jcgay.snp4j.response.Error error) {
        isNotNull();
        checkNotNull(error);
        if (!error.equals(actual.getError())) {
            failWithMessage("Expected error to be <%s> but was <%s>.", error, actual.getError());
        }
        return this;
    }

    public ResponseAssert hasNoError() {
        if (actual.getError() != null) {
            failWithMessage("Expected no error but was <%s>.", actual.getError());
        }
        return this;
    }

    private void checkNotNull(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("Expected value cannot be null.");
        }
    }
}