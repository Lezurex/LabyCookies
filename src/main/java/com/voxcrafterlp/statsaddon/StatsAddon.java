package com.voxcrafterlp.statsaddon;

import com.google.common.collect.Lists;
import com.voxcrafterlp.statsaddon.events.*;
import net.labymod.api.LabyModAPI;
import net.labymod.api.LabyModAddon;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.NumberElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Consumer;
import net.labymod.utils.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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

    @Override
    public void onEnable() {
        statsAddon = this;
        isPlayingCookies = false;

        //EVENT REGISTRATION
        //new PluginMessageEventHandler().register();
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
    }

    @Override
    protected void fillSettings(List<SettingsElement> list) {
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
        }, this.enabled));
    }

    public static StatsAddon getStatsAddon() { return statsAddon; }

    @Override
    public LabyModAPI getApi() { return super.getApi(); }

    public boolean isPlayingCookies() { return isPlayingCookies; }

    public void setPlayingCookies(boolean playingCookies) { isPlayingCookies = playingCookies; }

    public List<String> getPlayersJoined() { return playersJoined; }

    public String getPrefix() { return "\u00A78[\u00A7bLabyCookies\u00A78] "; }
}
