package com.voxcrafterlp.statsaddon.utils.compatibility.events;

import com.google.gson.JsonElement;

import java.util.concurrent.Callable;

public abstract class ServerMessageEvent implements Callable<Void> {

    public String s;
    public JsonElement jsonElement;

    public ServerMessageEvent(String s, JsonElement jsonElement) {
        this.s = s;
        this.jsonElement = jsonElement;
    }
}
