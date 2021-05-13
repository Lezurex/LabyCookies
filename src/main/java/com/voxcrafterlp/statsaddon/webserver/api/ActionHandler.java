package com.voxcrafterlp.statsaddon.webserver.api;

import com.google.gson.JsonObject;
import java.util.List;

public interface ActionHandler {

    /**
     * Handles a request for a specific action.
     * @param pathParts {@link List} with each single part of the web path
     * @param body The root object as a {@link JsonObject} of the request
     * @return Response which should be sent back
     */
    public String handle(List<String> pathParts, JsonObject body);

}
