package com.voxcrafterlp.statsaddon.utils.nickchecker;

import net.minecraft.client.network.NetworkPlayerInfo;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 11.03.2021
 * Time: 20:24
 * Project: LabyCookies
 */

public class PremiumCheck implements Check {

    private NetworkPlayerInfo playerInfo;
    private boolean successful;

    @Override
    public void performCheck(NetworkPlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
        this.successful = playerInfo.getPlayerTeam().getColorPrefix().equals("§6");
    }

    @Override
    public boolean isCheckSuccessful() {
        return this.successful;
    }

    @Override
    public boolean ignore() {
        final String prefix = this.playerInfo.getPlayerTeam().getColorPrefix();
        if(prefix.equals("§a") || prefix.equals("§b") || prefix.contains("Dev") || prefix.contains("Mod") ||
                prefix.contains("Content") || prefix.contains("Sup") || prefix.contains("Admin")) return false;

        return prefix.length() > 2;
    }

    @Override
    public int getWeight() {
        return 25;
    }
}
