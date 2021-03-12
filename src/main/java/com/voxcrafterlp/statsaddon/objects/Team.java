package com.voxcrafterlp.statsaddon.objects;

import com.google.common.collect.Lists;
import lombok.Getter;
import java.util.List;

@Getter
public class Team {

    private final String prefix;
    private final List<PlayerStats> playerStats = Lists.newCopyOnWriteArrayList();

    public Team(String prefix) {
        this.prefix = prefix;
    }

    public void addPlayerStats(PlayerStats playerStats) {
        this.playerStats.add(playerStats);
    }

}
