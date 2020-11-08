package com.voxcrafterlp.statsaddon.events;

import com.google.common.collect.Lists;
import com.voxcrafterlp.statsaddon.StatsAddon;
import com.voxcrafterlp.statsaddon.utils.StatsDisplayUtil;
import net.labymod.api.events.MessageReceiveEvent;
import net.labymod.main.LabyMod;
import net.minecraft.client.Minecraft;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This file was created by VoxCrafter_LP & Lezurex!
 * Date: 06.09.2020
 * Time: 16:51
 * For Project: Labymod Stats Addon
 */

public class MessageReceiveEventHandler {

    private String lastPlayerName;

    public void register() {
        StatsAddon.getStatsAddon().getApi().getEventManager().register(new MessageReceiveEvent() {
            @Override
            public boolean onReceive(String formatted, String unFormatted) {

                if(StatsAddon.getStatsAddon().isPlayingCookies() && unFormatted.contains("»") && !unFormatted.contains(Minecraft.getMinecraft().thePlayer.getGameProfile().getName())) {
                    new Thread(() -> {
                        try {
                            Thread.sleep(StatsAddon.getStatsAddon().cooldown);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        List<String> playerNames = Lists.newCopyOnWriteArrayList();
                        Minecraft.getMinecraft().thePlayer.sendQueue.getPlayerInfoMap().forEach((loadedPlayer) -> {
                            if(!StatsAddon.getStatsAddon().getPlayersJoined().contains(loadedPlayer.getGameProfile().getName())) {
                                playerNames.add(loadedPlayer.getGameProfile().getName());
                                StatsAddon.getStatsAddon().getPlayersJoined().add(loadedPlayer.getGameProfile().getName());
                            }

                        });

                        new StatsDisplayUtil().displayStats(playerNames);
                    }).start();
                }
                if(StatsAddon.getStatsAddon().isPlayingCookies() && StatsAddon.getStatsAddon().enabled) {
                    new Thread(() -> {
                        if(unFormatted.toLowerCase().contains("-=")) {
                            lastPlayerName = getNameFromStatsLine(unFormatted);
                        }
                        if(unFormatted.toLowerCase().contains("ranking:") && unFormatted.startsWith(" ")) {
                            String[] content = formatted.split("\u00A7e");
                            if(content.length == 2) {

                                if(!content[1].contains("-")) {
                                    int rank = Integer.parseInt(content[1].replace("\u00A7e", "").replace(" ", "").replace(".", "").replace(",", "").replace("\u00A7r", ""));
                                    if(rank < StatsAddon.getStatsAddon().warnLevel) {
                                        try {
                                            Thread.sleep(20);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        if(!lastPlayerName.equals(Minecraft.getMinecraft().thePlayer.getGameProfile().getName())) {
                                            LabyMod.getInstance().displayMessageInChat(StatsAddon.getStatsAddon().getPrefix() + "\u00A74Achtung! \u00A77Potentiell gef\u00E4hrlicher Gegner\u00A77!");
                                            LabyMod.getInstance().displayMessageInChat(StatsAddon.getStatsAddon().getPrefix() + "\u00A77Platz \u00A7e#" + rank + " \u00A77Name\u00A78: \u00A7c" + lastPlayerName);

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