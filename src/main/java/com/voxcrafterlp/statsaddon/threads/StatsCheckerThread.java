package com.voxcrafterlp.statsaddon.threads;

import com.voxcrafterlp.statsaddon.StatsAddon;
import com.voxcrafterlp.statsaddon.objects.PlayerStats;
import com.voxcrafterlp.statsaddon.utils.StatsChecker;
import net.labymod.main.LabyMod;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 09.02.2021
 * Time: 17:53
 * Project: LabyCookies
 */

public class StatsCheckerThread extends Thread {

    private final StatsChecker statsChecker;

    public StatsCheckerThread() { this.statsChecker = StatsAddon.getInstance().getStatsChecker(); }

    /**
     * Gets every {@link PlayerStats} object from the queue and performs a stats request
     */
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(StatsAddon.getInstance().getCooldown());
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }

            if(!this.statsChecker.getQueue().isEmpty()) {
                final PlayerStats playerStats = this.statsChecker.getQueue().get(0);
                if(!playerStats.isChecked()) {
                    this.statsChecker.setLastRequested(playerStats);
                    playerStats.performStatsCheck();
                    playerStats.setChecked(true);

                    if(!this.statsChecker.getCheckedPlayers().contains(playerStats))
                        this.statsChecker.getCheckedPlayers().add(playerStats);
                    this.statsChecker.getQueue().remove(playerStats);
                }
            }
        }
    }
}
