package com.voxcrafterlp.statsaddon.events;

import com.google.common.collect.Lists;
import com.voxcrafterlp.statsaddon.StatsAddon;
import com.voxcrafterlp.statsaddon.utils.StatsDisplayUtil;
import net.labymod.api.events.MessageReceiveEvent;
import net.labymod.main.LabyMod;
import net.minecraft.client.Minecraft;

import java.util.List;

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

                if(StatsAddon.getInstance().getCurrentGamemode() != null && unFormatted.contains("»") && !unFormatted.contains(LabyMod.getInstance().getPlayerName())) {
                    new Thread(() -> {
                        try {
                            Thread.sleep(StatsAddon.getInstance().cooldown);
                        } catch (InterruptedException exception) {
                            exception.printStackTrace();
                        }

                        List<String> playerNames = Lists.newCopyOnWriteArrayList();
                        Minecraft.getMinecraft().thePlayer.sendQueue.getPlayerInfoMap().forEach((loadedPlayer) -> {
                            if(!StatsAddon.getInstance().getPlayersJoined().contains(loadedPlayer.getGameProfile().getName())) {
                                playerNames.add(loadedPlayer.getGameProfile().getName());
                                StatsAddon.getInstance().getPlayersJoined().add(loadedPlayer.getGameProfile().getName());
                            }

                        });

                        new StatsDisplayUtil().displayStats(playerNames);

                    }).start();
                }
                if(StatsAddon.getInstance().getCurrentGamemode() != null && StatsAddon.getInstance().enabled) {
                    new Thread(() -> {
                        if(unFormatted.toLowerCase().contains("-=")) {
                            lastPlayerName = getNameFromStatsLine(unFormatted);
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
                                        if(!lastPlayerName.equals(Minecraft.getMinecraft().thePlayer.getGameProfile().getName())) {
                                            LabyMod.getInstance().displayMessageInChat(StatsAddon.getInstance().getGamemodePrefix() + "\u00A74Achtung! \u00A77Potentiell gef\u00E4hrlicher Gegner\u00A77!");
                                            LabyMod.getInstance().displayMessageInChat(StatsAddon.getInstance().getGamemodePrefix() + "\u00A77Platz \u00A7e#" + rank + " \u00A77Name\u00A78: \u00A7c" + lastPlayerName);
                                            if(StatsAddon.getInstance().alertEnabled) {
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