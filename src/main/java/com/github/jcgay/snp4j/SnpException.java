package com.github.jcgay.snp4j;

import com.github.jcgay.snp4j.impl.response.Error;
import com.github.jcgay.snp4j.impl.response.Status;
import lombok.Getter;

public class SnpException extends RuntimeException {

    @Getter
    private Status status;

    public SnpException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SnpException(Error error) {
        super(error.getHint());
        this.status = error.getStatus();
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(super.toString())
                .append("\n")
                .append(" Status: ")
                .append(status != null ? status : "NONE")
                .toString();
    }
}
