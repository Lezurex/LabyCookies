package com.voxcrafterlp.statsaddon.events;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.voxcrafterlp.statsaddon.StatsAddon;
import com.voxcrafterlp.statsaddon.utils.StatsDisplayUtil;
import net.labymod.core.LabyModCore;
import net.labymod.main.LabyMod;
import net.minecraft.client.network.NetworkPlayerInfo;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServerMessageEvent {

    public void register() {
        StatsAddon.getInstance().getApi().getEventManager().register(new net.labymod.api.events.ServerMessageEvent() {
            @Override
            public void onServerMessage(String s, JsonElement jsonElement) {
                if(jsonElement.getAsJsonObject().has("game_mode")) {
                    if(!StatsAddon.getInstance().lmcDoubled) {
                        StatsAddon.getInstance().lmcDoubled = true;
                        if(jsonElement.getAsJsonObject().get("game_mode").getAsString().toLowerCase().contains("cookies") && StatsAddon.getInstance().enabled) {
                            LabyMod.getInstance().displayMessageInChat(StatsAddon.getInstance().getPrefix() + "\u00A77Die LabyCookies Integration wurde \u00A7aaktiviert\u00A78.");
                            StatsAddon.getInstance().setPlayingCookies(true);
                            StatsAddon.getInstance().checkedPlayers.clear();

                            new Thread(() -> {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                List<NetworkPlayerInfo> playerNames = Lists.newCopyOnWriteArrayList();
                                LabyModCore.getMinecraft().getPlayer().sendQueue.getPlayerInfoMap().forEach((loadedPlayer) -> {
                                    playerNames.add(loadedPlayer);
                                    StatsAddon.getInstance().getPlayersJoined().add(loadedPlayer);
                                });

                                new StatsDisplayUtil().displayStats(playerNames, null);
                            }).start();
                        } else {
                            StatsAddon.getInstance().setPlayingCookies(false);
                            StatsAddon.getInstance().getPlayersJoined().clear();
                            LabyMod.getInstance().displayMessageInChat(StatsAddon.getInstance().getPrefix() + "\u00A77Die LabyCookies Integration wurde \u00A7cdeaktiviert\u00A78.");
                        }
                    } else
                        StatsAddon.getInstance().lmcDoubled = false;
                }
            }
        });
    }

    private boolean isAvailableGamemode(String name) {
        AtomicBoolean exists = new AtomicBoolean(false);

        StatsAddon.getInstance().getEnabledGamemods().forEach((string, enabled) -> {
            if(name.toLowerCase().startsWith(string.toLowerCase()))
                exists.set(true);
        });

        if(!exists.get()) {
            StatsAddon.getInstance().setCurrentGamemode(null);
            return false;
        }

        AtomicBoolean gamemodeEnabled = new AtomicBoolean(false);

        StatsAddon.getInstance().getEnabledGamemods().forEach((string, enabled) -> {
            if(name.toLowerCase().startsWith(string.toLowerCase())) {
                gamemodeEnabled.set(enabled);
                StatsAddon.getInstance().setCurrentGamemode(string);
            }
        });

        if(!gamemodeEnabled.get())
            StatsAddon.getInstance().setCurrentGamemode(null);

        System.out.println("Current gamemode: " + name + " (Version: " + StatsAddon.getInstance().getCurrentVersion() + ")");
        return gamemodeEnabled.get();
    }

}
