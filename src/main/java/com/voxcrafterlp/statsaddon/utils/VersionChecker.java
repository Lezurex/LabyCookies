package com.voxcrafterlp.statsaddon.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.voxcrafterlp.statsaddon.StatsAddon;
import net.labymod.main.LabyMod;
import net.labymod.main.LabyModForge;
import net.minecraft.client.Minecraft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This file was created by VoxCrafter_LP and Lezurex!
 * Date: 31.12.2020
 * Time: 11:59
 * Project: LabyCookies
 */

public class VersionChecker {

    private final String version;

    public VersionChecker() {
        this.version = StatsAddon.getInstance().getCurrentVersion();
        if(StatsAddon.getInstance().isVersionCheckerEnabled())
            this.checkForUpdates();
    }

    /**
     * Connects to the Github api and gets the latest release.
     * Notifies the player in the chat and via a popup whether a new version
     * is available.
     */
    private void checkForUpdates() {
        new Thread(() -> {
            try {
                Thread.sleep(500);
                System.out.println("Checking for updates...");

                URL url = new URL("https://api.github.com/repos/Lezurex/LabyCookies/releases/latest");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = bufferedReader.readLine()) != null) {
                    content.append(inputLine);
                }
                bufferedReader.close();
                connection.disconnect();

                JsonParser jsonParser = new JsonParser();
                JsonElement jsonElement = jsonParser.parse(content.toString());
                String tagName = jsonElement.getAsJsonObject().get("tag_name").getAsString();
                String releaseURL = jsonElement.getAsJsonObject().get("html_url").getAsString();

                if(!tagName.equals(this.version)) {
                    LabyMod.getInstance().displayMessageInChat(StatsAddon.getInstance().getPrefix() + "\u00A77Es ist eine \u00A7bneue Version \u00A77verfügbar. \u00A78(\u00A7b" + tagName + "\u00A78)");

                    if (!Minecraft.getMinecraft().getVersion().contains("1.8")) {
                        if(jsonElement.getAsJsonObject().get("assets").getAsJsonArray().size() < 2) {
                            LabyMod.getInstance().displayMessageInChat(StatsAddon.getInstance().getPrefix() + "\u00A77 Momentan ist \u00A7ckeine neue Version \u00A77für die 1.12.2 verfügbar\u00A78.");
                            return;
                        }
                    }

                    LabyMod.getInstance().notifyMessageRaw("Update verfügbar", "Es ist eine neue Version verfügbar");

                    if(LabyModForge.isForge()) {

                    } else {
                        LabyMod.getInstance().displayMessageInChat(StatsAddon.getInstance().getPrefix() + "\u00A77Download\u00A78: [\u00A7b" + releaseURL + "\u00A78]");
                        LabyMod.getInstance().openWebpage(releaseURL, true);
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
