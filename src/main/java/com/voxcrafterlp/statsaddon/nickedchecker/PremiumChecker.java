
package com.voxcrafterlp.statsaddon.nickedchecker;

import net.minecraft.client.network.NetworkPlayerInfo;

public class PremiumChecker implements NickAlgorithm {

    @Override
    public boolean performCheck(NetworkPlayerInfo playerInfo, String joinMessage) {

        String prefix = playerInfo.getPlayerTeam().getColorPrefix();
        System.out.println(prefix);
        if (prefix.contains("§6")) {
            return true;
        }

        return false;
    }
}