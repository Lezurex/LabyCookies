package com.voxcrafterlp.statsaddon.webserver;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

public class WebUtils {

    /**
     * Loads a file form the resource folder and gets its MIME type.
     * @param path Valid path starting in resources folder
     * @return {@link Resource} object with useful information for web usage
     */
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

    /**
     * Splits a web path into single parts and removes empty values.
     * @param path A web path to be split
     * @return ArrayList of parts without empty values
     */
    public static ArrayList<String> getPathParts(String path) {
        String[] strings = path.split("/");
        ArrayList<String> parts = new ArrayList<>();
        for (String string : strings) {
            if (!string.equals(""))
                parts.add(string);
        }
        return parts;
    }

}
