package com.github.jcgay.snp4j.request;

public class EmptyParameter extends Parameter {

    private static final EmptyParameter EMPTY = new EmptyParameter();

    public static EmptyParameter empty() {
        return EMPTY;
    }

    private EmptyParameter() {
        super("", null);
    }
}
