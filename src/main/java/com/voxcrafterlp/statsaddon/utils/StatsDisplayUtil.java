package com.voxcrafterlp.statsaddon.utils;

import com.voxcrafterlp.statsaddon.StatsAddon;
import net.minecraft.client.Minecraft;

import java.util.List;

/**
 * This file was created by VoxCrafter_LP & Lezurex!
 * Date: 06.09.2020
 * Time: 16:31
 * For Project: Labymod Stats Addon
 */

public class StatsDisplayUtil {

    public void displayStats(List<String> playerNames) {
        new Thread(() -> {
            for(String string: playerNames) {
                if (StatsAddon.getInstance().getCurrentGamemode() != null) {
                    if (!string.equals(Minecraft.getMinecraft().thePlayer.getGameProfile().getName())) {
                        if (!StatsAddon.getInstance().checkedPlayers.contains(string)) {
                            Minecraft.getMinecraft().thePlayer.sendChatMessage("/stats " + string);
                            playerNames.remove(string);
                            StatsAddon.getInstance().checkedPlayers.add(string);
                            try {
                                Thread.sleep(StatsAddon.getInstance().cooldown);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            playerNames.remove(string);
                        }
                    }
                } else
                    break;
            }
        }).start();
    }
}
