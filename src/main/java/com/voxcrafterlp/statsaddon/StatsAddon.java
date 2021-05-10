package com.voxcrafterlp.statsaddon;

import com.google.common.collect.Lists;
import com.voxcrafterlp.statsaddon.events.JoinEventHandler;
import com.voxcrafterlp.statsaddon.events.MessageReceiveEventHandler;
import com.voxcrafterlp.statsaddon.events.ServerMessageEventHandler;
import com.voxcrafterlp.statsaddon.events.TickListener;
import com.voxcrafterlp.statsaddon.objects.PlayerStats;
import com.voxcrafterlp.statsaddon.objects.StatsType;
import com.voxcrafterlp.statsaddon.objects.alertRules.AlertRuleManager;
import com.voxcrafterlp.statsaddon.utils.KeyPressUtil;
import com.voxcrafterlp.statsaddon.utils.StatsChecker;
import com.voxcrafterlp.statsaddon.utils.compatibility.CompatibilityLayer;
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
    private final String currentVersion = "v2.1.0";
    private final boolean isPreRelease = false;

    private final String[] allowedPreReleaseUUIDs = new String[] {
            "4f08412d-5e85-46ec-89fd-028c1ed073a3",
            "20c018b7-970b-4eac-bbeb-713e72503f05"
    };

    @Getter
    private static StatsAddon instance;
    private final Map<String, Boolean> enabledGamemods = new HashMap<>();
    private final Map<String, PlayerStats> loadedPlayerStats = new HashMap<>();
    private StatsChecker statsChecker;
    private Webserver webserver;
    private KeyPressUtil keyPressUtil;
    private AlertRuleManager alertRuleManager;
    @Setter
    private boolean lmcDoubled, online, websiteMessageShown;

    @Setter
    private String currentGamemode;
    private StatsType statsType;
    private int cooldown, rankWarnLevel, winrateWarnLevel, cookiesPerGameWarnLevel, reloadStatsKey;
    private boolean enabled, webserverEnabled, alertEnabled, versionCheckerEnabled, showStatsMessages;

    @Override
    public void onEnable() {

        if (!checkPreRelease())
            return;

        instance = this;
        this.online = false;
        this.currentGamemode = null;
        this.lmcDoubled = false;

        //EVENT REGISTRATION
        CompatibilityLayer.registerMessageReceive(MessageReceiveEventHandler.class);
        CompatibilityLayer.registerServerMessage(ServerMessageEventHandler.class);
        CompatibilityLayer.registerOnJoin(JoinEventHandler.class);

        this.statsChecker = new StatsChecker();
        this.alertRuleManager = new AlertRuleManager();

        new Thread(() -> {
            System.out.println("Starting webserver..");
            this.webserver = new Webserver();
        }).start();
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                keyPressUtil = new KeyPressUtil();
                CompatibilityLayer.registerListener(new TickListener());
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }).start();
    }

    /**
     * @return Whether or not the addon is allowed to load
     */
    private boolean checkPreRelease() {
        if (isPreRelease) {
            boolean isValidUUID = false;
            String uuid = null;
            while (uuid == null) {
                try {
                    uuid = LabyMod.getInstance().getPlayerUUID().toString();
                } catch (NullPointerException exception) {

                }
            }
            System.out.println("User UUID is: " + uuid);
            for (String candidate : allowedPreReleaseUUIDs) {
                System.out.println("Checking UUID: " + candidate);
                if (candidate.equals(uuid)) {
                    isValidUUID = true;
                    System.out.println("Breaking.");
                    break;
                }
            }

            if (!isValidUUID) {
                System.out.println("Addon not loading. PreRelease violation found!");
                return false;
            }
        }
        return true;
    }

    @Override
    public void loadConfig() {
        // Load config or set default values
        this.enabled = !this.getConfig().has("enabled") || this.getConfig().get("enabled").getAsBoolean();
        this.webserverEnabled = !this.getConfig().has("webserverEnabled") || this.getConfig().get("webserverEnabled").getAsBoolean();
        this.alertEnabled = !this.getConfig().has("alertEnabled") || this.getConfig().get("alertEnabled").getAsBoolean();
        this.versionCheckerEnabled = !this.getConfig().has("versionCheckerEnabled") || this.getConfig().get("versionCheckerEnabled").getAsBoolean();
        this.cooldown = this.getConfig().has("cooldown") ? this.getConfig().get("cooldown").getAsInt() : 1000;
        this.rankWarnLevel = this.getConfig().has("rankWarnLevel") ? this.getConfig().get("rankWarnLevel").getAsInt() : 100;
        this.winrateWarnLevel = this.getConfig().has("winrateWarnLevel") ? this.getConfig().get("winrateWarnLevel").getAsInt() : 40;
        this.cookiesPerGameWarnLevel = this.getConfig().has("cookiesPerGameWarnLevel") ? this.getConfig().get("cookiesPerGameWarnLevel").getAsInt() : 1200;
        this.statsType = this.getConfig().has("statstype") ? StatsType.fromConfigName(this.getConfig().get("statstype").getAsString())  : StatsType.STATS30;
        this.reloadStatsKey = this.getConfig().has("reloadStatsKey") ? this.getConfig().get("reloadStatsKey").getAsInt() : 34; //Default: G
        this.showStatsMessages = !this.getConfig().has("showStatsMessages") || this.getConfig().get("showStatsMessages").getAsBoolean();

        this.getGamemodes().forEach((string, material) -> {
            if(enabledGamemods.containsKey(string))
                enabledGamemods.replace(string, (!this.getConfig().has(string) || this.getConfig().get(string).getAsBoolean()));
            else
                enabledGamemods.put(string, (!this.getConfig().has(string) || this.getConfig().get(string).getAsBoolean()));
        });

        this.alertRuleManager.loadConfig();
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
        list.get(1).setDescriptionText("Aktiviert oder deaktiviert das Addon komplett.");
        list.add(new BooleanElement("Webseite", new ControlElement.IconData("labymod/textures/addons/statsaddon/websiteEnabled.png"), new Consumer<Boolean>() {
            @Override
            public void accept(Boolean accepted) {
                webserverEnabled = accepted;
                getConfig().addProperty("webserverEnabled", accepted);
                saveConfig();
                if (accepted) {
                    final String url = "http://localhost:" +webserver.getWebserver().getAddress().getPort() + "/";
                    LabyMod.getInstance().displayMessageInChat(getPrefix() + "\u00A77Die Webseite wurde \u00A7bgeöffnet\u00A78. " +
                            "\u00A77Alternativ ist diese aber auch über diesen \u00A7bLink \u00A77zu erreichen\u00A78: " +
                            "[\u00A7b" + url + "\u00A78]");
                    LabyMod.getInstance().openWebpage(url, false);
                    websiteMessageShown = true;
                }
            }
        }, this.webserverEnabled));
        list.get(2).setDescriptionText("Öffne die Webseite beim ersten Join auf de Server automatisch.");

        NumberElement queryInterval = new NumberElement("Abfrageintervall", new ControlElement.IconData(Material.WATCH), this.cooldown);
        queryInterval.addCallback(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                cooldown = integer;
                getConfig().addProperty("cooldown", integer);
                saveConfig();
            }
        });
        queryInterval.setDescriptionText("Wie viel Zeit (in Millisekunden) zwischen den einzelnen Stats-Abfragen gewartet werden soll. Unter 750ms ist nicht empfohlen!");
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
        rankWarnLevelElement.setDescriptionText("Wenn die Position im Ranking eines Gegners unter diesen Wert fällt, wirst du gewarnt.");
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
        winrateWarnLevelElement.setDescriptionText("Wenn die Winrate eines Gegners diesen Wert übersteigt, wirst du gewarnt.");
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
        cookiesPerGameWarnLevelElement.setDescriptionText("Wenn die durchschnittliche Anzahl Cookies pro Spiel eines Gegners diesen Wert übersteigt, wirst du gewarnt. (Nur in Cookies verfügbar)");
        list.add(cookiesPerGameWarnLevelElement);
        list.add(new BooleanElement("Alarm", new ControlElement.IconData(Material.NOTE_BLOCK), new Consumer<Boolean>() {
            @Override
            public void accept(Boolean accepted) {
                alertEnabled = accepted;
                getConfig().addProperty("alertEnabled", accepted);
                saveConfig();
            }
        }, this.alertEnabled));
        BooleanElement updateCheckerElement = new BooleanElement("Updater", new ControlElement.IconData("labymod/textures/addons/statsaddon/updateChecker.png"), new Consumer<Boolean>() {
            @Override
            public void accept(Boolean accepted) {
                versionCheckerEnabled = accepted;
                getConfig().addProperty("versionCheckerEnabled", accepted);
                saveConfig();
            }
        }, this.versionCheckerEnabled);
        updateCheckerElement.setDescriptionText("Wenn aktiv, wird das Addon regelmäßig nach Updates suchen und dich benachrichtigen, falls eine neue Version verfügbar sein sollte.");
        list.add(updateCheckerElement);
        KeyElement reloadStatsKeyElement = new KeyElement("Stats erneut abfragen",
                new ControlElement.IconData(("labymod/textures/addons/statsaddon/statsReload.png")),
                reloadStatsKey, new Consumer<Integer>() {
            @Override
            public void accept(Integer key) {
                reloadStatsKey = key;
                keyPressUtil.registerKeys(); //Update hotkeys
                getConfig().addProperty("reloadStatsKey", reloadStatsKey);
                saveConfig();
            }
        });
        reloadStatsKeyElement.setDescriptionText("Beim drücken dieser Taste werden alle Stats in einer Runde neu geladen.");
        list.add(reloadStatsKeyElement);
        BooleanElement showStatsMessagesElement = new BooleanElement("Stats-Nachrichten anzeigen", new ControlElement.IconData("labymod/textures/addons/statsaddon/showStatsMessages.png"), new Consumer<Boolean>() {
            @Override
            public void accept(Boolean accepted) {
                showStatsMessages = accepted;
                getConfig().addProperty("showStatsMessages", accepted);
                saveConfig();
            }
        }, this.showStatsMessages);
        showStatsMessagesElement.setDescriptionText("Ist diese Option aktiv, werden die Stats-Nachrichten im Chat nicht mehr angezeigt. (Experimentelles Feature)");
        list.add(showStatsMessagesElement);

        List<String> dropdownEntries = Lists.newCopyOnWriteArrayList();
        for (StatsType statsType : StatsType.values()) {
            dropdownEntries.add(statsType.getConfigName());
        }
        DropDownMenu<String> statsDropDownMenu = new DropDownMenu<String>("Statstyp", 0, 0, 0, 0)
                .fill(dropdownEntries.toArray(new String[0]));
        DropDownElement<String> statsDropDown = new DropDownElement<String>("Statstype", statsDropDownMenu);

        statsDropDownMenu.setSelected(this.statsType.getConfigName());
        statsDropDown.setChangeListener(new Consumer<String>() {
            @Override
            public void accept(String string) {
                statsType = StatsType.fromConfigName(string);
                getConfig().addProperty("statstype", string);
                saveConfig();
            }
        });
        statsDropDown.setDescriptionText("Wähle den bevorzugten Statstyp aus, den das Addon benutzen soll.");
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
