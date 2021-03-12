package com.voxcrafterlp.statsaddon.webserver;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

public class WebUtils {

    /**
     * Loads a file form the resource folder and gets its MIME type.
     *
     * @param path Valid path starting in resources folder
     * @return {@link Resource} object with useful information for web usage
     */
    public Resource getResource(String path) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream stream = classLoader.getResourceAsStream(path);
        if (stream == null) {
            throw new IllegalArgumentException("File \"" + path + "\" was not found!");
        } else {

            if (path.endsWith(".js")) {
                return new Resource(stream, "text/javascript");
            } else if (path.endsWith(".css")) {
                return new Resource(stream, "text/css");
            }
            MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
            return new Resource(stream, fileTypeMap.getContentType(path));
        }
    }

    /**
     * Splits a web path into single parts and removes empty values.
     *
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
