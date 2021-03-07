package com.voxcrafterlp.statsaddon.webserver.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.voxcrafterlp.statsaddon.webserver.Resource;
import com.voxcrafterlp.statsaddon.webserver.WebUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class APIHandler implements HttpHandler {

    public static Map<String, ActionHandler> actionHandlers;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        if (actionHandlers == null) {
            initActionHandlers();
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody()));
        int c = 0;
        StringBuilder request = new StringBuilder();
        while ((c = br.read()) != -1) {
            request.append((char) c);
        }

        String path = httpExchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");

        String entryPoint = pathParts[1];
        ActionHandler actionHandler;
        String response = "";
        try {
            actionHandler = actionHandlers.get(entryPoint.toLowerCase(Locale.ROOT));
            JSONObject jsonObject = new JSONObject(request.toString());
            response = actionHandler.handle(pathParts, jsonObject);
        } catch (NullPointerException exception) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("data", new JSONArray());
            jsonObject.getJSONArray("data").put(new JSONObject().put("status", "error").put("error", "Requested API endpoint dooes not exist"));
        } catch (JSONException exception) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("data", new JSONArray());
            jsonObject.getJSONArray("data").put(new JSONObject().put("status", "error").put("error", "Malformed JSON"));
        }

        OutputStream os = httpExchange.getResponseBody();

        httpExchange.getResponseHeaders().put("Content-Type", Collections.singletonList("application/json"));
        httpExchange.sendResponseHeaders(404, response.getBytes(StandardCharsets.UTF_8).length);

        os.close();
    }

    private void initActionHandlers() {
        actionHandlers.put("stats", new StatsHandler());
    }
}
