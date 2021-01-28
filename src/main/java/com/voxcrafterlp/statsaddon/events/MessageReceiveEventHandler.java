package com.voxcrafterlp.statsaddon.events;

import com.google.common.collect.Lists;
import com.voxcrafterlp.statsaddon.StatsAddon;
import com.voxcrafterlp.statsaddon.utils.StatsDisplayUtil;
import net.labymod.api.events.MessageReceiveEvent;
import net.labymod.main.LabyMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;

import java.util.List;

/**
 * This file was created by VoxCrafter_LP & Lezurex!
 * Date: 06.09.2020
 * Time: 16:51
 * For Project: Labymod Stats Addon
 */

public class MessageReceiveEventHandler {

    private NetworkPlayerInfo lastPlayerInfo;

    public void register() {
        StatsAddon.getInstance().getApi().getEventManager().register(new MessageReceiveEvent() {
            @Override
            public boolean onReceive(String formatted, String unFormatted) {

                if(StatsAddon.getInstance().getCurrentGamemode() != null && unFormatted.contains("»") && !unFormatted.contains(LabyMod.getInstance().getPlayerName())) {
                    new Thread(() -> {
                        try {
                            Thread.sleep(StatsAddon.getInstance().cooldown);
                        } catch (InterruptedException exception) {
                            exception.printStackTrace();
                        }

                        List<NetworkPlayerInfo> playerNames = Lists.newCopyOnWriteArrayList();
                        Minecraft.getMinecraft().thePlayer.sendQueue.getPlayerInfoMap().forEach((loadedPlayer) -> {
                            if(!StatsAddon.getInstance().getPlayersJoined().contains(loadedPlayer)) {
                                playerNames.add(loadedPlayer);
                                StatsAddon.getInstance().getPlayersJoined().add(loadedPlayer);
                            }

                        });

                        new StatsDisplayUtil().displayStats(playerNames, unFormatted);
                    }).start();
                }
                if(StatsAddon.getInstance().getCurrentGamemode() != null && StatsAddon.getInstance().enabled) {
                    new Thread(() -> {
                        if (unFormatted.toLowerCase().contains("-=")) {
                            String lastPlayerName = getNameFromStatsLine(unFormatted);
                            for (NetworkPlayerInfo playerInfo : Minecraft.getMinecraft().thePlayer.sendQueue.getPlayerInfoMap()) {
                                if (playerInfo.getGameProfile().getName().equalsIgnoreCase(lastPlayerName)) {
                                    lastPlayerInfo = playerInfo;
                                    break;
                                }
                            }
                        }
                        if(unFormatted.toLowerCase().contains("ranking:") && unFormatted.startsWith(" ")) {
                            String[] content = formatted.split("\u00A7e");
                            if(content.length == 2) {

                                if(!content[1].contains("-")) {
                                    int rank = Integer.parseInt(content[1].replace("\u00A7e", "").replace(" ", "").replace(".", "").replace(",", "").replace("'", "").replace("\u00A7r", ""));
                                    if(rank < StatsAddon.getInstance().warnLevel) {
                                        try {
                                            Thread.sleep(20);
                                        } catch (InterruptedException exception) {
                                            exception.printStackTrace();
                                        }
                                        if(!lastPlayerInfo.getGameProfile().getName().equals(Minecraft.getMinecraft().thePlayer.getGameProfile().getName())) {
                                            LabyMod.getInstance().displayMessageInChat(StatsAddon.getInstance().getPrefix() + "\u00A74Achtung! \u00A77Potentiell gef\u00E4hrlicher Gegner\u00A77!");
                                            LabyMod.getInstance().displayMessageInChat(StatsAddon.getInstance().getPrefix() + "\u00A77Platz \u00A7e#" + rank + " \u00A77Name\u00A78: \u00A7c" + lastPlayerInfo);
                                            if (StatsAddon.getInstance().alertEnabled) {
                                                new Thread(() -> {
                                                    for (int i = 0; i < 5; i++) {
                                                        Minecraft.getMinecraft().thePlayer.playSound("note.pling", 1, 1);
                                                        try {
                                                            Thread.sleep(250);
                                                        } catch (InterruptedException exception) {
                                                            exception.printStackTrace();
                                                        }
                                                    }
                                                }).start();
                                            }
                                            new StatsDisplayUtil().checkNicked(lastPlayerInfo, unFormatted);
                                        }
                                    }
                                }
                            }
                        }
                    }).start();
                }
                return false;
            }
        });
    }

    private String getNameFromStatsLine(String string) {
        if(string.contains("-=")) {
            String[] words = string.split(" ");
            return words[3].replace("\u00A76", "");
        }
        return null;
    }
}