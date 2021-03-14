package com.voxcrafterlp.statsaddon.webserver.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.voxcrafterlp.statsaddon.StatsAddon;
import com.voxcrafterlp.statsaddon.objects.PlayerStats;
import com.voxcrafterlp.statsaddon.objects.Team;
import net.minecraft.client.Minecraft;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatsHandler implements ActionHandler {

    @Override
    public String handle(List<String> pathParts, JsonObject body) {
        final JsonObject response = new JsonObject();
        final JsonArray data = new JsonArray();

        response.add("data", data);

        final Map<String, PlayerStats> playerStatsList = StatsAddon.getInstance().getLoadedPlayerStats();
        final Map<String, Team> teams = new HashMap<>();

        playerStatsList.forEach((string, playerStats) -> {
            if (!playerStats.getPlayerName().equals(StatsAddon.getInstance().getMinecraftThePlayer().getGameProfile().getName())) {
                final String teamPrefix = playerStats.getPlayerInfo().getPlayerTeam().getColorPrefix();
                if (teams.containsKey(teamPrefix)) {
                    teams.get(teamPrefix).addPlayerStats(playerStats);
                } else {
                    Team team = new Team(teamPrefix);
                    team.addPlayerStats(playerStats);
                    teams.put(teamPrefix, team);
                }
            }
        });


        teams.forEach((prefix, team) -> team.getPlayerStats().forEach(playerStats -> data.add(playerStats.toJSONObject())));

        return response.toString();
    }
}
