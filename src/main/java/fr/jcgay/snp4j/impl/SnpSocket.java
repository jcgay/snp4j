package fr.jcgay.snp4j.impl;

import fr.jcgay.snp4j.impl.request.Request;
import fr.jcgay.snp4j.impl.response.Response;

import java.io.Closeable;

public interface SnpSocket extends Closeable {

    Response send(Request request);
}
