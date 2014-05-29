package fr.jcgay.snp4j.impl;

import fr.jcgay.snp4j.impl.request.Request;
import fr.jcgay.snp4j.impl.response.Response;
import lombok.NonNull;

import java.io.Closeable;

public interface SnpSocket extends Closeable {

    Response send(@NonNull Request request);
}
