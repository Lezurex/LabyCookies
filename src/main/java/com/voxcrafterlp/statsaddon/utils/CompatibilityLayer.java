package com.voxcrafterlp.statsaddon.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;

public class CompatibilityLayer {

    public static EntityPlayerSP getMinecraftThePlayer() {
        // Change thePlayer to player for 1.12.2 version
        return Minecraft.getMinecraft().player;
    }

    public static NetHandlerPlayClient getMinecraftThePlayerSendQueue() {
        return Minecraft.getMinecraft().player.connection;
    }

    public static String playerInfoGetPrefix(NetworkPlayerInfo playerInfo) {
        return playerInfo.getPlayerTeam().getPrefix();
    }

    public static String playerInfoGetSuffix(NetworkPlayerInfo playerInfo) {
        return playerInfo.getPlayerTeam().getSuffix();
    }

}
