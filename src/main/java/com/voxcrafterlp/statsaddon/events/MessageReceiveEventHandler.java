package com.voxcrafterlp.statsaddon.events;

import com.voxcrafterlp.statsaddon.StatsAddon;
import com.voxcrafterlp.statsaddon.objects.PlayerStats;
import net.labymod.api.events.MessageReceiveEvent;
import net.labymod.main.LabyMod;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StringUtils;

/**
 * This file was created by VoxCrafter_LP and Lezurex!
 * Date: 06.09.2020
 * Time: 16:51
 * For Project: Labymod Stats Addon
 */

public class MessageReceiveEventHandler {

    private String lastPlayerName;

    public void register() {
        StatsAddon.getInstance().getApi().getEventManager().register(new MessageReceiveEvent() {
            @Override
            public boolean onReceive(String formatted, String unFormatted) {
                if (!StatsAddon.getInstance().isEnabled()) return false;
                if (!StatsAddon.getInstance().isOnline()) return false;

                if (StatsAddon.getInstance().getCurrentGamemode() != null && unFormatted.contains("»") && !unFormatted.contains(LabyMod.getInstance().getPlayerName())) {
                    new Thread(() -> {
                        try {
                            Thread.sleep(StatsAddon.getInstance().getCooldown());
                        } catch (InterruptedException exception) {
                            exception.printStackTrace();
                        }

                        Minecraft.getMinecraft().thePlayer.sendQueue.getPlayerInfoMap().forEach((loadedPlayer) -> {
                            final String playerName = loadedPlayer.getGameProfile().getName();

                            if (!StatsAddon.getInstance().getLoadedPlayerStats().containsKey(playerName) &&
                                    !loadedPlayer.getGameProfile().getName().equals(LabyMod.getInstance().getPlayerName()) &&
                                    !loadedPlayer.getPlayerTeam().getColorSuffix().toLowerCase()
                                            .replace("i", "y")
                                            .replace("á", "a")
                                            .contains("party") && playerName.equals(getNameFromJoinLine(unFormatted))) {
                                StatsAddon.getInstance().getLoadedPlayerStats().put(playerName, new PlayerStats(loadedPlayer));
                                LabyMod.getInstance().displayMessageInChat("Added player " + loadedPlayer.getGameProfile().getName() + " to queue. (Message Handler)");
                            }
                        });
                    }).start();
                }

                if (StatsAddon.getInstance().getCurrentGamemode() != null && unFormatted.contains("«") && !unFormatted.contains(LabyMod.getInstance().getPlayerName())) {
                    final String playerName = StringUtils.stripControlCodes(unFormatted.split(" ")[1]);
                    final PlayerStats playerStats = StatsAddon.getInstance().getLoadedPlayerStats().get(playerName);

                    StatsAddon.getInstance().getLoadedPlayerStats().remove(playerName);
                    StatsAddon.getInstance().getStatsChecker().getCheckedPlayers().remove(playerStats);
                    StatsAddon.getInstance().getStatsChecker().getQueue().remove(playerStats);

                    return false;
                }
                if (isHiddenMessage(unFormatted)) {
                    final PlayerStats playerStats = StatsAddon.getInstance().getStatsChecker().getLastRequested();
                    if (playerStats == null) return !StatsAddon.getInstance().isShowStatsMessages();
                    playerStats.setStatsHidden(true);

                    return !StatsAddon.getInstance().isShowStatsMessages();
                }
                if (isStatsNotFoundMessage(unFormatted)) {
                    final PlayerStats playerStats = StatsAddon.getInstance().getStatsChecker().getLastRequested();
                    if (playerStats == null) return !StatsAddon.getInstance().isShowStatsMessages();
                    playerStats.setNickProbability(100);

                    return !StatsAddon.getInstance().isShowStatsMessages();
                }

                if (StatsAddon.getInstance().getCurrentGamemode() != null) {
                    new Thread(() -> {
                        if (unFormatted.toLowerCase().contains("-=")) {
                            lastPlayerName = getNameFromStatsLine(unFormatted);
                        }
                        if (unFormatted.startsWith(" ")) {
                            if (unFormatted.contains("%")) {
                                String[] content = formatted.split("\u00A7e");
                                if (content.length != 2) return;

                                if (!content[1].contains("-")) {
                                    double winrate = Double.parseDouble(content[1]
                                            .replace("\u00A7e", "")
                                            .replace(" ", "")
                                            .replace("\u00A7r", "")
                                            .replace("%", ""));

                                    final PlayerStats playerStats = StatsAddon.getInstance().getLoadedPlayerStats().get(lastPlayerName);
                                    playerStats.setWinRate(winrate);
                                    playerStats.setChecked(true);
                                    playerStats.performStatsAnalysis(PlayerStats.AlertType.WINRATE);
                                    playerStats.performNickCheck();
                                }
                            } else if (unFormatted.toLowerCase().contains("ranking:")) {
                                String[] content = formatted.split("\u00A7e");
                                if (content.length != 2) return;

                                if (!content[1].contains("-")) {
                                    int rank = Integer.parseInt(content[1]
                                            .replace("\u00A7e", "")
                                            .replace(" ", "")
                                            .replace(".", "")
                                            .replace(",", "")
                                            .replace("'", "")
                                            .replace("\u00A7r", "")
                                            .replace("`", "")
                                            .replace("’", ""));


                                    final PlayerStats playerStats = StatsAddon.getInstance().getLoadedPlayerStats().get(lastPlayerName);
                                    playerStats.setRank(rank);
                                    playerStats.setChecked(true);
                                    playerStats.performStatsAnalysis(PlayerStats.AlertType.RANK);

                                    if (!hasGamemodeWinrateSupport(StatsAddon.getInstance().getCurrentGamemode()))
                                        playerStats.performNickCheck();
                                }
                            } else if (unFormatted.toLowerCase().contains("played") ||
                                    unFormatted.toLowerCase().contains("gspielt") ||
                                    unFormatted.toLowerCase().contains("gspüte") ||
                                    unFormatted.toLowerCase().contains("gespielte") ||
                                    unFormatted.toLowerCase().contains("gespillte")
                            ) {
                                String[] content = formatted.split("\u00A7e");
                                if (content.length != 2) return;

                                if (!content[1].contains("-")) {
                                    int playedGames = Integer.parseInt(content[1]
                                            .replace("\u00A7e", "")
                                            .replace(" ", "")
                                            .replace(".", "")
                                            .replace(",", "")
                                            .replace("'", "")
                                            .replace("\u00A7r", "")
                                            .replace("`", "")
                                            .replace("’", ""));


                                    final PlayerStats playerStats = StatsAddon.getInstance().getLoadedPlayerStats().get(lastPlayerName);
                                    playerStats.setPlayedGames(playedGames);
                                    playerStats.setChecked(true);
                                }
                            } else if (unFormatted.toLowerCase().contains("won") ||
                                    unFormatted.toLowerCase().contains("gwunne") ||
                                    unFormatted.toLowerCase().contains("gwunnene") ||
                                    unFormatted.toLowerCase().contains("gewonnene") ||
                                    unFormatted.toLowerCase().contains("gewonnen")
                            ) {
                                String[] content = formatted.split("\u00A7e");
                                if (content.length != 2) return;

                                if (!content[1].contains("-")) {
                                    int wins = Integer.parseInt(content[1]
                                            .replace("\u00A7e", "")
                                            .replace(" ", "")
                                            .replace(".", "")
                                            .replace(",", "")
                                            .replace("'", "")
                                            .replace("\u00A7r", "")
                                            .replace("`", "")
                                            .replace("’", ""));


                                    final PlayerStats playerStats = StatsAddon.getInstance().getLoadedPlayerStats().get(lastPlayerName);
                                    playerStats.setWins(wins);
                                    playerStats.setChecked(true);
                                }
                            }

                        }
                    }).start();
                }

                return isStatsMessage(unFormatted) && !StatsAddon.getInstance().isShowStatsMessages();
            }
        });
    }

    private String getNameFromStatsLine(String string) {
        if (string.contains("-=")) {
            String[] words = string.split(" ");
            return words[3].replace("\u00A76", "");
        }
        return null;
    }

    /**
     * Extracts the player name form the join message
     * @param string Unformatted chat message (without color codes)
     * @return Extracted player name
     */
    private String getNameFromJoinLine(String string) {
        string = string.replace("» ", "");
        StringBuilder playerName = new StringBuilder();
        for (int i = 0; i < string.length() - 1; i++) {
            char c = string.charAt(i);
            if (c == ' ') {
                break;
            }
            playerName.append(c);
        }
        LabyMod.getInstance().displayMessageInChat(playerName.toString());
        return playerName.toString();
    }

    private boolean hasGamemodeWinrateSupport(String gamemode) {
        return !gamemode.equals("JL");
    }

    private boolean isHiddenMessage(String message) {
        return (message.contains("The statistics of this player are hidden") ||
                message.contains("Die Statistiken von diesem Spieler sind versteckt") ||
                message.contains("D'Statistiken vun dësem Spiller sinn verstoppt") || message.contains("D'Statistike von dem Spieler sind versteckt") ||
                message.contains("D'Statistike vo dem Spieler sind versteckt"));
    }

    private boolean isStatsNotFoundMessage(String message) {
        return (message.contains("This player could not be found") || message.contains("Dieser Spieler konnte nicht gefunden werden") ||
                message.contains("Dëse Spiller konnt net fonnt ginn") || message.contains("De Spieler isch nöd gfunde worde"));
    }

    private boolean isStatsMessage(String message) {
        System.out.println(message);
        return message.startsWith("-= ") || (message.startsWith(" ") && !message.startsWith("  ")) || message.contains("------");
    }
}