package com.voxcrafterlp.statsaddon.utils;

import net.minecraft.client.Minecraft;

import java.util.List;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 06.09.2020
 * Time: 16:31
 * For Project: Labymod Stats Addon
 */

public class StatsDisplayUtil {

    public void displayStats(List<String> playerNames) {
        new Thread(() -> {
            for(String string: playerNames) {
                if(!string.equals(Minecraft.getMinecraft().thePlayer.getGameProfile().getName())) {
                    Minecraft.getMinecraft().thePlayer.sendChatMessage("/stats " + string);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
