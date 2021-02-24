package com.voxcrafterlp.statsaddon.webserver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class WebUtils {

    public Resource getResourceAsString(String path) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(path);
        if (resource == null) {
            throw new IllegalArgumentException("File \"" + path + "\" was not found!");
        } else {
            File file = null;
            try {
                file = new File(resource.toURI());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            List<String> lines;
            StringBuffer stringBuffer = new StringBuffer();
            try {
                lines = Files.readAllLines(file.toPath());
                for (String line:lines) {
                    stringBuffer.append(line);
                };
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                return new Resource(stringBuffer.toString(), file, file.toURL().openConnection().getContentType());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
