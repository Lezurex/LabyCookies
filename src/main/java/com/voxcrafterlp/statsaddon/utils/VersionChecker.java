package com.voxcrafterlp.statsaddon.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.voxcrafterlp.statsaddon.StatsAddon;
import net.labymod.main.LabyMod;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentProcessor;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.ForgeHooks;

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

                JsonParser jsonParser = new JsonParser();
                JsonElement jsonElement = jsonParser.parse(content.toString());
                String tagName = jsonElement.getAsJsonObject().get("tag_name").getAsString();

                if(!tagName.equals(this.VERSION)) {
                    LabyMod.getInstance().displayMessageInChat(StatsAddon.getInstance().getPrefix() + "\u00A77Es ist eine \u00A7bneue Version \u00A77verfügbar. \u00A78(\u00A7b" + tagName + "\u00A78)");
                    ChatComponentText link = new ChatComponentText("\u00A78[\u00A7bLINK\u00A78]");
                    link.getChatStyle().setChatClickEvent(ForgeHooks.newChatWithLinks(jsonElement.getAsJsonObject().get("assets").getAsJsonArray().get(0).getAsJsonObject().get("browser_download_url").getAsString()).getChatStyle().getChatClickEvent());
                    ChatComponentText main = new ChatComponentText("");
                    main.appendText(StatsAddon.getInstance().getPrefix()).appendText("\u00A77Download\u00A78: ").appendSibling(link);
                    Minecraft.getMinecraft().thePlayer.addChatMessage(main);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
