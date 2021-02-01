package com.voxcrafterlp.statsaddon.utils;

import com.google.common.collect.Lists;
import com.voxcrafterlp.statsaddon.objects.PlayerStats;
import lombok.Getter;

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

    public StatsChecker() {
        this.checkedPlayers = Lists.newCopyOnWriteArrayList();
        this.queue = Lists.newCopyOnWriteArrayList();
    }

    public void startChecker() {
        //TODO
    }


    private boolean isAlreadyChecked(PlayerStats playerStats) {
        return this.checkedPlayers.contains(playerStats);
    }

    public void addToQueue(PlayerStats playerStats) {
        if(!this.queue.contains(playerStats))
            this.queue.add(playerStats);
    }

}
