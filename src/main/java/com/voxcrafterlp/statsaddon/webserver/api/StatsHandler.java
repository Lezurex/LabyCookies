package com.voxcrafterlp.statsaddon.webserver.api;

import com.google.common.collect.Lists;
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

        // Remove dummy date before testing on GommeHDnet
        playerStatsList = Lists.newCopyOnWriteArrayList();

        playerStatsList.add(new PlayerStats("Lezurex_", true, false, 50, 50));
        playerStatsList.add(new PlayerStats("EinTigger", true, true, 5, 99));
        playerStatsList.add(new PlayerStats("VoxCrafter_LP", true, true, 42, 69));


        for (PlayerStats playerStats : playerStatsList) {
            data.put(playerStats.toJSONObject());
        }

        return response.toString();
    }
}
