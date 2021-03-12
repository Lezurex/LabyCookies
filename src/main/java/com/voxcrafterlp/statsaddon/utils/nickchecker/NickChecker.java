package com.voxcrafterlp.statsaddon.utils.nickchecker;

import com.voxcrafterlp.statsaddon.objects.PlayerStats;
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

    private final PlayerStats playerStats;
    private final NetworkPlayerInfo playerInfo;
    private double result;

    public NickChecker(PlayerStats playerStats) {
        this.playerStats = playerStats;
        this.playerInfo = this.playerStats.getPlayerInfo();
    }

    /**
     * Returns a nick probability in percent based on different checks
     *
     * @return {@link Double} probability (0 - 100)
     */
    public void checkPlayer() {
        if(this.playerStats.getNickProbability() != -1) return;

        new DefaultSkinCheck().performCheck(playerInfo);
    }

}
