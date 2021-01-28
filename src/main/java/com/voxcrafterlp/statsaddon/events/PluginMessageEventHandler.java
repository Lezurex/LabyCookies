package com.voxcrafterlp.statsaddon.events;

import com.google.common.collect.Lists;
import com.voxcrafterlp.statsaddon.StatsAddon;
import com.voxcrafterlp.statsaddon.utils.StatsDisplayUtil;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import net.labymod.api.events.PluginMessageEvent;
import net.labymod.main.LabyMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.PacketBuffer;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 06.09.2020
 * Time: 15:45
 * For Project: Labymod Stats Addon
 */

public class PluginMessageEventHandler {

    public void register() {
        StatsAddon.getInstance().getApi().getEventManager().register(new PluginMessageEvent() {
            @Override
            public void receiveMessage(String string, PacketBuffer packetBuffer) {
                if(string.equals("GoMod")) {
                    String content = readString(packetBuffer, 2048);
                    if(content.toLowerCase().contains("cookies") && StatsAddon.getInstance().enabled) {
                        LabyMod.getInstance().displayMessageInChat(StatsAddon.getInstance().getPrefix() + "\u00A77Die LabyCookies Integration wurde \u00A7aaktiviert\u00A78.");
                        StatsAddon.getInstance().setPlayingCookies(true);

                        new Thread(() -> {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            List<NetworkPlayerInfo> playerNames = Lists.newCopyOnWriteArrayList();
                            Minecraft.getMinecraft().thePlayer.sendQueue.getPlayerInfoMap().forEach((loadedPlayer) -> {
                                playerNames.add(loadedPlayer);
                                StatsAddon.getInstance().getPlayersJoined().add(loadedPlayer);
                            });

                            new StatsDisplayUtil().displayStats(playerNames, null);
                        }).start();
                    } else {
                        StatsAddon.getInstance().setPlayingCookies(false);
                        StatsAddon.getInstance().getPlayersJoined().clear();
                    }
                }
            }
        });
    }

    private int readVarIntFromBuffer(ByteBuf buf) {
        int i = 0;
        int j = 0;

        byte b0;
        do {
            b0 = buf.readByte();
            i |= (b0 & 127) << j++ * 7;
            if (j > 5) {
                throw new RuntimeException("VarInt too big");
            }
        } while((b0 & 128) == 128);

        return i;
    }

    private String readString(ByteBuf buf, int maxLength) {
        int i = this.readVarIntFromBuffer(buf);
        if (i > maxLength * 4) {
            throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + i + " > " + maxLength * 4 + ")");
        } else if (i < 0) {
            throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
        } else {
            byte[] bytes = new byte[i];
            buf.readBytes(bytes);
            String s = new String(bytes, StandardCharsets.UTF_8);
            if (s.length() > maxLength) {
                throw new DecoderException("The received string length is longer than maximum allowed (" + i + " > " + maxLength + ")");
            } else {
                return s;
            }
        }
    }

}