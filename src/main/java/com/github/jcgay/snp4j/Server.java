package com.github.jcgay.snp4j;

import lombok.Data;
import lombok.NonNull;

@Data
public class Server {

    @NonNull
    private final String host;
    private final int port;
}
