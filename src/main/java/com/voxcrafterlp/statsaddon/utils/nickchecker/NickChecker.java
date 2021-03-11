package com.voxcrafterlp.statsaddon.utils.nickchecker;

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
        new DefaultSkinCheck().performCheck(playerInfo);
        return 0.0;
    }

}
