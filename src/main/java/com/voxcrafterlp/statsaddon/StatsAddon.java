package com.voxcrafterlp.statsaddon;

import com.google.common.collect.Lists;
import com.voxcrafterlp.statsaddon.events.MessageReceiveEventHandler;
import com.voxcrafterlp.statsaddon.events.ServerMessageEvent;
import com.voxcrafterlp.statsaddon.utils.VersionChecker;
import net.labymod.api.LabyModAPI;
import net.labymod.api.LabyModAddon;
import net.labymod.gui.elements.DropDownMenu;
import net.labymod.settings.elements.*;
import net.labymod.utils.Consumer;
import net.labymod.utils.Material;
import net.labymod.utils.ModColor;
import net.labymod.utils.ServerData;
import net.minecraft.client.network.NetworkPlayerInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This file was created by VoxCrafter_LP & Lezurex!
 * Date: 06.09.2020
 * Time: 15:39
 * For Project: Labymod Stats Addon
 */

public class StatsAddon extends LabyModAddon {

    private String currentVersion = "v1.3.0";

    public int cooldown, warnLevel;
    public boolean enabled, alertEnabled, lmcDoubled;
    public List<NetworkPlayerInfo> checkedPlayers;

    private static StatsAddon statsAddon;
    private String currentGamemode, statsType;
    private List<NetworkPlayerInfo> playersJoined = Lists.newCopyOnWriteArrayList();

    private Map<String, Boolean> enabledGamemods = new HashMap<>();

    @Override
    public void onEnable() {
        statsAddon = this;
        currentGamemode = null;
        checkedPlayers = Lists.newCopyOnWriteArrayList();

        //EVENT REGISTRATION
        new MessageReceiveEventHandler().register();
        new ServerMessageEvent().register();

        this.getApi().getEventManager().registerOnJoin(new Consumer<ServerData>() {
            @Override
            public void accept(final ServerData serverData) {
                if(serverData.getIp().equalsIgnoreCase("gommehd.net") ||
                        serverData.getIp().equalsIgnoreCase("premium.gommehd.net") ||
                        serverData.getIp().equalsIgnoreCase("mc.gommehd.net") ||
                        serverData.getIp().equalsIgnoreCase("gommehd.com") ||
                        serverData.getIp().equalsIgnoreCase("premium.gommehd.com") ||
                        serverData.getIp().equalsIgnoreCase("mc.gommehd.com")) {

                    new Thread(() -> {
                        try {
                            Thread.sleep(2500);
                            new VersionChecker();
                        } catch (InterruptedException exception) {
                            exception.printStackTrace();
                        }
                    }).start();
                }
            }
        });

    }

    @Override
    public void loadConfig() {
        this.enabled = !this.getConfig().has("enabled") || this.getConfig().get("enabled").getAsBoolean();
        this.alertEnabled = !this.getConfig().has("alertEnabled") || this.getConfig().get("alertEnabled").getAsBoolean();
        this.cooldown = this.getConfig().has("cooldown") ? this.getConfig().get("cooldown").getAsInt() : 1000;
        this.warnLevel = this.getConfig().has("warnLevel") ? this.getConfig().get("warnLevel").getAsInt() : 100;
        this.statsType = this.getConfig().has("statstype") ? this.getConfig().get("statstype").getAsString() : "STATS 30 DAYS";

        this.getGamemodes().forEach((string, material) -> {
            if(enabledGamemods.containsKey(string))
                enabledGamemods.replace(string, (!this.getConfig().has(string) || this.getConfig().get(string).getAsBoolean()));
            else
                enabledGamemods.put(string, (!this.getConfig().has(string) || this.getConfig().get(string).getAsBoolean()));
        });
    }

    @Override
    protected void fillSettings(List<SettingsElement> list) {
        list.add(new HeaderElement(ModColor.cl('b') + "General settings"));

        list.add(new BooleanElement("Enabled", new ControlElement.IconData(Material.LEVER), new Consumer<Boolean>() {
            @Override
            public void accept(Boolean accepted) {
                enabled = accepted;
                getConfig().addProperty("enabled", accepted);
                saveConfig();
            }
        }, this.enabled));

        NumberElement queryInterval = new NumberElement("Query interval", new ControlElement.IconData(Material.WATCH), this.cooldown);
        queryInterval.addCallback(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                cooldown = integer;
                getConfig().addProperty("cooldown", integer);
                saveConfig();
            }
        });
        list.add(queryInterval);
        NumberElement warnLevelElement = new NumberElement("Warn Rank", new ControlElement.IconData(Material.NOTE_BLOCK), this.warnLevel);
        warnLevelElement.addCallback(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                warnLevel = integer;
                getConfig().addProperty("warnLevel", integer);
                saveConfig();
            }
        });
        list.add(warnLevelElement);
        list.add(new BooleanElement("Alert", new ControlElement.IconData(Material.NOTE_BLOCK), new Consumer<Boolean>() {
            @Override
            public void accept(Boolean accepted) {
                alertEnabled = accepted;
                getConfig().addProperty("alertEnabled", accepted);
                saveConfig();
            }
        }, this.alertEnabled));

        DropDownMenu<String> statsDropDownMenu = new DropDownMenu<String>("Statstype",0,0,0,0)
                .fill(new String[]{"STATSALL", "STATS 30 DAYS", "STATS 20 DAYS", "STATS 15 DAYS",
                        "STATS 10 DAYS", "STATS 5 DAYS", "STATS 3 DAYS"});
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
        list.add(new HeaderElement(ModColor.cl('a') + "Enabled gamemodes"));

        this.getGamemodes().forEach((string, material) -> {
            list.add(new BooleanElement(string, new ControlElement.IconData(material), new Consumer<Boolean>() {
                @Override
                public void accept(Boolean enabled) {
                    if(enabledGamemods.containsKey(string))
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
     * @return Filled map
     */
    private Map<String, Material> getGamemodes() {
        Map<String, Material> map = new HashMap<String, Material>();

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

    public static StatsAddon getInstance() { return statsAddon; }

    public void setCurrentGamemode(String currentGamemode) { this.currentGamemode = currentGamemode; }

    public String getCurrentGamemode() { return currentGamemode; }

    @Override
    public LabyModAPI getApi() { return super.getApi(); }

    public List<NetworkPlayerInfo> getPlayersJoined() { return playersJoined; }

    public Map<String, Boolean> getEnabledGamemods() { return enabledGamemods; }

    public String getGamemodePrefix() { return "\u00A78[\u00A7b" + getCurrentGamemode() + "-Stats\u00A78] "; }

    public String getPrefix() { return "\u00A78[\u00A7bStatsAddon\u00A78] "; }

    public String getCurrentVersion() { return currentVersion; }

    public String getStatsType() { return statsType; }
}
