package com.voxcrafterlp.statsaddon.objects.alertRules;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.voxcrafterlp.statsaddon.StatsAddon;
import com.voxcrafterlp.statsaddon.objects.PlayerStats;
import lombok.Getter;
import net.labymod.main.LabyMod;

import java.util.List;

public class AlertRuleManager {

    @Getter
    private final List<AlertRule> alertRules = Lists.newCopyOnWriteArrayList();

    public void loadConfig() {
        JsonArray jsonArray = StatsAddon.getInstance().getConfig().getAsJsonArray("alertRules");
        if (jsonArray != null) {
            jsonArray.forEach(jsonElement -> {
                alertRules.add(AlertRule.deserialize(jsonElement.getAsJsonObject()));
            });
        }
    }

    public void checkAll(PlayerStats playerStats) {
        System.out.println(alertRules.toString());
        for (AlertRule alertRule : alertRules) {
            // Only the first positive alarm has to be sent
            if (alertRule.check(playerStats)) break;
        }
    }
}
