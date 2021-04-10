package com.voxcrafterlp.statsaddon.objects.alertRules;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.voxcrafterlp.statsaddon.StatsAddon;
import com.voxcrafterlp.statsaddon.objects.PlayerStats;
import net.labymod.main.LabyMod;

import java.util.List;

public class AlertRule {

    private final List<StatsParam> statsParams;

    public AlertRule(List<StatsParam> statsParams) {
        this.statsParams = statsParams;
    }

    public boolean check(PlayerStats playerStats) {
        System.out.println(serialize().toString());
        boolean failed = false;
        for (StatsParam statsParam : statsParams) {
            if (!statsParam.check(playerStats))
                failed = true;
            if (failed) break;
        }
        if (!failed) {
            LabyMod.getInstance().displayMessageInChat(StatsAddon.getInstance()
                    .getGamemodePrefix() + "\u00A77Regel für Spieler \u00A7c" + playerStats
                    .getPlayerName() + "\u00A77 ausgelöst\u00A78!");
            return true;
        }
        return false;
    }

    public void addParam(StatsParam statsParam) {
        statsParams.add(statsParam);
    }

    public JsonObject serialize() {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        for (StatsParam statsParam : statsParams) {
            jsonArray.add(statsParam.serialize());
        }
        jsonObject.add("params", jsonArray);
        return jsonObject;
    }

    public static AlertRule deserialize(JsonObject jsonObject) {
        JsonArray jsonArray = jsonObject.getAsJsonArray("params");
        List<StatsParam> statsParams = Lists.newCopyOnWriteArrayList();
        jsonArray.forEach(param -> {
            try {
                statsParams.add(StatsParam.deserialize(param.getAsJsonObject()));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        });
        return new AlertRule(statsParams);
    }
}
