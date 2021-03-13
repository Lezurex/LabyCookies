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
        this.playedGames = -1;
        this.wins = -1;
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

    /**
     * Generates the stats command and sends it to the server
     */
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

    /**
     * Checks if the rank or winrate are higher than the value set in the settings
     * @param type Type of the stats that should be checked
     */
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

    /**
     * Starts the nick check after a 200ms delay
     */
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

    /**
     * Sends a chat and achievement alert to the player
     * If enabled, notifies the player with a 'pling' sound
     * @param type Type of the alert
     */
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

    /**
     * Generates a stats command based on the player's settings
     *
     * @param playerName Name of the player whose stats should be checked
     * @return {@link String} Stats command without the slash ('/')
     */
    private String getStatsCommand(String playerName) {
        final String statsType = StatsAddon.getInstance().getStatsType().toLowerCase();
        if(statsType.equals("statsall")) return "statsall " + playerName;

        int days = Integer.parseInt(statsType.toLowerCase().replace(" ", "").replace("tage", "")
                .replace("stats", ""));

        return ((days == 30) ? "stats " + playerName : "statsd " + days + " " + playerName);
    }

    public double getWinRate() {
        if (this.winRate == -1) {
            if (this.wins != -1 && this.playedGames != -1) {
                return Math.round((double) (this.wins / this.playedGames) * 100);
            }
            else {
                return -1;
            }
        } else {
            return this.winRate;
        }
    }

    /**
     * Converts the most important information into a {@link JsonObject}
     * @return {@link JsonObject} object with all the necessary information for the website
     */
    public JsonObject toJSONObject() {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", this.playerName);
        jsonObject.addProperty("warned", this.warned);
        jsonObject.addProperty("rank", this.rank);
        jsonObject.addProperty("winRate", getWinRate());
        jsonObject.addProperty("playedGames", this.playedGames);
        jsonObject.addProperty("wins", this.wins);
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
