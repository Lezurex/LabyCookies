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
            if (alertRule.check(playerStats)) {
                playerStats.setWarned(true);
                break;
            }
        }
    }

    public void save() {
        if (StatsAddon.getInstance().getConfig().has("alertRules"))
            StatsAddon.getInstance().getConfig().remove("alertRules");
        StatsAddon.getInstance().getConfig().add("alertRules", new JsonArray());
        JsonArray jsonArray = StatsAddon.getInstance().getConfig().getAsJsonArray("alertRules");
        alertRules.forEach(alertRule -> {
            jsonArray.add(alertRule.serialize());
        });
        StatsAddon.getInstance().saveConfig();
    }
}
