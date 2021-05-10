package com.voxcrafterlp.statsaddon.events;

import com.google.gson.JsonElement;
import com.voxcrafterlp.statsaddon.StatsAddon;
import com.voxcrafterlp.statsaddon.objects.PlayerStats;
import com.voxcrafterlp.statsaddon.utils.compatibility.CompatibilityLayer;
import com.voxcrafterlp.statsaddon.utils.compatibility.events.ServerMessageEvent;
import net.labymod.core.LabyModCore;
import net.labymod.main.LabyMod;

import java.util.concurrent.atomic.AtomicBoolean;

public class ServerMessageEventHandler extends ServerMessageEvent {

    public ServerMessageEventHandler(String s, JsonElement jsonElement) {
        super(s, jsonElement);
    }

    public Void call() {
        if (!StatsAddon.getInstance().isEnabled()) return null;
        if (!StatsAddon.getInstance().isOnline()) return null;

        if (jsonElement.getAsJsonObject().has("game_mode")) {
            if (!StatsAddon.getInstance().isLmcDoubled()) {
                StatsAddon.getInstance().setLmcDoubled(true);

                String name = jsonElement.getAsJsonObject().get("game_mode").getAsString().toLowerCase();
                if (isAvailableGamemode(name)) {
                    LabyMod.getInstance().displayMessageInChat(StatsAddon.getInstance().getPrefix() + "\u00A77Das StatsAddon wurde \u00A7aaktiviert\u00A78.");
                    StatsAddon.getInstance().clearCache();
                    StatsAddon.getInstance().getStatsChecker().startChecker();

                    new Thread(() -> {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        LabyModCore.getMinecraft().getPlayer().sendQueue.getPlayerInfoMap().forEach((loadedPlayer) -> {
                            if (!StatsAddon.getInstance().getLoadedPlayerStats().containsKey(loadedPlayer.getGameProfile().getName()) &&
                                    !loadedPlayer.getGameProfile().getName().equals(LabyMod.getInstance().getPlayerName()) &&
                                    !CompatibilityLayer.playerInfoGetSuffix(loadedPlayer).toLowerCase()
                                            .replace("i", "y")
                                            .replace("á", "a")
                                            .contains("party")) {
                                StatsAddon.getInstance().getLoadedPlayerStats().put(loadedPlayer.getGameProfile().getName(), new PlayerStats(loadedPlayer, null));
                            }
                        });
                    }).start();
                } else {
                    StatsAddon.getInstance().getStatsChecker().stopCheck();
                    StatsAddon.getInstance().clearCache();
                    LabyMod.getInstance().displayMessageInChat(StatsAddon.getInstance().getPrefix() + "\u00A77Das StatsAddon ist hier \u00A7cnicht \u00A77verfügbar\u00A78.");
                }
            } else
                StatsAddon.getInstance().setLmcDoubled(false);
        }
        return null;
    }

    private boolean isAvailableGamemode(String name) {
        AtomicBoolean exists = new AtomicBoolean(false);

        StatsAddon.getInstance().getEnabledGamemods().forEach((string, enabled) -> {
            if (name.toLowerCase().startsWith(string.toLowerCase()))
                exists.set(true);
        });

        if (!exists.get()) {
            StatsAddon.getInstance().setCurrentGamemode(null);
            return false;
        }

        AtomicBoolean gamemodeEnabled = new AtomicBoolean(false);

        StatsAddon.getInstance().getEnabledGamemods().forEach((string, enabled) -> {
            if (name.toLowerCase().startsWith(string.toLowerCase())) {
                gamemodeEnabled.set(enabled);
                StatsAddon.getInstance().setCurrentGamemode(string);
            }
        });

        if (!gamemodeEnabled.get())
            StatsAddon.getInstance().setCurrentGamemode(null);

        System.out.println("Current gamemode: " + name + " (Version: " + StatsAddon.getInstance().getCurrentVersion() + ")");
        return gamemodeEnabled.get();
    }

}
