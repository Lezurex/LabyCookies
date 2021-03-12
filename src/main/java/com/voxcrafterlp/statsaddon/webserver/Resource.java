package com.voxcrafterlp.statsaddon.webserver;

import lombok.Getter;

import java.io.InputStream;

@Getter
public class Resource {

    private final InputStream stream;
    private final String mime;

    public Resource(InputStream stream, String mime) {
        this.stream = stream;
        this.mime = mime;
    }

}
