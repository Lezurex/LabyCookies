package com.voxcrafterlp.statsaddon.webserver.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.voxcrafterlp.statsaddon.StatsAddon;
import com.voxcrafterlp.statsaddon.objects.PlayerStats;
import com.voxcrafterlp.statsaddon.objects.StatsType;
import com.voxcrafterlp.statsaddon.utils.compatibility.CompatibilityLayer;
import com.voxcrafterlp.statsaddon.utils.compatibility.NetworkPlayerInfo;

import java.util.List;
import java.util.Map;

public class UserStatsHandler implements ActionHandler {

    @Override
    public String handle(List<String> pathParts, JsonObject body) {
        if (StatsAddon.getInstance().getCurrentGamemode() == null) {
            return "{\"data\":[]}";
        }
        Map<String, PlayerStats> playerStatsList = StatsAddon.getInstance().getLoadedPlayerStats();
        String username = CompatibilityLayer.getMinecraftThePlayer().getGameProfile().getName();
        if (!playerStatsList.containsKey(username + "%30D")) {
            System.out.println("Doesnt contain 30d");
            PlayerStats playerStats = new PlayerStats((NetworkPlayerInfo) CompatibilityLayer
                    .getMinecraftThePlayerSendQueue()
                    .getPlayerInfo(CompatibilityLayer.getMinecraftThePlayer().getGameProfile()
                            .getId()), StatsType.STATS30);
            playerStatsList.put(username + "%30D", playerStats);
        }
        if (!playerStatsList.containsKey(username + "%ALL")) {
            System.out.println("Doesnt contain all");
            PlayerStats playerStats = new PlayerStats((NetworkPlayerInfo) CompatibilityLayer
                    .getMinecraftThePlayerSendQueue()
                    .getPlayerInfo(CompatibilityLayer.getMinecraftThePlayer().getGameProfile()
                            .getId()), StatsType.STATSALL);
            playerStatsList.put(username + "%ALL", playerStats);
        }
        if (!playerStatsList.containsKey(username + "%1D")) {
            System.out.println("Doesnt contain 1d");
            PlayerStats playerStats = new PlayerStats((NetworkPlayerInfo) CompatibilityLayer
                    .getMinecraftThePlayerSendQueue()
                    .getPlayerInfo(CompatibilityLayer.getMinecraftThePlayer().getGameProfile()
                            .getId()), StatsType.STATS1);
            playerStatsList.put(username + "%1D", playerStats);
        }

        JsonObject root = new JsonObject();
        JsonArray array = new JsonArray();
        root.add("data", array);
        array.add(playerStatsList.get(username + "%30D").toJSONObject());
        array.add(playerStatsList.get(username + "%ALL").toJSONObject());
        array.add(playerStatsList.get(username + "%1D").toJSONObject());
        return root.toString();
    }
}
