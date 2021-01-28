package com.voxcrafterlp.statsaddon.utils;

import com.voxcrafterlp.statsaddon.StatsAddon;
import com.voxcrafterlp.statsaddon.nickedchecker.ClanChecker;
import com.voxcrafterlp.statsaddon.nickedchecker.NickAlgorithm;
import com.voxcrafterlp.statsaddon.nickedchecker.PremiumChecker;
import net.labymod.main.LabyMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * This file was created by VoxCrafter_LP & Lezurex!
 * Date: 06.09.2020
 * Time: 16:31
 * For Project: Labymod Stats Addon
 */

public class StatsDisplayUtil {

    private ArrayList<NickAlgorithm> nickAlgorithms;

    public StatsDisplayUtil() {

    }

    public void displayStats(List<NetworkPlayerInfo> playerNames, String joinMessage) {
        new Thread(() -> {
            for(NetworkPlayerInfo playerInfo : playerNames) {
                if (!playerInfo.getPlayerTeam().getColorSuffix().toLowerCase().replace("i", "y").replace("á", "a").contains("party")) {
                    if (!playerInfo.getGameProfile().getName().equals(Minecraft.getMinecraft().thePlayer.getGameProfile().getName())) {
                        if (!StatsAddon.getInstance().checkedPlayers.contains(playerInfo)) {
                            Minecraft.getMinecraft().thePlayer.sendChatMessage("/" + this.getCommand(playerInfo.toString()));
                            playerNames.remove(playerInfo);
                            StatsAddon.getInstance().checkedPlayers.add(playerInfo);
                            try {
                                Thread.sleep(StatsAddon.getInstance().cooldown);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            playerNames.remove(playerInfo);
                        }
                    }
                } else {
                    playerNames.remove(playerInfo);
                    StatsAddon.getInstance().checkedPlayers.add(playerInfo);
                }
            }
        }).start();
    }

    public void checkNicked(NetworkPlayerInfo playerInfo, String joinMessage) {
        nickAlgorithms = new ArrayList<>();
        nickAlgorithms.add(new ClanChecker());
        if (joinMessage != null) {
            nickAlgorithms.add(new PremiumChecker());
        }

        int points = 0;
        int max = nickAlgorithms.size();
        for (NickAlgorithm algorithm : nickAlgorithms) {
            System.out.println(algorithm.getClass().getName());
            boolean test = algorithm.performCheck(playerInfo, joinMessage);
            System.out.println("Test: " + test);
            if (test) {
                points++;
            }
        }

        float percentage;

        if (points != 0) {
            percentage = (max / points) * 100;
        } else {
            percentage = 0;
        }

        if (percentage >= 50) {
            LabyMod.getInstance().displayMessageInChat(StatsAddon.getInstance().getPrefix() + "\u00A74Achtung! \u00A77Potentiell genickter Spieler\u00A77!");
            LabyMod.getInstance().displayMessageInChat(StatsAddon.getInstance().getPrefix() + "\u00A77Name\u00A78: \u00A7c" + playerInfo.getGameProfile().getName() +
                    " \u00A77Wahrscheinlichkeit\u00A78: \u00A7c" + percentage + "%" + " \u00A77Punkte\u00A78: \u00A7c" + points);
        }

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
