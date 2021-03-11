package com.voxcrafterlp.statsaddon.webserver.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        ArrayList<String> pathParts = getPathParts(path);

        String entryPoint = pathParts.get(1);
        ActionHandler actionHandler;
        String response = "";
        int httpCode = 200;
        try {
            actionHandler = actionHandlers.get(entryPoint.toLowerCase(Locale.ROOT));
            JSONObject jsonObject = new JSONObject(request.toString());
            response = actionHandler.handle(pathParts, jsonObject);
        } catch (NullPointerException exception) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("data", new JSONArray());
            jsonObject.getJSONArray("data").put(new JSONObject().put("status", "error").put("error", "Requested API endpoint does not exist"));
            response = jsonObject.toString();
            httpCode = 404;
        } catch (JSONException exception) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("data", new JSONArray());
            jsonObject.getJSONArray("data").put(new JSONObject().put("status", "error").put("error", "Malformed JSON"));
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

    private ArrayList<String> getPathParts(String path) {
        String[] strings = path.split("/");
        ArrayList<String> parts = new ArrayList<>();
        for (String string : strings) {
            if (!string.equals(""))
                parts.add(string);
        }
        return parts;
    }
}
