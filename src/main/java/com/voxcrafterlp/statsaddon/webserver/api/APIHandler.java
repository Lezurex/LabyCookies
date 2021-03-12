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
        if (actionHandlers.size() == 0) {
            initActionHandlers();
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody()));
        int c = 0;
        StringBuilder request = new StringBuilder();
        while ((c = br.read()) != -1) {
            request.append((char) c);
        }

        String path = httpExchange.getRequestURI().getPath();
        ArrayList<String> pathParts = WebUtils.getPathParts(path);

        String entryPoint = pathParts.get(1);
        ActionHandler actionHandler;
        String response = "";
        int httpCode = 200;
        try {
            actionHandler = actionHandlers.get(entryPoint.toLowerCase(Locale.ROOT));
            JsonObject jsonObject = new JsonParser().parse(request.toString()).getAsJsonObject();
            response = actionHandler.handle(pathParts, jsonObject);
        } catch (NullPointerException exception) {
            exception.printStackTrace();
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

        OutputStream os = httpExchange.getResponseBody();

        httpExchange.getResponseHeaders().put("Content-Type", Collections.singletonList("application/json"));
        httpExchange.sendResponseHeaders(httpCode, response.getBytes(StandardCharsets.UTF_8).length);

        os.write(response.getBytes());
        os.close();
    }

    private void initActionHandlers() {
        actionHandlers.put("stats", new StatsHandler());
    }
}
