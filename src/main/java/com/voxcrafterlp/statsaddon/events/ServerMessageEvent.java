package com.voxcrafterlp.statsaddon.events;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.voxcrafterlp.statsaddon.StatsAddon;
import com.voxcrafterlp.statsaddon.utils.StatsDisplayUtil;
import net.labymod.main.LabyMod;
import net.labymod.servermanager.Server;
import net.minecraft.client.Minecraft;

import java.util.List;

public class ServerMessageEvent {

    public void register() {
        StatsAddon.getStatsAddon().getApi().getEventManager().register(new net.labymod.api.events.ServerMessageEvent() {
            @Override
            public void onServerMessage(String s, JsonElement jsonElement) {
                if (jsonElement.getAsJsonObject().has("game_mode")) {
                    if (!StatsAddon.getStatsAddon().lmcDoubled) {
                        StatsAddon.getStatsAddon().lmcDoubled = true;
                        if (jsonElement.getAsJsonObject().get("game_mode").getAsString().toLowerCase().contains("cookies") && StatsAddon.getStatsAddon().enabled) {
                            LabyMod.getInstance().displayMessageInChat(StatsAddon.getStatsAddon().getPrefix() + "\u00A77Die LabyCookies Integration wurde \u00A7aaktiviert\u00A78.");
                            StatsAddon.getStatsAddon().setPlayingCookies(true);
                            StatsAddon.getStatsAddon().checkedPlayers.clear();

                        new Thread(() -> {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            List<String> playerNames = Lists.newCopyOnWriteArrayList();
                            Minecraft.getMinecraft().thePlayer.sendQueue.getPlayerInfoMap().forEach((loadedPlayer) -> {
                                playerNames.add(loadedPlayer.getGameProfile().getName());
                                StatsAddon.getStatsAddon().getPlayersJoined().add(loadedPlayer.getGameProfile().getName());
                            });

                            new StatsDisplayUtil().displayStats(playerNames);
                        }).start();
                        } else {
                            StatsAddon.getStatsAddon().setPlayingCookies(false);
                            StatsAddon.getStatsAddon().getPlayersJoined().clear();
                            LabyMod.getInstance().displayMessageInChat(StatsAddon.getStatsAddon().getPrefix() + "\u00A77Die LabyCookies Integration wurde \u00A7cdeaktiviert\u00A78.");
                        }
                    } else {
                        StatsAddon.getStatsAddon().lmcDoubled = false;
                    }
                }
            }
        });
    }

}
