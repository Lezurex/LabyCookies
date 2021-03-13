package com.voxcrafterlp.statsaddon.webserver.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class IPHandler implements ActionHandler {
    @Override
    public String handle(ArrayList<String> pathParts, JsonObject body) {
        String address = "error";
        try {
            address = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        JsonObject response = new JsonObject();
        JsonArray array = new JsonArray();
        JsonObject data = new JsonObject();
        data.addProperty("ip", address);
        array.add(data);
        response.add("data", array);
        return response.toString();
    }
}
