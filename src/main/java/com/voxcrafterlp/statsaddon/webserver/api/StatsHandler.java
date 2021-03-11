package com.voxcrafterlp.statsaddon.webserver.api;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class StatsHandler implements ActionHandler {
    @Override
    public String handle(ArrayList<String> pathParts, JSONObject body) {
        return "{}";
    }
}
