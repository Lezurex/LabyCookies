package com.voxcrafterlp.statsaddon.utils.nickchecker;

import com.voxcrafterlp.statsaddon.objects.PlayerStats;
import com.voxcrafterlp.statsaddon.utils.compatibility.NetworkPlayerInfo;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 12.03.2021
 * Time: 18:49
 * Project: LabyCookies
 */

public class StatsCheck implements Check {

    private final PlayerStats playerStats;
    private boolean successful;

    public StatsCheck(PlayerStats playerStats) {
        this.playerStats = playerStats;
    }

    @Override
    public void performCheck(NetworkPlayerInfo playerInfo) {
        this.successful = (this.playerStats.getRank() > 500 || this.playerStats.getWinRate() < 15);
    }

    @Override
    public boolean isCheckSuccessful() {
        return this.successful;
    }

    @Override
    public boolean ignore() {
        return this.playerStats.getNickProbability() == 100;
    }

    @Override
    public int getWeight() {
        return 20;
    }
}
