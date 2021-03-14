package com.voxcrafterlp.statsaddon.webserver.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.voxcrafterlp.statsaddon.webserver.WebUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class APIHandler implements HttpHandler {

    public static Map<String, ActionHandler> actionHandlers = new HashMap<>();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        if(actionHandlers.size() == 0)
            initActionHandlers();

        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody()));
        
        int c = 0;
        final StringBuilder request = new StringBuilder();
        while ((c = bufferedReader.read()) != -1) {
            request.append((char) c);
        }

        final String path = httpExchange.getRequestURI().getPath();
        final List<String> pathParts = WebUtils.getPathParts(path);

        final String entryPoint = pathParts.get(1);
        ActionHandler actionHandler;
        String response = "";
        int httpCode = 200;

        try {
            actionHandler = actionHandlers.get(entryPoint.toLowerCase(Locale.ROOT));
            JsonObject jsonObject = new JsonParser().parse(request.toString()).getAsJsonObject();
            response = actionHandler.handle(pathParts, jsonObject);
        } catch (NullPointerException exception) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("data", new JsonArray());
            JsonObject data = new JsonObject();
            data.addProperty("status", "error");
            data.addProperty("error", "Requested API endpoint does not exist!");
            jsonObject.getAsJsonArray("data").add(data);
            response = jsonObject.toString();
            httpCode = 404;
        } catch (JsonParseException exception) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("data", new JsonArray());
            JsonObject data = new JsonObject();
            data.addProperty("status", "error");
            data.addProperty("error", "Malformed JSON");
            jsonObject.getAsJsonArray("data").add(data);
            response = jsonObject.toString();
            httpCode = 400;
        }

        final OutputStream outputStream = httpExchange.getResponseBody();

        if (System.getProperty("file.encoding").equals("UTF-8"))
            httpExchange.getResponseHeaders().put("Content-Type", Collections.singletonList("application/json;charset=UTF-8"));
        else
            httpExchange.getResponseHeaders().put("Content-Type", Collections.singletonList("application/json;charset=ISO-8859-1"));
        httpExchange.sendResponseHeaders(httpCode, 0);

        outputStream.write(response.getBytes());
        outputStream.close();
    }

    private void initActionHandlers() {
        actionHandlers.put("stats", new StatsHandler());
        actionHandlers.put("ip", new IPHandler());
        actionHandlers.put("version", new VersionHandler());
    }
}
