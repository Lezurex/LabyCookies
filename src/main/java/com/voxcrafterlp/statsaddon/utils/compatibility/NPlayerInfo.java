package com.voxcrafterlp.statsaddon.utils.compatibility;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;

public class NPlayerInfo {

    private final NetworkPlayerInfo playerInfo;

    public NPlayerInfo(NetworkPlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
    }

    public GameProfile getGameProfile() {
        return this.playerInfo.getGameProfile();
    }

    public ScorePlayerTeam getPlayerTeam() {
        return this.playerInfo.getPlayerTeam();
    }

    public String getSkinPath() {
        return this.playerInfo.getLocationSkin().getPath();
    }
}
