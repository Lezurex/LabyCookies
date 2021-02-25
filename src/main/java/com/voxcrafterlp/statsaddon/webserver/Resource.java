package com.voxcrafterlp.statsaddon.webserver;

import java.io.File;

public class Resource {

    private File file;
    private String mime;

    public Resource(File file, String mime) {
        this.file = file;
        this.mime = mime;
    }

    public File getFile() {
        return file;
    }

    public String getMime() {
        return mime;
    }
}
