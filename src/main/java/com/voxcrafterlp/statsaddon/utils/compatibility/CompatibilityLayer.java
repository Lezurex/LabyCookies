package com.voxcrafterlp.statsaddon.utils.compatibility;

import com.google.gson.JsonElement;
import com.voxcrafterlp.statsaddon.utils.compatibility.events.JoinEvent;
import com.voxcrafterlp.statsaddon.utils.compatibility.events.MessageReceiveEvent;
import com.voxcrafterlp.statsaddon.utils.compatibility.events.ServerMessageEvent;
import net.labymod.main.LabyMod;
import net.labymod.utils.ServerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;

import java.util.concurrent.Callable;

public class CompatibilityLayer {

    public static EntityPlayerSP getMinecraftThePlayer() {
        // Change thePlayer to player for 1.12.2 version
        return Minecraft.getMinecraft().thePlayer;
    }

    public static NetHandlerPlayClient getMinecraftThePlayerSendQueue() {
        return Minecraft.getMinecraft().thePlayer.sendQueue;
    }

    public static String playerInfoGetPrefix(NPlayerInfo playerInfo) {
        return playerInfo.getPlayerTeam().getColorPrefix();
    }

    public static String playerInfoGetSuffix(NPlayerInfo playerInfo) {
        return playerInfo.getPlayerTeam().getColorSuffix();
    }

    public static void playSound(String resource, float volume, float pitch) {
        Minecraft.getMinecraft().thePlayer.playSound(resource, volume, pitch);
    }

    /**
     * Listen to a Forge Event.
     * Use {@link #registerMessageReceive(Class)} or {@link #} for specific LabyMod events-
     *
     * @param listener Listener object
     */
    public static void registerListener(Object listener) {
        LabyMod.getInstance().getLabyModAPI().registerForgeListener(listener);
    }

    public static void registerMessageReceive(Class<? extends MessageReceiveEvent> T) {
        LabyMod.getInstance().getEventManager().register((formatted, unFormatted) -> {
            try {
                Callable<Boolean> listener = T.getDeclaredConstructor(String.class, String.class)
                        .newInstance(formatted, unFormatted);
                return listener.call();
            } catch (Exception exception) {
                exception.printStackTrace();
                return false;
            }
        });
    }

    public static void registerServerMessage(Class<? extends ServerMessageEvent> T) {
        LabyMod.getInstance().getEventManager()
                .register((net.labymod.api.events.ServerMessageEvent) (s, jsonElement) -> {
                    try {
                        Callable<Void> listener = T.getDeclaredConstructor(String.class, JsonElement.class)
                                .newInstance(s, jsonElement);
                        listener.call();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                });
    }

    public static void registerOnJoin(Class<? extends JoinEvent> T) {
        LabyMod.getInstance().getEventManager().registerOnJoin(serverData -> {
            try {
                Callable<Void> listener = T.getDeclaredConstructor(ServerData.class).newInstance(serverData);
                listener.call();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

}
