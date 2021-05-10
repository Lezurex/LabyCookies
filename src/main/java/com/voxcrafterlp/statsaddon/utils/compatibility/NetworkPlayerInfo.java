package com.voxcrafterlp.statsaddon.utils.compatibility;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.play.server.S38PacketPlayerListItem;

public class NetworkPlayerInfo extends net.minecraft.client.network.NetworkPlayerInfo {
    public NetworkPlayerInfo(GameProfile p_i46294_1_) {
        super(p_i46294_1_);
    }

    public NetworkPlayerInfo(S38PacketPlayerListItem.AddPlayerData p_i46295_1_) {
        super(p_i46295_1_);
    }

    public String getSkinPath() {
        return super.getLocationSkin().getResourcePath();
    }
}
