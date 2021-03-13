package com.voxcrafterlp.statsaddon.webserver.api;

import com.google.gson.JsonObject;
import org.json.JSONObject;

import java.util.ArrayList;

public interface ActionHandler {

    /**
     * Handles a request for a specific action.
     * @param pathParts {@link ArrayList} with each single part of the web path
     * @param body The root object as a {@link JSONObject} of the request
     * @return Response which should be sent back
     */
    public String handle(ArrayList<String> pathParts, JsonObject body);

}
