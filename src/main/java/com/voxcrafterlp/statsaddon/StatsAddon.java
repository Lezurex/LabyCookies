package com.voxcrafterlp.statsaddon;

import com.voxcrafterlp.statsaddon.events.MessageReceiveEventHandler;
import com.voxcrafterlp.statsaddon.events.ServerMessageEvent;
import com.voxcrafterlp.statsaddon.events.TickListener;
import com.voxcrafterlp.statsaddon.objects.PlayerStats;
import com.voxcrafterlp.statsaddon.utils.KeyPressUtil;
import com.voxcrafterlp.statsaddon.utils.StatsChecker;
import com.voxcrafterlp.statsaddon.utils.VersionChecker;
import com.voxcrafterlp.statsaddon.webserver.Webserver;
import lombok.Getter;
import lombok.Setter;
import net.labymod.api.LabyModAPI;
import net.labymod.api.LabyModAddon;
import net.labymod.gui.elements.DropDownMenu;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.*;
import net.labymod.utils.Consumer;
import net.labymod.utils.Material;
import net.labymod.utils.ModColor;
import net.labymod.utils.ServerData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This file was created by VoxCrafter_LP and Lezurex!
 * Date: 06.09.2020
 * Time: 15:39
 * For Project: Labymod Stats Addon
 */

@Getter
public class StatsAddon extends LabyModAddon {

    /**
     * String in default semantic versioning syntax: vX.Y.Z
     */
    private final String currentVersion = "v2.0.0";

    private final String[] allowedPreReleaseUUIDs = new String[] {
            "4f08412d-5e85-46ec-89fd-028c1ed073a3",
            "20c018b7-970b-4eac-bbeb-713e72503f05",
    };

    @Getter
    private static StatsAddon instance;
    private final Map<String, Boolean> enabledGamemods = new HashMap<>();
    private final Map<String, PlayerStats> loadedPlayerStats = new HashMap<>();
    private StatsChecker statsChecker;
    private Webserver webserver;
    private KeyPressUtil keyPressUtil;
    @Setter
    private boolean lmcDoubled, online;

    @Setter
    private String currentGamemode, statsType;
    private int cooldown, rankWarnLevel, winrateWarnLevel, cookiesPerGameWarnLevel, reloadStatsKey;
    private boolean enabled, alertEnabled, versionCheckerEnabled, showStatsMessages;

    @Override
    public void onEnable() {
//         PreRelease Checker
//        boolean isValidUUID = false;
//        String uuid = null;
//        while (uuid == null) {
//            try {
//                uuid = LabyMod.getInstance().getPlayerUUID().toString();
//            } catch (NullPointerException exception) {
//
//            }
//        }
//        System.out.println("User UUID is: " + uuid);
//        for (String candidate : allowedPreReleaseUUIDs) {
//            System.out.println("Checking UUID: " + candidate);
//            if (candidate.equals(uuid)) {
//                isValidUUID = true;
//                System.out.println("Breaking.");
//                break;
//            }
//        }
//
//        if (!isValidUUID) {
//            System.out.println("Addon not loading. PreRelease violation found!");
//            return;
//        }
//         PreRelease Checker end

        instance = this;
        this.online = false;
        this.currentGamemode = null;
        this.lmcDoubled = false;

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
                            LabyMod.getInstance().displayMessageInChat(getPrefix() + "\u00A77Das Webinterface ist hier erreichbar: \u00A78[\u00A7bhttp://localhost:" + getWebserver().getWebserver().getAddress().getPort() + "\u00A78]");
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
            this.webserver = new Webserver();
        }).start();
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                keyPressUtil = new KeyPressUtil();
                getApi().registerForgeListener(new TickListener());
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }).start();
    }

    @Override
    public void loadConfig() {
        // Load config or set default values
        this.enabled = !this.getConfig().has("enabled") || this.getConfig().get("enabled").getAsBoolean();
        this.alertEnabled = !this.getConfig().has("alertEnabled") || this.getConfig().get("alertEnabled").getAsBoolean();
        this.versionCheckerEnabled = !this.getConfig().has("versionCheckerEnabled") || this.getConfig().get("versionCheckerEnabled").getAsBoolean();
        this.cooldown = this.getConfig().has("cooldown") ? this.getConfig().get("cooldown").getAsInt() : 1000;
        this.rankWarnLevel = this.getConfig().has("rankWarnLevel") ? this.getConfig().get("rankWarnLevel").getAsInt() : 100;
        this.winrateWarnLevel = this.getConfig().has("winrateWarnLevel") ? this.getConfig().get("winrateWarnLevel").getAsInt() : 40;
        this.cookiesPerGameWarnLevel = this.getConfig().has("cookiesPerGameWarnLevel") ? this.getConfig().get("cookiesPerGameWarnLevel").getAsInt() : 1200;
        this.statsType = this.getConfig().has("statstype") ? this.getConfig().get("statstype").getAsString() : "STATS 30 TAGE";
        this.reloadStatsKey = this.getConfig().has("reloadStatsKey") ? this.getConfig().get("reloadStatsKey").getAsInt() : 34; //Default: G
        this.showStatsMessages = !this.getConfig().has("showStatsMessages") || this.getConfig().get("showStatsMessages").getAsBoolean();

        this.getGamemodes().forEach((string, material) -> {
            if(enabledGamemods.containsKey(string))
                enabledGamemods.replace(string, (!this.getConfig().has(string) || this.getConfig().get(string).getAsBoolean()));
            else
                enabledGamemods.put(string, (!this.getConfig().has(string) || this.getConfig().get(string).getAsBoolean()));
        });
    }

    @Override
    protected void fillSettings(List<SettingsElement> list) {
        list.add(new HeaderElement(ModColor.cl('b') + "Allgemeine Einstellungen"));

        list.add(new BooleanElement("Aktiviert", new ControlElement.IconData(Material.LEVER), new Consumer<Boolean>() {
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
        NumberElement rankWarnLevelElement = new NumberElement("Warn Rang", new ControlElement.IconData("labymod/textures/addons/statsaddon/rankAlert.png"), this.rankWarnLevel);
        rankWarnLevelElement.addCallback(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                rankWarnLevel = integer;
                getConfig().addProperty("rankWarnLevel", integer);
                saveConfig();
            }
        });
        list.add(rankWarnLevelElement);
        NumberElement winrateWarnLevelElement = new NumberElement("Warn Winrate", new ControlElement.IconData("labymod/textures/addons/statsaddon/winrateAlert.png"), this.winrateWarnLevel);
        winrateWarnLevelElement.addCallback(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                winrateWarnLevel = integer;
                getConfig().addProperty("winrateWarnLevel", integer);
                saveConfig();
            }
        });
        list.add(winrateWarnLevelElement);
        NumberElement cookiesPerGameWarnLevelElement = new NumberElement("Warn Cookies/Spiel", new ControlElement.IconData("labymod/textures/addons/statsaddon/cookiesAlert.png"), this.cookiesPerGameWarnLevel);
        cookiesPerGameWarnLevelElement.addCallback(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                cookiesPerGameWarnLevel = integer;
                getConfig().addProperty("cookiesPerGameWarnLevel", integer);
                saveConfig();
            }
        });
        list.add(cookiesPerGameWarnLevelElement);
        list.add(new BooleanElement("Alarm", new ControlElement.IconData(Material.NOTE_BLOCK), new Consumer<Boolean>() {
            @Override
            public void accept(Boolean accepted) {
                alertEnabled = accepted;
                getConfig().addProperty("alertEnabled", accepted);
                saveConfig();
            }
        }, this.alertEnabled));
        list.add(new BooleanElement("Updater", new ControlElement.IconData("labymod/textures/addons/statsaddon/updateChecker.png"), new Consumer<Boolean>() {
            @Override
            public void accept(Boolean accepted) {
                versionCheckerEnabled = accepted;
                getConfig().addProperty("versionCheckerEnabled", accepted);
                saveConfig();
            }
        }, this.versionCheckerEnabled));
        list.add(new KeyElement("Stats erneut abfragen",
                new ControlElement.IconData(("labymod/textures/addons/statsaddon/statsReload.png")),
                reloadStatsKey, new Consumer<Integer>() {
            @Override
            public void accept(Integer key) {
                reloadStatsKey = key;
                keyPressUtil.registerKeys(); //Update hotkeys
                getConfig().addProperty("reloadStatsKey", reloadStatsKey);
                saveConfig();
            }
        }));
        list.add(new BooleanElement("Stats-Nachrichten anzeigen", new ControlElement.IconData("labymod/textures/addons/statsaddon/showStatsMessages.png"), new Consumer<Boolean>() {
            @Override
            public void accept(Boolean accepted) {
                showStatsMessages = accepted;
                getConfig().addProperty("showStatsMessages", accepted);
                saveConfig();
            }
        }, this.showStatsMessages));

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

    /**
     * Clears all the cached information about loaded players
     */
    public void clearCache() {
        this.getLoadedPlayerStats().clear();
        this.getStatsChecker().getCheckedPlayers().clear();
        this.getStatsChecker().getQueue().clear();
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
