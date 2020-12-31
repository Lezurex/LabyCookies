package com.voxcrafterlp.statsaddon;

import com.google.common.collect.Lists;
import com.voxcrafterlp.statsaddon.events.MessageReceiveEventHandler;
import com.voxcrafterlp.statsaddon.events.ServerMessageEvent;
import net.labymod.api.LabyModAPI;
import net.labymod.api.LabyModAddon;
import net.labymod.settings.elements.*;
import net.labymod.utils.Consumer;
import net.labymod.utils.Material;
import net.labymod.utils.ModColor;

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

    public int cooldown;
    public int warnLevel;
    public boolean enabled;
    public boolean alertEnabled;
    public boolean lmcDoubled;
    public List<String> checkedPlayers;

    private static StatsAddon statsAddon;
    private boolean isPlayingCookies;
    private List<String> playersJoined = Lists.newCopyOnWriteArrayList();

    private Map<String, Boolean> enabledGamemods = new HashMap<>();

    @Override
    public void onEnable() {
        statsAddon = this;
        isPlayingCookies = false;

        //EVENT REGISTRATION
        new MessageReceiveEventHandler().register();
        new ServerMessageEvent().register();
        System.out.println("LabyCookies enabled");
        checkedPlayers = Lists.newCopyOnWriteArrayList();

    }

    @Override
    public void loadConfig() {
        this.enabled = !this.getConfig().has("enabled") || this.getConfig().get("enabled").getAsBoolean();
        this.alertEnabled = !this.getConfig().has("alertEnabled") || this.getConfig().get("alertEnabled").getAsBoolean();
        this.cooldown = this.getConfig().has("cooldown") ? this.getConfig().get("cooldown").getAsInt() : 1000;
        this.warnLevel = this.getConfig().has("warnLevel") ? this.getConfig().get("warnLevel").getAsInt() : 100;

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
        map.put("BedWars", Material.BED);
        map.put("Cores", Material.BEACON);
        map.put("JumpLeague", Material.DIAMOND_BOOTS);
        map.put("TTT", Material.STICK);
        map.put("SpeedUHC", Material.GOLDEN_APPLE);
        map.put("EnderGames", Material.ENDER_PEARL);
        map.put("MasterBuilders", Material.IRON_PICKAXE);
        map.put("SurvivalGames", Material.IRON_SWORD);
        map.put("QuickSurvivalGames", Material.IRON_SWORD);

        return map;
    }

    public static StatsAddon getStatsAddon() { return statsAddon; }

    @Override
    public LabyModAPI getApi() { return super.getApi(); }

    public boolean isPlayingCookies() { return isPlayingCookies; }

    public void setPlayingCookies(boolean playingCookies) { isPlayingCookies = playingCookies; }

    public List<String> getPlayersJoined() { return playersJoined; }

    public String getPrefix() { return "\u00A78[\u00A7bLabyCookies\u00A78] "; }
}
