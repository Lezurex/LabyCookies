package com.voxcrafterlp.statsaddon.utils.nickchecker;

import net.minecraft.client.network.NetworkPlayerInfo;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 11.03.2021
 * Time: 20:32
 * Project: LabyCookies
 */

public class BadlionCheck implements Check {

    private boolean successful;

    @Override
    public void performCheck(NetworkPlayerInfo playerInfo) {
        this.successful = playerInfo.getPlayerTeam().getColorSuffix().contains("✔");
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
        return 15;
    }

}
