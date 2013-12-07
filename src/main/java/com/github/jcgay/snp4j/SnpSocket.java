package com.github.jcgay.snp4j;

import com.github.jcgay.snp4j.request.Request;
import lombok.NonNull;

import java.io.Closeable;

public interface SnpSocket extends Closeable {

    boolean send(@NonNull Request request);
}
