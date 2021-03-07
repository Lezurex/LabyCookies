package com.voxcrafterlp.statsaddon.webserver.api;

import org.json.JSONObject;

public interface ActionHandler {

    public String handle(String[] pathParts, JSONObject body);

}
