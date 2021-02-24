package com.voxcrafterlp.statsaddon.webserver;

import java.io.File;

public class Resource {

    private String content;
    private File file;
    private String mime;

    public Resource(String content, File file, String mime) {
        this.content = content;
        this.file = file;
        this.mime = mime;
    }

    public String getContent() {
        return content;
    }

    public File getFile() {
        return file;
    }

    public String getMime() {
        return mime;
    }
}
