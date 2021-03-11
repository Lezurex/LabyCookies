package com.voxcrafterlp.statsaddon.webserver.api;

import org.json.JSONObject;

import java.util.ArrayList;

public interface ActionHandler {

    public String handle(ArrayList<String> pathParts, JSONObject body);

}
