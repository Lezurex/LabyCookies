package com.voxcrafterlp.statsaddon.webserver;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;

public class WebUtils {

    public Resource getResource(String path) {
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

            try {
                if (file.getName().endsWith(".js")) {
                    return new Resource(file, "text/javascript");
                } else if (file.getName().endsWith(".css")) {
                    return new Resource(file, "text/css");
                }
                return new Resource(file, file.toURL().openConnection().getContentType());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
