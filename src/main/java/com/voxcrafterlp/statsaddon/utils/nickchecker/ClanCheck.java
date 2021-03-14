package com.voxcrafterlp.statsaddon.utils.nickchecker;

import net.minecraft.client.network.NetworkPlayerInfo;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 11.03.2021
 * Time: 20:25
 * Project: LabyCookies
 */

public class ClanCheck implements Check {

    private NetworkPlayerInfo playerInfo;
    private boolean successful;

    @Override
    public void performCheck(NetworkPlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
        this.successful = !playerInfo.getPlayerTeam().getSuffix().contains("[");
    }

    @Override
    public boolean isCheckSuccessful() {
        return this.successful;
    }

    @Override
    public boolean ignore() {
        return playerInfo.getPlayerTeam().getSuffix().contains("party");
    }

    @Override
    public int getWeight() {
        return 20;
    }
}
