package com.voxcrafterlp.statsaddon.utils;

import com.voxcrafterlp.statsaddon.StatsAddon;
import net.labymod.main.LabyMod;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This file was created by VoxCrafter_LP & Lezurex!
 * Date: 31.12.2020
 * Time: 11:59
 * Project: LabyCookies
 */

public class VersionChecker {

    private final String VERSION;

    public VersionChecker() {
        this.VERSION = StatsAddon.getInstance().getCurrentVersion();
        this.checkForUpdates();
    }

    public void checkForUpdates() {
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

                String tagName = new JSONObject(content.toString()).getString("tag_name");

                if(!tagName.equals(this.VERSION))
                    LabyMod.getInstance().displayMessageInChat(StatsAddon.getInstance().getPrefix() + "\u00A77Es ist eine \u00A7bneue Version \u00A77verfügbar.");
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
