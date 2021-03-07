package com.voxcrafterlp.statsaddon.webserver.api;

import org.json.JSONObject;

public class StatsHandler implements ActionHandler {
    @Override
    public String handle(String[] pathParts, JSONObject body) {
        return "{}";
    }
}
