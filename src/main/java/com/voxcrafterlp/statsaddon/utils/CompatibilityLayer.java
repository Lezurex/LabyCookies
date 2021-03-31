package com.voxcrafterlp.statsaddon.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class CompatibilityLayer {

    public static EntityPlayerSP getMinecraftThePlayer() {
        // Change thePlayer to player for 1.12.2 version
        return Minecraft.getMinecraft().player;
    }

    public static NetHandlerPlayClient getMinecraftThePlayerSendQueue() {
        return Minecraft.getMinecraft().player.connection;
    }

    public static String playerInfoGetPrefix(NetworkPlayerInfo playerInfo) {
        try {
            return playerInfo.getPlayerTeam().getPrefix();
        } catch (NullPointerException exception) {
            return "";
        }
    }

    public static String playerInfoGetSuffix(NetworkPlayerInfo playerInfo) {
        try {
            return playerInfo.getPlayerTeam().getSuffix();
        } catch (NullPointerException exception) {
            return "";
        }
    }

    public static void playSound(String resource, float volume, float pitch) {
        Minecraft.getMinecraft().player.playSound(new SoundEvent(new ResourceLocation(resource)), volume, pitch);
    }

}
