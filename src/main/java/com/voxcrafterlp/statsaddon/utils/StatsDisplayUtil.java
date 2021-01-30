package com.voxcrafterlp.statsaddon.utils;

import com.voxcrafterlp.statsaddon.StatsAddon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;

import java.util.List;

/**
 * This file was created by VoxCrafter_LP & Lezurex!
 * Date: 06.09.2020
 * Time: 16:31
 * For Project: Labymod Stats Addon
 */

public class StatsDisplayUtil {

    public void displayStats(List<NetworkPlayerInfo> playerInfos) {
        new Thread(() -> {
            for(NetworkPlayerInfo playerInfo: playerInfos) {
                if (StatsAddon.getInstance().getCurrentGamemode() != null) {
                    if (!playerInfo.getPlayerTeam().getColorSuffix().toLowerCase().replace("i", "y").replace("á", "a").contains("party")) {
                        if (playerInfo.getGameProfile() != Minecraft.getMinecraft().thePlayer.getGameProfile()) {
                            if (!StatsAddon.getInstance().checkedPlayers.contains(playerInfo)) {
                                Minecraft.getMinecraft().thePlayer.sendChatMessage("/" + this.getCommand(playerInfo.getDisplayName().toString()));
                                playerInfos.remove(playerInfo);
                                StatsAddon.getInstance().checkedPlayers.add(playerInfo);
                                try {
                                    Thread.sleep(StatsAddon.getInstance().cooldown);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                playerInfos.remove(playerInfo);
                            }
                        }
                    }
                } else
                    break;
            }
        }).start();
    }

    private String getCommand(String playerName) {
        final String statsType = StatsAddon.getInstance().getStatsType().toLowerCase();
        if(statsType.equals("statsall")) return "statsall " + playerName;

        int days = Integer.parseInt(statsType.replace(" ", "").replace("days", "")
                .replace("stats", ""));

        if(days == 30) return "stats " + playerName;
        else return "statsd " + days + " " + playerName;
    }

}
