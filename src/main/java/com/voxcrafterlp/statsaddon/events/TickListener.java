package com.voxcrafterlp.statsaddon.events;

import com.voxcrafterlp.statsaddon.StatsAddon;
import com.voxcrafterlp.statsaddon.utils.KeyPressUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 11.03.2021
 * Time: 16:20
 * Project: LabyCookies
 */

public class TickListener {

    private final HashMap<Integer, Boolean> lastPressed;

    public TickListener() {
        this.lastPressed = new HashMap<>();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        final KeyPressUtil keyPressUtil = StatsAddon.getInstance().getKeyPressUtil();

        keyPressUtil.getRegisteredHotKeys().forEach(hotKey -> {
            if(hotKey.isPressed()) {
                if(lastPressed.containsKey(hotKey.getKey())) {
                    if(!lastPressed.get(hotKey.getKey())) {
                        hotKey.trigger();
                        lastPressed.replace(hotKey.getKey(), true);
                    }
                } else {
                    lastPressed.put(hotKey.getKey(), true);
                    hotKey.trigger();
                }
            } else {
                if(this.lastPressed.containsKey(hotKey.getKey()))
                    this.lastPressed.replace(hotKey.getKey(), false);
                 else
                    this.lastPressed.put(hotKey.getKey(), false);
            }
        });
    }

}
