package com.voxcrafterlp.statsaddon.objects;

import lombok.Getter;
import net.labymod.core.LabyModCore;
import org.lwjgl.input.Keyboard;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 11.03.2021
 * Time: 16:46
 * Project: LabyCookies
 */

@Getter
public class HotKey {

    private final int key;
    private final Runnable onPress;

    public HotKey(int key, Runnable onPress) {
        this.key = key;
        this.onPress = onPress;
    }

    public boolean isPressed() {
        return (this.key != -1 && Keyboard.isKeyDown(this.key) && !LabyModCore.getMinecraft().isMinecraftChatOpen());
    }

    public void trigger() {
        this.onPress.run();
    }

}
