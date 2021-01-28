package com.voxcrafterlp.statsaddon.nickedchecker;

import net.minecraft.client.network.NetworkPlayerInfo;

public interface NickAlgorithm {

    public boolean performCheck(NetworkPlayerInfo playerInfo, String joinMessage);

}
