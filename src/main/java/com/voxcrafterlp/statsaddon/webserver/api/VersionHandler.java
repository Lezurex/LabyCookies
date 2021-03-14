package com.voxcrafterlp.statsaddon.webserver.api;

import com.google.gson.JsonObject;
import com.voxcrafterlp.statsaddon.StatsAddon;

import java.util.List;

public class VersionHandler implements ActionHandler {
    @Override
    public String handle(List<String> pathParts, JsonObject body) {
        return "{\"data\":[{\"version\":\"" + StatsAddon.getInstance().getCurrentVersion() + "\"}]}";
    }
}
