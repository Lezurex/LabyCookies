package com.voxcrafterlp.statsaddon.objects.alertRules;

import com.google.gson.JsonObject;
import com.voxcrafterlp.statsaddon.objects.PlayerStats;
import lombok.Getter;
import lombok.Setter;

import java.text.DecimalFormat;

public class StatsParam {

    @Getter
    @Setter
    private double value;
    private ParamType paramType;
    private CompareType compareType;

    public StatsParam(double value, ParamType paramType, CompareType compareType) {
        this.value = value;
        this.paramType = paramType;
        this.compareType = compareType;
    }

    public boolean check(PlayerStats playerStats) {
        System.out.println("Checking param: " + serialize().toString());
        switch (paramType) {
            case WINS:
                return compare(playerStats.getWins());
            case GAMES:
                return compare(playerStats.getPlayedGames());
            case WINRATE:
                return compare(playerStats.getWinRate());
            case RANK:
                return compare(playerStats.getRank());
            case COOKIES_PER_GAME:
                double cookiesPerGame = playerStats.getCookiesPerGame();
                if (cookiesPerGame == -1) {
                    return false;
                }
                return compare(playerStats.getCookiesPerGame());
            default:
                return false;
        }
    }

    private boolean compare(double statsValue) {
        switch (compareType) {
            case GREATER_THAN:
                return statsValue >= value;
            case LESS_THAN:
                return statsValue < value && statsValue > 0;
            default:
                return false;
        }
    }

    public JsonObject serialize() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("value", value);
        jsonObject.addProperty("paramType", paramType.name());
        jsonObject.addProperty("compareType", compareType.name());
        return jsonObject;
    }

    public static StatsParam deserialize(JsonObject jsonObject) throws IllegalArgumentException {
        ParamType paramType = ParamType.fromString(jsonObject.get("paramType").getAsString());
        CompareType compareType = CompareType.fromString(jsonObject.get("compareType").getAsString());
        if (paramType == null || compareType == null) {
            throw new IllegalArgumentException("One or both enums are invalid!");
        }
        return new StatsParam(jsonObject.get("value").getAsDouble(), paramType, compareType);
    }

    public String genHumanReadable() {
        DecimalFormat df = new DecimalFormat("###.##");
        return this.paramType.getHumanReadable() + " " + this.compareType.getHumanReadable() + " " + df.format(this.value);
    }

    public enum ParamType {
        WINS ("Wins"),
        WINRATE ("Winrate"),
        GAMES ("Spiele"),
        RANK ("Rang"),
        COOKIES_PER_GAME ("Cookies/Spiel");

        @Getter
        private final String humanReadable;

        ParamType(String humanReadable) {
            this.humanReadable = humanReadable;
        }

        public static ParamType fromString(String string) {
            for (ParamType value : ParamType.values()) {
                if (value.name().equals(string))
                    return value;
            }
            return null;
        }
    }

    public enum CompareType {
        GREATER_THAN (">="),
        LESS_THAN ("<");

        @Getter
        private final String humanReadable;

        CompareType(String humanReadable) {
            this.humanReadable = humanReadable;
        }

        public static CompareType fromString(String string) {
            for (CompareType value : CompareType.values()) {
                if (value.name().equals(string))
                    return value;
            }
            return null;
        }
    }
}
