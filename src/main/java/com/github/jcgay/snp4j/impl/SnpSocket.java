package com.github.jcgay.snp4j.impl;

import com.github.jcgay.snp4j.impl.request.Request;
import com.github.jcgay.snp4j.impl.response.Response;
import lombok.NonNull;

import java.io.Closeable;

public interface SnpSocket extends Closeable {

    Response send(@NonNull Request request);
}
