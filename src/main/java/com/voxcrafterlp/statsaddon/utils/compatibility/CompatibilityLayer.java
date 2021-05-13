package com.voxcrafterlp.statsaddon.utils.compatibility;

import com.google.gson.JsonElement;
import com.voxcrafterlp.statsaddon.utils.compatibility.events.JoinEvent;
import com.voxcrafterlp.statsaddon.utils.compatibility.events.MessageReceiveEvent;
import com.voxcrafterlp.statsaddon.utils.compatibility.events.ServerMessageEvent;
import com.voxcrafterlp.statsaddon.utils.compatibility.events.TickEventHandler;
import net.labymod.api.event.Subscribe;
import net.labymod.main.LabyMod;
import net.labymod.utils.ServerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;

import java.util.concurrent.Callable;

public class CompatibilityLayer {

    public static ClientPlayerEntity getMinecraftThePlayer() {
        // Change thePlayer to player for 1.12.2 version
        return Minecraft.getInstance().player;
    }

    public static ClientPlayNetHandler getMinecraftThePlayerSendQueue() {
        return Minecraft.getInstance().getConnection();
    }

    public static String playerInfoGetPrefix(NPlayerInfo playerInfo) {
        return playerInfo.getPlayerTeam().getPrefix().getString();
    }

    public static String playerInfoGetSuffix(NPlayerInfo playerInfo) {
        return playerInfo.getPlayerTeam().getSuffix().getString();
    }

    public static void playSound(String resource, float volume, float pitch) {
        getMinecraftThePlayer().playSound(new SoundEvent(new ResourceLocation(resource)), volume, pitch);
    }

    /**
     * Listen to a Forge Event.
     * Use {@link #registerMessageReceive(Class)} or {@link #} for specific LabyMod events-
     *
     * @param listener Listener object
     */
    public static void registerListener(Object listener) {
        LabyMod.getInstance().getEventService().registerListener(listener);
    }

    public static void registerTick() {
        LabyMod.getInstance().getEventService().registerListener(new TickEventHandler());
    }

    public static void registerMessageReceive(Class<? extends MessageReceiveEvent> T) {
        LabyMod.getInstance().getEventService().registerListener(new EventClass() {
            @Subscribe
            public boolean onMessageReceive(ITextComponent iTextComponent) {
                String formatted = iTextComponent.getString();
                String unFormatted = iTextComponent.getUnformattedComponentText();
                try {
                    Callable<Boolean> listener = T.getDeclaredConstructor(String.class, String.class)
                            .newInstance(formatted, unFormatted);
                    return listener.call();
                } catch (Exception exception) {
                    exception.printStackTrace();
                    return false;
                }
            }
        });
    }

    public static void registerServerMessage(Class<? extends ServerMessageEvent> T) {
        LabyMod.getInstance().getEventService().registerListener(new EventClass() {
            @Subscribe
            public void onServerMessage(String s, JsonElement jsonElement) {
                try {
                    Callable<Void> listener = T.getDeclaredConstructor(String.class, JsonElement.class)
                            .newInstance(s, jsonElement);
                    listener.call();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
    }

    public static void registerOnJoin(Class<? extends JoinEvent> T) {
        LabyMod.getInstance().getEventService().registerListener(new EventClass() {
            @Subscribe
            public void onServerJoin(ServerData serverData) {
                try {
                    Callable<Void> listener = T.getDeclaredConstructor(ServerData.class).newInstance(serverData);
                    listener.call();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
    }

    static class EventClass {}

}
