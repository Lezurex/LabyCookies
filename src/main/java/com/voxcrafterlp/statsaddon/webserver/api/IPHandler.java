package com.voxcrafterlp.statsaddon.webserver.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.List;

public class IPHandler implements ActionHandler {

    @Override
    public String handle(List<String> pathParts, JsonObject body) {
        String address = "error";

        try {
            address = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        final JsonObject response = new JsonObject();
        final JsonArray array = new JsonArray();
        final JsonObject data = new JsonObject();

        data.addProperty("ip", address);
        array.add(data);
        response.add("data", array);

        return response.toString();
    }
}
