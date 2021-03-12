package com.voxcrafterlp.statsaddon.objects;

import java.util.ArrayList;
import java.util.List;

public class Team {

    private String prefix;
    private final List<PlayerStats> playerStats = new ArrayList<>();

    public Team(String prefix) {
        this.prefix = prefix;
    }

    public void addPlayerStats(PlayerStats playerStats) {
        this.playerStats.add(playerStats);
    }

    public String getPrefix() {
        return prefix;
    }

    public List<PlayerStats> getPlayerStats() {
        return playerStats;
    }
}
