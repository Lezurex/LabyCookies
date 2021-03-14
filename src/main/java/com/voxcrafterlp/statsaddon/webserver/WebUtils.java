package com.voxcrafterlp.statsaddon.webserver;

import com.google.common.collect.Lists;

import javax.activation.MimetypesFileTypeMap;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WebUtils {

    /**
     * Loads a file form the resource folder and gets its MIME type.
     *
     * @param path Valid path starting in resources folder
     * @return {@link Resource} object with useful information for web usage
     */
    public Resource getResource(String path) {
        final ClassLoader classLoader = getClass().getClassLoader();
        final InputStream stream = classLoader.getResourceAsStream(path);

        if (stream == null) {
            throw new IllegalArgumentException("File \"" + path + "\" was not found!");
        } else {
            if (path.endsWith(".js")) {
                return new Resource(stream, "text/javascript");
            } else if (path.endsWith(".css")) {
                return new Resource(stream, "text/css");
            }

            return new Resource(stream, new MimetypesFileTypeMap().getContentType(path));
        }
    }

    /**
     * Splits a web path into single parts and removes empty values.
     *
     * @param path A web path to be split
     * @return ArrayList of parts without empty values
     */
    public static List<String> getPathParts(String path) {
        final String[] strings = path.split("/");
        final List<String> parts = Lists.newCopyOnWriteArrayList();

        Arrays.stream(strings).forEach(string -> {
            if(!string.equals("")) parts.add(string);
        });

        return parts;
    }

}
