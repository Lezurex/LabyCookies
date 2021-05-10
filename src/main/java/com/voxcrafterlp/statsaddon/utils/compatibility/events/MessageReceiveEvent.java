package com.voxcrafterlp.statsaddon.utils.compatibility.events;

import java.util.concurrent.Callable;

public abstract class MessageReceiveEvent implements Callable<Boolean> {

    public String formatted;
    public String unFormatted;

    public MessageReceiveEvent(String formatted, String unFormatted) {
        this.formatted = formatted;
        this.unFormatted = unFormatted;
    }
}
