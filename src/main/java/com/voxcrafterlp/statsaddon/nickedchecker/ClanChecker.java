package com.voxcrafterlp.statsaddon.nickedchecker;

import net.minecraft.client.network.NetworkPlayerInfo;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClanChecker implements NickAlgorithm {

    @Override
    public boolean performCheck(NetworkPlayerInfo playerInfo, String joinMessage) {

        String colorSuffix = playerInfo.getPlayerTeam().getColorSuffix();
        Pattern pattern = Pattern.compile("\\[.+\\]");
        Matcher matcher = pattern.matcher(colorSuffix);
        System.out.println(playerInfo.getGameProfile().getName() + ": " + matcher.find());
        if (matcher.find(0)) {
            System.out.println("If is true");
            return false;
        } else {
            return true;
        }

    }
}