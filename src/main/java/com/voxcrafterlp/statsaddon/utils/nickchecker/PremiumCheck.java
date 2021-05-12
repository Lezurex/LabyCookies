package com.voxcrafterlp.statsaddon.utils.nickchecker;

import com.voxcrafterlp.statsaddon.utils.compatibility.CompatibilityLayer;
import com.voxcrafterlp.statsaddon.utils.compatibility.NPlayerInfo;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 11.03.2021
 * Time: 20:24
 * Project: LabyCookies
 */

public class PremiumCheck implements Check {

    private NPlayerInfo playerInfo;
    private boolean successful;

    @Override
    public void performCheck(NPlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
        this.successful = CompatibilityLayer.playerInfoGetPrefix(playerInfo).equals("§6");
    }

    @Override
    public boolean isCheckSuccessful() {
        return this.successful;
    }

    @Override
    public boolean ignore() {
        final String prefix = CompatibilityLayer.playerInfoGetPrefix(playerInfo);
        if(prefix.equals("§a") || prefix.equals("§bSupreme §7| ") || prefix.contains("Dev") || prefix.contains("Mod") ||
                prefix.contains("Content") || prefix.contains("Sup") || prefix.contains("Admin")) return false;

        return prefix.length() > 2;
    }

    @Override
    public int getWeight() {
        return 25;
    }
}
