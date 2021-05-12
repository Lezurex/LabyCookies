package com.voxcrafterlp.statsaddon.utils.compatibility.events;

import net.labymod.utils.ServerData;

import java.util.concurrent.Callable;

public abstract class JoinEvent implements Callable<Void> {

    public ServerData serverData;

    public JoinEvent(ServerData serverData) {
        this.serverData = serverData;
    }
}
