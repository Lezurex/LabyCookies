package com.voxcrafterlp.statsaddon.webserver.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.voxcrafterlp.statsaddon.StatsAddon;
import com.voxcrafterlp.statsaddon.objects.PlayerStats;
import com.voxcrafterlp.statsaddon.objects.Team;
import net.labymod.main.LabyMod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatsHandler implements ActionHandler {
    @Override
    public String handle(ArrayList<String> pathParts, JsonObject body) {
        JsonObject response = new JsonObject();
        JsonArray data = new JsonArray();
        response.add("data", data);

        Map<String, PlayerStats> playerStatsList = StatsAddon.getInstance().getLoadedPlayerStats();
        Map<String, Team> teams = new HashMap<>();

        playerStatsList.forEach((string, playerStats) -> {
            final String teamPrefix = playerStats.getPlayerInfo().getPlayerTeam().getColorPrefix();
            if (teams.containsKey(teamPrefix)) {
                teams.get(teamPrefix).addPlayerStats(playerStats);
            } else {
                Team team = new Team(teamPrefix);
                team.addPlayerStats(playerStats);
                teams.put(teamPrefix, team);
            }

        });


        teams.forEach((prefix, team) -> {
            for (PlayerStats playerStats : team.getPlayerStats()) {
                data.add(playerStats.toJSONObject());
            }
        });

        return response.toString();
    }
}
