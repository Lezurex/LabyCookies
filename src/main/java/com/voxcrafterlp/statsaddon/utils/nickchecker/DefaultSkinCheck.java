package com.voxcrafterlp.statsaddon.utils.nickchecker;

import net.labymod.main.LabyMod;
import net.minecraft.client.network.NetworkPlayerInfo;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 11.03.2021
 * Time: 20:34
 * Project: LabyCookies
 */

public class DefaultSkinCheck implements Check {

    private boolean successful;

    @Override
    public void performCheck(NetworkPlayerInfo playerInfo) {
        final String path = playerInfo.getLocationSkin().getResourcePath();
        this.successful = (path.equals("textures/entity/steve.png") || path.equals("textures/entity/alex.png"));
    }

    @Override
    public boolean isCheckSuccessful() {
        return this.successful;
    }

    @Override
    public boolean ignore() {
        return false;
    }

    @Override
    public int getWeight() {
        return 20;
    }
}
