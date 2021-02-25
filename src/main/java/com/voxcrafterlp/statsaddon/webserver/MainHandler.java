package com.voxcrafterlp.statsaddon.webserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;

public class MainHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();

        if (path.endsWith("/")) {
            path = path + "index.html";
        }

        Resource resource = null;

        try {
            resource = new WebUtils().getResource("webapp" + path);
        } catch (IllegalArgumentException exception) {

        }

        OutputStream os = httpExchange.getResponseBody();

        if (resource == null) {
            String response = "<h1>404 Resource not found</h1>";
            httpExchange.getResponseHeaders().put("Content-Type", Collections.singletonList("text/html"));
            httpExchange.sendResponseHeaders(404, response.getBytes(StandardCharsets.UTF_8).length);
            os.write(response.getBytes());
        } else {
            httpExchange.getResponseHeaders().put("Content-Type", Collections.singletonList(resource.getMime()));
            httpExchange.sendResponseHeaders(200, resource.getFile().length());
            Files.copy(resource.getFile().toPath(), os);
        }
        os.close();
    }
}
