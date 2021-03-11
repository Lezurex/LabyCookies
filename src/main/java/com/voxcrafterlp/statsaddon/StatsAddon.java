package com.voxcrafterlp.statsaddon;

import com.voxcrafterlp.statsaddon.events.MessageReceiveEventHandler;
import com.voxcrafterlp.statsaddon.events.ServerMessageEvent;
import com.voxcrafterlp.statsaddon.objects.PlayerStats;
import com.voxcrafterlp.statsaddon.utils.StatsChecker;
import com.voxcrafterlp.statsaddon.utils.VersionChecker;
import com.voxcrafterlp.statsaddon.webserver.Webserver;
import lombok.Getter;
import lombok.Setter;
import net.labymod.api.LabyModAPI;
import net.labymod.api.LabyModAddon;
import net.labymod.gui.elements.DropDownMenu;
import net.labymod.settings.elements.*;
import net.labymod.utils.Consumer;
import net.labymod.utils.Material;
import net.labymod.utils.ModColor;
import net.labymod.utils.ServerData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This file was created by VoxCrafter_LP & Lezurex!
 * Date: 06.09.2020
 * Time: 15:39
 * For Project: LabyMod Stats Addon
 */

@Getter
public class StatsAddon extends LabyModAddon {

    /**
     * String in default semantic versioning syntax: vX.Y.Z
     */
    private final String currentVersion = "v2.0.0";

    private int cooldown, rankWarnLevel, winrateWarnLevel;
    @Setter
    private boolean enabled, alertEnabled, lmcDoubled, online;

    private static StatsAddon statsAddon;
    private String currentGamemode, statsType;

    private final Map<String, Boolean> enabledGamemods = new HashMap<>();

    private final Map<String, PlayerStats> loadedPlayerStats = new HashMap<>();
    private StatsChecker statsChecker;
    private Webserver webserver;

    @Override
    public void onEnable() {
        statsAddon = this;
        this.online = false;
        currentGamemode = null;

        //EVENT REGISTRATION
        new MessageReceiveEventHandler().register();
        new ServerMessageEvent().register();

        this.getApi().getEventManager().registerOnJoin(new Consumer<ServerData>() {
            @Override
            public void accept(final ServerData serverData) {
                if (serverData.getIp().equalsIgnoreCase("gommehd.net") ||
                        serverData.getIp().equalsIgnoreCase("premium.gommehd.net") ||
                        serverData.getIp().equalsIgnoreCase("mc.gommehd.net") ||
                        serverData.getIp().equalsIgnoreCase("gommehd.com") ||
                        serverData.getIp().equalsIgnoreCase("premium.gommehd.com") ||
                        serverData.getIp().equalsIgnoreCase("mc.gommehd.com")) {

                    online = true;

                    new Thread(() -> {
                        try {
                            Thread.sleep(2000);
                            new VersionChecker();
                        } catch (InterruptedException exception) {
                            exception.printStackTrace();
                        }
                    }).start();
                } else
                    online = false;
            }
        });

        this.statsChecker = new StatsChecker();
        new Thread(() -> {
            System.out.println("Starting webserver..");
            webserver = new Webserver();
        }).start();
    }

    @Override
    public void loadConfig() {
        // Load config or set default values
        this.enabled = !this.getConfig().has("enabled") || this.getConfig().get("enabled").getAsBoolean();
        this.alertEnabled = !this.getConfig().has("alertEnabled") || this.getConfig().get("alertEnabled").getAsBoolean();
        this.cooldown = this.getConfig().has("cooldown") ? this.getConfig().get("cooldown").getAsInt() : 1000;
        this.rankWarnLevel = this.getConfig().has("rankWarnLevel") ? this.getConfig().get("rankWarnLevel").getAsInt() : 100;
        this.winrateWarnLevel = this.getConfig().has("winrateWarnLevel") ? this.getConfig().get("winrateWarnLevel").getAsInt() : 40;
        this.statsType = this.getConfig().has("statstype") ? this.getConfig().get("statstype").getAsString() : "STATS 30 DAYS";

        this.getGamemodes().forEach((string, material) -> {
            if (enabledGamemods.containsKey(string))
                enabledGamemods.replace(string, (!this.getConfig().has(string) || this.getConfig().get(string).getAsBoolean()));
            else
                enabledGamemods.put(string, (!this.getConfig().has(string) || this.getConfig().get(string).getAsBoolean()));
        });
    }

    @Override
    protected void fillSettings(List<SettingsElement> list) {
        list.add(new HeaderElement(ModColor.cl('b') + "Allgemeine Einstellungen"));

        list.add(new BooleanElement("Enabled", new ControlElement.IconData(Material.LEVER), new Consumer<Boolean>() {
            @Override
            public void accept(Boolean accepted) {
                enabled = accepted;
                getConfig().addProperty("enabled", accepted);
                saveConfig();
            }
        }, this.enabled));

        NumberElement queryInterval = new NumberElement("Abfragenintervall", new ControlElement.IconData(Material.WATCH), this.cooldown);
        queryInterval.addCallback(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                cooldown = integer;
                getConfig().addProperty("cooldown", integer);
                saveConfig();
            }
        });
        list.add(queryInterval);
        NumberElement rankWarnLevelElement = new NumberElement("Warn Rank", new ControlElement.IconData(Material.NOTE_BLOCK), this.rankWarnLevel);
        rankWarnLevelElement.addCallback(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                rankWarnLevel = integer;
                getConfig().addProperty("rankWarnLevel", integer);
                saveConfig();
            }
        });
        list.add(rankWarnLevelElement);
        NumberElement winrateWarnLevelElement = new NumberElement("Warn Winrate", new ControlElement.IconData(Material.NOTE_BLOCK), this.winrateWarnLevel);
        winrateWarnLevelElement.addCallback(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                winrateWarnLevel = integer;
                getConfig().addProperty("winrateWarnLevel", integer);
                saveConfig();
            }
        });
        list.add(winrateWarnLevelElement);
        list.add(new BooleanElement("Alarm", new ControlElement.IconData(Material.NOTE_BLOCK), new Consumer<Boolean>() {
            @Override
            public void accept(Boolean accepted) {
                alertEnabled = accepted;
                getConfig().addProperty("alertEnabled", accepted);
                saveConfig();
            }
        }, this.alertEnabled));

        DropDownMenu<String> statsDropDownMenu = new DropDownMenu<String>("Statstyp", 0, 0, 0, 0)
                .fill(new String[]{"STATSALL", "STATS 30 TAGE", "STATS 20 TAGE", "STATS 15 TAGE",
                        "STATS 10 TAGE", "STATS 5 TGAE", "STATS 3 TAGE"});
        DropDownElement<String> statsDropDown = new DropDownElement<String>("Statstype", statsDropDownMenu);

        statsDropDownMenu.setSelected(this.statsType);
        statsDropDown.setChangeListener(new Consumer<String>() {
            @Override
            public void accept(String string) {
                statsType = string;
                getConfig().addProperty("statstype", string);
                saveConfig();
            }
        });

        list.add(statsDropDown);

        //Gamemodes
        list.add(new HeaderElement(ModColor.cl('a') + "Aktivierte Spielmodi"));

        this.getGamemodes().forEach((string, material) -> {
            list.add(new BooleanElement(string, new ControlElement.IconData(material), new Consumer<Boolean>() {
                @Override
                public void accept(Boolean enabled) {
                    if (enabledGamemods.containsKey(string))
                        enabledGamemods.replace(string, enabled);
                    else
                        enabledGamemods.put(string, enabled);
                    getConfig().addProperty(string, enabled);
                    saveConfig();
                }
            }, enabledGamemods.get(string)));
        });
    }

    /**
     * Fills the map with a list of all gamemodes which have stats enabled
     *
     * @return Filled {@link Map} with the name as key and the icon ({@link Material}) as the value.
     */
    private Map<String, Material> getGamemodes() {
        Map<String, Material> map = new HashMap<>();

        map.put("Cookies", Material.COOKIE);
        map.put("SkyWars", Material.GRASS);
        map.put("BW", Material.BED);
        map.put("Cores", Material.BEACON);
        map.put("JL", Material.DIAMOND_BOOTS);
        map.put("TTT", Material.STICK);
        map.put("SpeedUHC", Material.GOLDEN_APPLE);
        map.put("EG", Material.ENDER_PEARL);
        map.put("MasterBuilders", Material.IRON_PICKAXE);
        map.put("SG", Material.IRON_SWORD);
        map.put("QSG", Material.IRON_SWORD);

        return map;
    }

    public void clearCache() {
        getLoadedPlayerStats().clear();
        getStatsChecker().getCheckedPlayers().clear();
        getStatsChecker().getQueue().clear();
    }

    public static StatsAddon getInstance() {
        return statsAddon;
    }

    public void setCurrentGamemode(String currentGamemode) {
        this.currentGamemode = currentGamemode;
    }

    @Override
    public LabyModAPI getApi() {
        return super.getApi();
    }

    public String getGamemodePrefix() {
        return "\u00A78[\u00A7b" + getCurrentGamemode() + "-Stats\u00A78] ";
    }

    public String getPrefix() {
        return "\u00A78[\u00A7bStatsAddon\u00A78] ";
    }

}
