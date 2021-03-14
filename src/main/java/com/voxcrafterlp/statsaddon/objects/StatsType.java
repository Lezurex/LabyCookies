package com.voxcrafterlp.statsaddon.objects;

import lombok.Getter;

public enum StatsType {

    STATS30 ("STATS 30 TAGE", "/stats"),
    STATS20 ("STATS 20 TAGE", "/statsd 20"),
    STATS15 ("STATS 15 TAGE", "/statsd 15"),
    STATS10 ("STATS 10 TAGE", "/statsd 10"),
    STATS5 ("STATS 5 TAGE", "/statsd 5"),
    STATS3 ("STATS 3 TAGE", "/statsd 3"),
    STATS1 ("STATS 1 TAG", "/statsd 1"),
    STATSALL ("STATSALL", "/statsall");

    @Getter
    private final String configName;
    @Getter
    private final String commandName;

    StatsType(String configName, String commandName) {
        this.configName = configName;
        this.commandName = commandName;
    }

    /**
     * Loops through each of the enums and returns the enum with the corresponding config name.
     * @param configName The name of the enum as written in the configuration
     * @return The corresponding {@link StatsType} or null if no result was found
     */
    public static StatsType fromConfigName(String configName) {
        for (StatsType statsType : StatsType.values()) {
            if (statsType.configName.equals(configName)) {
                return statsType;
            }
        }
        return null;
    }


}
