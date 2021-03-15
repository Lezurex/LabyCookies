package com.voxcrafterlp.statsaddon.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;

public class CompatibilityLayer {

    public static EntityPlayerSP getMinecraftThePlayer() {
        // Change thePlayer to player for 1.12.2 version
        return Minecraft.getMinecraft().thePlayer;
    }

    public static NetHandlerPlayClient getMinecraftThePlayerSendQueue() {
        return Minecraft.getMinecraft().thePlayer.sendQueue;
    }

    public static String playerInfoGetPrefix(NetworkPlayerInfo playerInfo) {
        return playerInfo.getPlayerTeam().getColorPrefix();
    }

    public static String playerInfoGetSuffix(NetworkPlayerInfo playerInfo) {
        return playerInfo.getPlayerTeam().getColorSuffix();
    }

    public static void playSound(String resource, float volume, float pitch) {
        Minecraft.getMinecraft().thePlayer.playSound(resource, volume, pitch);
    }

}
