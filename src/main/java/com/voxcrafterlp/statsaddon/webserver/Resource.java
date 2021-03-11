package com.voxcrafterlp.statsaddon.webserver;

import lombok.Getter;

import java.io.File;

@Getter
public class Resource {

    private final File file;
    private final String mime;

    public Resource(File file, String mime) {
        this.file = file;
        this.mime = mime;
    }

}
