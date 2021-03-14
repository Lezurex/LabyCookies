package com.voxcrafterlp.statsaddon.objects;

import com.google.gson.JsonObject;
import com.voxcrafterlp.statsaddon.StatsAddon;
import com.voxcrafterlp.statsaddon.utils.nickchecker.NickChecker;
import lombok.Getter;
import lombok.Setter;
import net.labymod.main.LabyMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import java.text.DecimalFormat;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 30.01.2021
 * Time: 21:49
 * Project: LabyCookies
 */

@Getter
@Setter
public class PlayerStats {

    private final NetworkPlayerInfo playerInfo;
    private final String playerName;
    private NickChecker nickChecker;
    private boolean checked, warned, statsHidden;
    private int rank, playedGames, wins, cookies;
    private double winRate, nickProbability;
    private StatsType statsType;

    public PlayerStats(NetworkPlayerInfo playerInfo, StatsType statsType) {
        this.playerInfo = playerInfo;
        this.checked = false;
        this.warned = false;
        this.rank = -1;
        this.winRate = -1.0;
        this.playedGames = -1;
        this.wins = -1;
        this.cookies = -1;
        this.nickProbability = 0;
        if (statsType == null) this.statsType = StatsAddon.getInstance().getStatsType();
        else this.statsType = statsType;
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
        if (StatsAddon.getInstance().getCurrentGamemode() == null) return;
        if (!playerInfo.getPlayerTeam().getSuffix().toLowerCase()
                .replace("i", "y")
                .replace("á", "a")
                .contains("party")) {

            if (!this.checked) {
                StatsAddon.getInstance().getMinecraftThePlayer().sendChatMessage(this.statsType.getCommandName() + " " + this.playerName);
                this.checked = true;
                try {
                    Thread.sleep(StatsAddon.getInstance().getCooldown());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * Checks if the rank or winrate are higher than the value set in the settings
     */
    public void performStatsAnalysis() {
        String username = StatsAddon.getInstance().getMinecraftThePlayer().getGameProfile().getName();
        if (username.length() >= this.playerName.length()) {
            String substring = this.playerName.substring(0, username.length());
            if (substring.equals(username))
                return;
        }

        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (this.wins != -1) {
            boolean firstMessageSent = false;

            if (this.rank < StatsAddon.getInstance().getRankWarnLevel() && this.rank > 0) {
                LabyMod.getInstance().displayMessageInChat(StatsAddon.getInstance().getGamemodePrefix() + "\u00A74Achtung! \u00A77Potentiell gef\u00E4hrlicher Gegner\u00A77!");
                firstMessageSent = true;
                this.sendAlert(AlertType.RANK);
            }

            if (this.winRate > (double) StatsAddon.getInstance().getWinrateWarnLevel()) {
                if (!firstMessageSent) {
                    LabyMod.getInstance().displayMessageInChat(StatsAddon.getInstance().getGamemodePrefix() + "\u00A74Achtung! \u00A77Potentiell gef\u00E4hrlicher Gegner\u00A77!");
                    firstMessageSent = true;
                }
                this.sendAlert(AlertType.WINRATE);
            }

            final double cookiesPerGame = this.getCookiesPerGame();
            if (cookiesPerGame > 0) {
                if (cookiesPerGame > (double) StatsAddon.getInstance().getCookiesPerGameWarnLevel()) {
                    if (!firstMessageSent) {
                        LabyMod.getInstance().displayMessageInChat(StatsAddon.getInstance().getGamemodePrefix() + "\u00A74Achtung! \u00A77Potentiell gef\u00E4hrlicher Gegner\u00A77!");
                        firstMessageSent = true;
                    }
                    this.sendAlert(AlertType.COOKIESPERGAME);
                }
            }
            // If a warn message has been sent, player has been warned at least once.
            this.warned = firstMessageSent;
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
     *
     * @param type Type of the alert
     */
    public void sendAlert(AlertType type) {
        if (this.warned) return;

        switch (type) {
            case RANK:
                LabyMod.getInstance().displayMessageInChat(StatsAddon.getInstance().getGamemodePrefix() + "\u00A77Platz \u00A7e#" + this.rank + " \u00A77Name\u00A78: \u00A7c" + this.playerName);
                break;
            case WINRATE:
                LabyMod.getInstance().displayMessageInChat(StatsAddon.getInstance().getGamemodePrefix() + "\u00A77Winrate\u00A78: \u00A7e" + this.winRate + "% \u00A77Name\u00A78: \u00A7c" + this.playerName);
                break;
            case COOKIESPERGAME:
                LabyMod.getInstance().displayMessageInChat(StatsAddon.getInstance().getGamemodePrefix() + "\u00A77Cookies/Spiel\u00A78: \u00A7e" + this.getCookiesPerGame() + " \u00A77Name\u00A78: \u00A7c" + this.playerName);
                break;
        }

        if (StatsAddon.getInstance().isAlertEnabled()) {
            switch (type) {
                case RANK:
                    LabyMod.getInstance().notifyMessageProfile(this.playerInfo.getGameProfile(), "Platz #" + this.rank);
                    break;
                case WINRATE:
                    LabyMod.getInstance().notifyMessageProfile(this.playerInfo.getGameProfile(), (int) this.winRate + "er Winrate");
                    break;
                case COOKIESPERGAME:
                    LabyMod.getInstance().notifyMessageProfile(this.playerInfo.getGameProfile(), (int) this.getCookiesPerGame() + " Cookies/Spiel");
                    break;
            }


            new Thread(() -> {
                for (int i = 0; i < 5; i++) {
                    StatsAddon.getInstance().getMinecraftThePlayer().playSound(new SoundEvent(new ResourceLocation("note.pling")), 1, 1);
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
        final String statsType = StatsAddon.getInstance().getStatsType().getCommandName();
        if (statsType.equals("statsall")) return "statsall " + playerName;

        int days = Integer.parseInt(statsType.toLowerCase().replace(" ", "").replace("tage", "")
                .replace("stats", ""));

        return ((days == 30) ? "stats " + playerName : "statsd " + days + " " + playerName);
    }

    public double getWinRate() {
        if (this.winRate != -1) return this.winRate;
        if (this.wins == -1 || this.playedGames == -1) return -1;
        if (this.playedGames == 0) return 0;

        double winRate = ((double) this.wins / (double) this.playedGames) * 100;
        return Double.parseDouble(new DecimalFormat("###.##").format(winRate));
    }

    public double getCookiesPerGame() {
        if (StatsAddon.getInstance().getCurrentGamemode() != null) {
            if (StatsAddon.getInstance().getCurrentGamemode().equals("Cookies")) {
                if (playedGames > 0) {
                    double cookiesPerGame = (double) this.cookies / (double) this.playedGames;
                    DecimalFormat df;
                    if (Double.toString(cookiesPerGame).contains(",")) df = new DecimalFormat("###,##");
                    else df = new DecimalFormat("###.##");
                    return Double.parseDouble(df.format(cookiesPerGame).replace(",", "."));
                }
            }
        }
        return -1;
    }

    /**
     * Converts the most important information into a {@link JsonObject}
     *
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
        jsonObject.addProperty("cookies", this.cookies);
        jsonObject.addProperty("cookiesPerGame", getCookiesPerGame());
        jsonObject.addProperty("statsHidden", this.statsHidden);
        jsonObject.addProperty("nickProbability", this.nickProbability);
        jsonObject.addProperty("prefix", this.playerInfo.getPlayerTeam().getPrefix());
        jsonObject.addProperty("statsType", this.statsType.getConfigName());
        return jsonObject;
    }

    public enum AlertType {

        RANK,
        WINRATE,
        COOKIESPERGAME,

    }

}
