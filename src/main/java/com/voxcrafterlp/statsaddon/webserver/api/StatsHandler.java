package com.voxcrafterlp.statsaddon.webserver.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.voxcrafterlp.statsaddon.StatsAddon;
import com.voxcrafterlp.statsaddon.objects.PlayerStats;
import com.voxcrafterlp.statsaddon.objects.Team;
import com.voxcrafterlp.statsaddon.utils.CompatibilityLayer;
import net.minecraft.client.Minecraft;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatsHandler implements ActionHandler {

    @Override
    public String handle(List<String> pathParts, JsonObject body) {

        if (StatsAddon.getInstance().getCurrentGamemode() == null) {
            return "{\"data\":[]}";
        }

        final JsonObject response = new JsonObject();
        final JsonArray data = new JsonArray();

        response.add("data", data);

        final Map<String, PlayerStats> playerStatsList = StatsAddon.getInstance().getLoadedPlayerStats();
        final Map<String, Team> teams = new HashMap<>();

        playerStatsList.forEach((string, playerStats) -> {
            if (!playerStats.getPlayerName().equals(CompatibilityLayer.getMinecraftThePlayer().getGameProfile().getName())) {
                final String teamPrefix = CompatibilityLayer.playerInfoGetPrefix(playerStats.getPlayerInfo());
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
