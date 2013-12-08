package com.github.jcgay.snp4j;

import com.github.jcgay.snp4j.request.Request;
import com.github.jcgay.snp4j.response.Response;
import lombok.NonNull;

import java.io.Closeable;

public interface SnpSocket extends Closeable {

    Response send(@NonNull Request request);
}
