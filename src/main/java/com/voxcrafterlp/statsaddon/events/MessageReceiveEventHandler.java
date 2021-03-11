package com.voxcrafterlp.statsaddon.events;

import com.voxcrafterlp.statsaddon.StatsAddon;
import com.voxcrafterlp.statsaddon.objects.PlayerStats;
import net.labymod.api.events.MessageReceiveEvent;
import net.labymod.main.LabyMod;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StringUtils;

/**
 * This file was created by VoxCrafter_LP & Lezurex!
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
                if(!StatsAddon.getInstance().isEnabled()) return false;
                if(!StatsAddon.getInstance().isOnline()) return false;

                if(StatsAddon.getInstance().getCurrentGamemode() != null && unFormatted.contains("»") && !unFormatted.contains(LabyMod.getInstance().getPlayerName())) {
                    new Thread(() -> {
                        try {
                            Thread.sleep(StatsAddon.getInstance().getCooldown());
                        } catch (InterruptedException exception) {
                            exception.printStackTrace();
                        }

                        Minecraft.getMinecraft().thePlayer.sendQueue.getPlayerInfoMap().forEach((loadedPlayer) -> {
                            final String playerName = loadedPlayer.getGameProfile().getName();

                            if(!StatsAddon.getInstance().getLoadedPlayerStats().containsKey(playerName))
                                StatsAddon.getInstance().getLoadedPlayerStats().put(playerName, new PlayerStats(loadedPlayer));
                        });
                    }).start();
                }

                if(StatsAddon.getInstance().getCurrentGamemode() != null && unFormatted.contains("«") && !unFormatted.contains(LabyMod.getInstance().getPlayerName())) {
                    StatsAddon.getInstance().getLoadedPlayerStats().remove(StringUtils.stripControlCodes(unFormatted.split(" ")[1]));
                    return false;
                }

                if(StatsAddon.getInstance().getCurrentGamemode() != null) {
                    new Thread(() -> {
                        if(unFormatted.toLowerCase().contains("-=")) {
                            lastPlayerName = getNameFromStatsLine(unFormatted);
                        }
                        if(unFormatted.toLowerCase().contains("ranking:") && unFormatted.startsWith(" ")) {
                            String[] content = formatted.split("\u00A7e");
                            if(content.length != 2) return;

                            if(!content[1].contains("-")) {
                                int rank = Integer.parseInt(content[1]
                                        .replace("\u00A7e", "")
                                        .replace(" ", "")
                                        .replace(".", "")
                                        .replace(",", "")
                                        .replace("'", "")
                                        .replace("\u00A7r", "")
                                        .replace("`", ""));


                                final PlayerStats playerStats = StatsAddon.getInstance().getLoadedPlayerStats().get(lastPlayerName);
                                playerStats.setRank(rank);
                                playerStats.setChecked(true);
                                playerStats.performStatsAnalysis(PlayerStats.AlertType.RANK);

                                if(!hasGamemodeWinrateSupport(StatsAddon.getInstance().getCurrentGamemode()))
                                    playerStats.performNickCheck();
                            }
                        }
                        if(unFormatted.toLowerCase().contains("%") && unFormatted.startsWith(" ")) {
                            String[] content = formatted.split("\u00A7e");
                            if(content.length != 2) return;

                            if(!content[1].contains("-")) {
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
                        }
                    }).start();
                }
                return false;
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

    private boolean hasGamemodeWinrateSupport(String gamemode) {
        return !gamemode.equals("JL");
    }
}