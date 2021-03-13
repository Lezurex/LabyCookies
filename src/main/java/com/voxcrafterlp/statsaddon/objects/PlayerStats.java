package com.voxcrafterlp.statsaddon.objects;

import com.google.gson.JsonObject;
import com.voxcrafterlp.statsaddon.StatsAddon;
import com.voxcrafterlp.statsaddon.utils.nickchecker.NickChecker;
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
    private NickChecker nickChecker;
    private boolean checked, warned, statsHidden;
    private int rank, playedGames, wins;
    private double winRate, nickProbability;

    public PlayerStats(NetworkPlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
        this.checked = false;
        this.warned = false;
        this.rank = -1;
        this.winRate = -1.0;
        this.nickProbability = 0;
        this.playerName = playerInfo.getGameProfile().getName();
        this.nickChecker = new NickChecker(this);

        StatsAddon.getInstance().getStatsChecker().addToQueue(this);
    }

    public PlayerStats(String playerName, boolean checked, boolean warned, int rank, double winRate) {
        this.playerName = playerName;
        this.checked = checked;
        this.warned = warned;
        this.rank = rank;
        this.winRate = winRate;
        this.playerInfo = null;
    }

    public void performStatsCheck() {
        if(StatsAddon.getInstance().getCurrentGamemode() == null) return;
        if(!playerInfo.getPlayerTeam().getColorSuffix().toLowerCase()
                .replace("i", "y")
                .replace("á", "a")
                .contains("party")) {
            if(!playerInfo.getGameProfile().getName().equals(Minecraft.getMinecraft().thePlayer.getGameProfile().getName())) {
                if(!this.checked) {
                    Minecraft.getMinecraft().thePlayer.sendChatMessage("/" + this.getStatsCommand(playerInfo.getGameProfile().getName()));
                    this.checked = true;
                    try {
                        Thread.sleep(StatsAddon.getInstance().getCooldown());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void performStatsAnalysis(AlertType type) {
        if(this.rank < StatsAddon.getInstance().getRankWarnLevel() && type == AlertType.RANK) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
            if(!this.playerName.equals(Minecraft.getMinecraft().thePlayer.getGameProfile().getName())) {
                this.sendAlert(AlertType.RANK);
                this.warned = true;
            }
        }
        if(this.winRate > (double) StatsAddon.getInstance().getWinrateWarnLevel() && type == AlertType.WINRATE) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
            if(!this.playerName.equals(Minecraft.getMinecraft().thePlayer.getGameProfile().getName())) {
                this.sendAlert(AlertType.WINRATE);
                this.warned = true;
            }
        }
    }

    public void performNickCheck() {
        new Thread(() -> {
            try {
                Thread.sleep(200); //Skin download delay
                this.nickProbability = this.nickChecker.checkPlayer();
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }).start();
    }

    public void sendAlert(AlertType type) {
        if(this.warned) return;

        if(type == AlertType.RANK) {
            LabyMod.getInstance().displayMessageInChat(StatsAddon.getInstance().getGamemodePrefix() + "\u00A74Achtung! \u00A77Potentiell gef\u00E4hrlicher Gegner\u00A77!");
            LabyMod.getInstance().displayMessageInChat(StatsAddon.getInstance().getGamemodePrefix() + "\u00A77Platz \u00A7e#" + this.rank + " \u00A77Name\u00A78: \u00A7c" + this.playerName);
        } else {
            LabyMod.getInstance().displayMessageInChat(StatsAddon.getInstance().getGamemodePrefix() + "\u00A74Achtung! \u00A77Potentiell gef\u00E4hrlicher Gegner\u00A77!");
            LabyMod.getInstance().displayMessageInChat(StatsAddon.getInstance().getGamemodePrefix() + "\u00A77Winrate\u00A78: \u00A7e" + this.winRate + "% \u00A77Name\u00A78: \u00A7c" + this.playerName);
        }

        if(StatsAddon.getInstance().isAlertEnabled()) {
            LabyMod.getInstance().notifyMessageProfile(this.playerInfo.getGameProfile(),
                    (type == AlertType.RANK) ? "Platz #" + this.rank: (int) this.winRate + "er Winrate");

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

        int days = Integer.parseInt(statsType.toLowerCase().replace(" ", "").replace("tage", "")
                .replace("stats", ""));

        if(days == 30) return "stats " + playerName;
        else return "statsd " + days + " " + playerName;
    }

    public JsonObject toJSONObject() {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", this.playerName);
        jsonObject.addProperty("warned", this.warned);
        jsonObject.addProperty("rank", this.rank);
        jsonObject.addProperty("winRate", this.winRate);
        jsonObject.addProperty("statsHidden", this.statsHidden);
        jsonObject.addProperty("nickProbability", this.nickProbability);
        jsonObject.addProperty("prefix", this.playerInfo.getPlayerTeam().getColorPrefix());
        return jsonObject;
    }

    public enum AlertType {

        RANK,
        WINRATE

    }

}
