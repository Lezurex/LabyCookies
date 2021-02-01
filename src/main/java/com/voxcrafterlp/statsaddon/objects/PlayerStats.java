package com.voxcrafterlp.statsaddon.objects;

import com.voxcrafterlp.statsaddon.StatsAddon;
import lombok.Getter;
import lombok.Setter;
import net.labymod.main.LabyMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 30.01.2021
 * Time: 21:49
 * Project: LabyCookies
 */

@Getter @Setter
public class PlayerStats {

    private final NetworkPlayerInfo playerInfo;
    private final String playerName;
    private boolean checked;
    private int rank;
    private double winRate;

    public PlayerStats(NetworkPlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
        this.checked = false;
        this.rank = 0;
        this.winRate = 0.0;
        this.playerName = playerInfo.getGameProfile().getName();

        StatsAddon.getInstance().getStatsChecker().addToQueue(this);
    }

    public void performStatsCheck() {
        if(StatsAddon.getInstance().getCurrentGamemode() != null) {
            if(!playerInfo.getPlayerTeam().getColorSuffix().toLowerCase()
                    .replace("i", "y")
                    .replace("á", "a")
                    .contains("party")) {
                if(!playerInfo.getGameProfile().getName().equals(Minecraft.getMinecraft().thePlayer.getGameProfile().getName())) {
                    if(!this.checked) {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/" + this.getStatsCommand(playerInfo.getGameProfile().getName()));
                        this.checked = true;
                        try {
                            Thread.sleep(StatsAddon.getInstance().cooldown);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public void performStatsAnalysis() {
        if(this.rank < StatsAddon.getInstance().warnLevel) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
            if(!this.playerName.equals(Minecraft.getMinecraft().thePlayer.getGameProfile().getName()))
                this.sendRankingAlert();
        }
    }

    public void sendRankingAlert() {
        LabyMod.getInstance().displayMessageInChat(StatsAddon.getInstance().getGamemodePrefix() + "\u00A74Achtung! \u00A77Potentiell gef\u00E4hrlicher Gegner\u00A77!");
        LabyMod.getInstance().displayMessageInChat(StatsAddon.getInstance().getGamemodePrefix() + "\u00A77Platz \u00A7e#" + this.rank + " \u00A77Name\u00A78: \u00A7c" + this.playerName);

        if(StatsAddon.getInstance().alertEnabled) {
            new Thread(() -> {
                for(int i = 0; i < 5; i++) {
                    Minecraft.getMinecraft().thePlayer.playSound("note.pling", 1, 1);
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException exception) {
                        exception.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public void sendWinrateAlert() {
        LabyMod.getInstance().displayMessageInChat(StatsAddon.getInstance().getGamemodePrefix() + "\u00A74Achtung! \u00A77Potentiell gef\u00E4hrlicher Gegner\u00A77!");
        LabyMod.getInstance().displayMessageInChat(StatsAddon.getInstance().getGamemodePrefix() + "\u00A77Winrate \u00A7e#" + this.winRate + " \u00A77Name\u00A78: \u00A7c" + this.playerName);

        if(StatsAddon.getInstance().alertEnabled) {
            new Thread(() -> {
                for(int i = 0; i < 5; i++) {
                    Minecraft.getMinecraft().thePlayer.playSound("note.pling", 1, 1);
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException exception) {
                        exception.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private String getStatsCommand(String playerName) {
        final String statsType = StatsAddon.getInstance().getStatsType().toLowerCase();
        if(statsType.equals("statsall")) return "statsall " + playerName;

        int days = Integer.parseInt(statsType.replace(" ", "").replace("days", "")
                .replace("stats", ""));

        if(days == 30) return "stats " + playerName;
        else return "statsd " + days + " " + playerName;
    }

}
