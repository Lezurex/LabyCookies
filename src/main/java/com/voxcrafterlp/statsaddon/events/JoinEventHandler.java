package com.voxcrafterlp.statsaddon.events;

import com.voxcrafterlp.statsaddon.StatsAddon;
import com.voxcrafterlp.statsaddon.utils.VersionChecker;
import com.voxcrafterlp.statsaddon.utils.compatibility.events.JoinEvent;
import net.labymod.main.LabyMod;
import net.labymod.utils.ServerData;

public class JoinEventHandler extends JoinEvent {

    public JoinEventHandler(ServerData serverData) {
        super(serverData);
    }

    @Override
    public Void call() throws Exception {
        StatsAddon statsAddon = StatsAddon.getInstance();
        if (serverData.getIp().equalsIgnoreCase("gommehd.net") ||
                serverData.getIp().equalsIgnoreCase("premium.gommehd.net") ||
                serverData.getIp().equalsIgnoreCase("mc.gommehd.net") ||
                serverData.getIp().equalsIgnoreCase("gommehd.com") ||
                serverData.getIp().equalsIgnoreCase("premium.gommehd.com") ||
                serverData.getIp().equalsIgnoreCase("mc.gommehd.com")||
                serverData.getIp().equalsIgnoreCase("citybuild.gommehd.net")) {

            statsAddon.setOnline(true);

            new Thread(() -> {
                try {
                    Thread.sleep(1500);
                    new VersionChecker();

                    if(!statsAddon.isWebsiteMessageShown()) {
                        final String url = "http://localhost:" + statsAddon.getWebserver().getWebserver().getAddress().getPort() + "/";
                        if(statsAddon.isWebserverEnabled()) {
                            LabyMod.getInstance().displayMessageInChat(statsAddon.getPrefix() + "\u00A77Die Webseite wurde \u00A7bgeöffnet\u00A78. " +
                                    "\u00A77Alternativ ist diese aber auch über diesen \u00A7bLink \u00A77zu erreichen\u00A78: " +
                                    "[\u00A7b" + url + "\u00A78]");
                            LabyMod.getInstance().openWebpage(url, false);
                            statsAddon.setWebsiteMessageShown(true);
                        }
                    }
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
            }).start();
        } else
            statsAddon.setOnline(false);
        return null;
    }
}
