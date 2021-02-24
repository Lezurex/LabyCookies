package com.voxcrafterlp.statsaddon.webserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

public class MainHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();

        if (path.endsWith("/")) {
            path = path + "index.html";
        }

        Resource resource = new WebUtils().getResourceAsString("webapp" + path);

        String response = resource.getContent();
        if (response == null) {
            httpExchange.getResponseHeaders().put("Content-Type", Collections.singletonList("text/html"));
            httpExchange.sendResponseHeaders(404, "<h1>404 Resource not found</h1>".getBytes(StandardCharsets.UTF_8).length);
        }
        httpExchange.getResponseHeaders().put("Content-Type", Collections.singletonList(resource.getMime()));
        httpExchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes(StandardCharsets.UTF_8));
        os.close();
    }
}
