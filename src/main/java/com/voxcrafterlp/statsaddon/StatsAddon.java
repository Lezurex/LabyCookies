package com.voxcrafterlp.statsaddon;

import com.google.common.collect.Lists;
import com.voxcrafterlp.statsaddon.events.MessageReceiveEventHandler;
import com.voxcrafterlp.statsaddon.events.PluginMessageEventHandler;
import net.labymod.api.LabyModAPI;
import net.labymod.api.LabyModAddon;
import net.labymod.settings.elements.SettingsElement;

import java.util.ArrayList;
import java.util.List;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 06.09.2020
 * Time: 15:39
 * For Project: Labymod Stats Addon
 */

public class StatsAddon extends LabyModAddon {

    private static StatsAddon statsAddon;
    private boolean isPlayingCookies;
    private List<String> playersJoined = Lists.newCopyOnWriteArrayList();

    @Override
    public void onEnable() {
        statsAddon = this;
        isPlayingCookies = false;

        //EVENT REGISTRATION
        new PluginMessageEventHandler().register();
        new MessageReceiveEventHandler().register();
    }

    @Override
    public void loadConfig() {

    }

    @Override
    protected void fillSettings(List<SettingsElement> list) {

    }

    public static StatsAddon getStatsAddon() { return statsAddon; }

    @Override
    public LabyModAPI getApi() { return super.getApi(); }

    public boolean isPlayingCookies() { return isPlayingCookies; }

    public void setPlayingCookies(boolean playingCookies) { isPlayingCookies = playingCookies; }

    public List<String> getPlayersJoined() { return playersJoined; }

    public String getPrefix() { return "\u00A78[\u00A7bLabyCookies\u00A78] "; }
}
