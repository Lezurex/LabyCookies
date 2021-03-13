package com.voxcrafterlp.statsaddon.utils;

import com.google.common.collect.Lists;
import com.voxcrafterlp.statsaddon.StatsAddon;
import com.voxcrafterlp.statsaddon.objects.HotKey;
import com.voxcrafterlp.statsaddon.objects.PlayerStats;
import lombok.Getter;
import net.labymod.main.LabyMod;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 11.03.2021
 * Time: 16:01
 * Project: LabyCookies
 */

@Getter
public class KeyPressUtil {

    private final List<HotKey> registeredHotKeys;

    public KeyPressUtil() {
        this.registeredHotKeys = Lists.newCopyOnWriteArrayList();
        this.registerKeys();
    }

    public void registerKeys() {
        this.registeredHotKeys.clear();

        this.registeredHotKeys.add(new HotKey(StatsAddon.getInstance().getReloadStatsKey(), () -> {
            if(!StatsAddon.getInstance().isEnabled()) return;
            if(!StatsAddon.getInstance().isOnline()) return ;
            if(StatsAddon.getInstance().getCurrentGamemode() == null) {
                LabyMod.getInstance().displayMessageInChat(StatsAddon.getInstance().getPrefix() + "\u00A77Das StatsAddon ist hier \u00A7cnicht \u00A77verfügbar\u00A78.");
                return;
            }

            LabyMod.getInstance().displayMessageInChat(StatsAddon.getInstance().getPrefix() + "\u00A77Die Statistiken werden nun \u00A7baktualisiert\u00A78.");
            StatsAddon.getInstance().clearCache();

            Minecraft.getMinecraft().thePlayer.sendQueue.getPlayerInfoMap().forEach((loadedPlayer) -> {
                final String playerName = loadedPlayer.getGameProfile().getName();

                if(!StatsAddon.getInstance().getLoadedPlayerStats().containsKey(playerName) && !loadedPlayer.getGameProfile().getName().equals(LabyMod.getInstance().getPlayerName()) && !loadedPlayer.getPlayerTeam().getColorSuffix().toLowerCase().contains("party"))
                    StatsAddon.getInstance().getLoadedPlayerStats().put(playerName, new PlayerStats(loadedPlayer));
            });
        }));
    }

}
