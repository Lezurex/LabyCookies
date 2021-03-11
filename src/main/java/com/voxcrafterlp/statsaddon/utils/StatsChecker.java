package com.voxcrafterlp.statsaddon.utils;

import com.google.common.collect.Lists;
import com.voxcrafterlp.statsaddon.objects.PlayerStats;
import com.voxcrafterlp.statsaddon.threads.StatsCheckerThread;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 01.02.2021
 * Time: 08:20
 * Project: LabyCookies
 */

@Getter
public class StatsChecker {

    private final List<PlayerStats> checkedPlayers;
    private final List<PlayerStats> queue;
    private StatsCheckerThread statsCheckerThread;
    @Setter
    private PlayerStats lastRequested;

    public StatsChecker() {
        this.checkedPlayers = Lists.newCopyOnWriteArrayList();
        this.queue = Lists.newCopyOnWriteArrayList();
    }

    public void startChecker() {
        this.statsCheckerThread = new StatsCheckerThread();
        this.statsCheckerThread.start();
    }

    public void stopCheck() {
        if(this.statsCheckerThread != null)
            this.statsCheckerThread.stop();
    }

    public void addToQueue(PlayerStats playerStats) {
        if(!this.queue.contains(playerStats))
            this.queue.add(playerStats);
    }

}
