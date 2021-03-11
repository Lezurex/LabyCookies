package com.voxcrafterlp.statsaddon.webserver.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.voxcrafterlp.statsaddon.StatsAddon;
import com.voxcrafterlp.statsaddon.objects.PlayerStats;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StatsHandler implements ActionHandler {
    @Override
    public String handle(ArrayList<String> pathParts, JsonObject body) {
        JsonObject response = new JsonObject();
        JsonArray data = new JsonArray();
        response.add("data", data);

        Map<String, PlayerStats> playerStatsList = StatsAddon.getInstance().getLoadedPlayerStats();

        // Remove dummy data before testing on GommeHDnet
//        playerStatsList = Lists.newCopyOnWriteArrayList();
//
//        playerStatsList.add(new PlayerStats("Lezurex_", true, false, 50, 50));
//        playerStatsList.add(new PlayerStats("EinTigger", true, true, 5, 99));
//        playerStatsList.add(new PlayerStats("VoxCrafter_LP", true, true, 42, 69));
        // dummy data end

        playerStatsList.forEach((string, playerStats) -> data.add(playerStats.toJSONObject()));

        return response.toString();
    }
}
