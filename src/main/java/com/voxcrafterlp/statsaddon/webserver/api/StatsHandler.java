package com.voxcrafterlp.statsaddon.webserver.api;

import com.voxcrafterlp.statsaddon.StatsAddon;
import com.voxcrafterlp.statsaddon.objects.PlayerStats;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class StatsHandler implements ActionHandler {
    @Override
    public String handle(ArrayList<String> pathParts, JSONObject body) {
        JSONObject response = new JSONObject();
        JSONArray data = new JSONArray();
        response.put("data", data);

        List<PlayerStats> playerStatsList = StatsAddon.getInstance().getStatsChecker().getCheckedPlayers();
        for (PlayerStats playerStats : playerStatsList) {
            data.put(playerStats.toJSONObject());
        }

        return response.toString();
    }
}
