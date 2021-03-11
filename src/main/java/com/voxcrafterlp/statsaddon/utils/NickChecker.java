package com.voxcrafterlp.statsaddon.utils;

import lombok.Getter;
import net.minecraft.client.network.NetworkPlayerInfo;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 11.03.2021
 * Time: 18:30
 * Project: LabyCookies
 */

@Getter
public class NickChecker {

    private final NetworkPlayerInfo playerInfo;

    public NickChecker(NetworkPlayerInfo networkPlayerInfo) {
        this.playerInfo = networkPlayerInfo;
    }

    /**
     * Returns a nick probability in percent based on different checks
     *
     * @return {@link Double} probability (0 - 100)
     */
    public double checkPlayer() {
        return 0.0;
    }

    private boolean isInClan() {
        return false;
    }

    private boolean isPremium() {
        final String prefix = playerInfo.getPlayerTeam().getColorPrefix();
        System.out.println(prefix);

        return prefix.equals("§6");
    }

    private boolean hasDefaultSkin() {
        return false;
    }

    private boolean hasBadlion() {
        return false;
    }

}
