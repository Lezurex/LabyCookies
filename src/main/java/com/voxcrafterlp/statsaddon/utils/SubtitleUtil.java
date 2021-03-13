package com.voxcrafterlp.statsaddon.utils;

import net.labymod.main.LabyMod;
import net.labymod.utils.ModColor;

import java.util.UUID;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 01.01.2021
 * Time: 00:51
 * Project: LabyCookies
 */

public class SubtitleUtil {

    /**
     * @deprecated Will probably be replaced
     *
     * Shows a subtitle below the name of a player
     *
     * @param uuid {@link UUID} object of the player
     * @param title Text of the subtitle
     */
    public void showSubtitle(UUID uuid, String title) {
        LabyMod.getInstance().getUserManager().getUsers().forEach((uuids, user) -> {
            if(uuids.equals(uuid)) {
                double subTitleSize = 1.4D;

                user.setSubTitle(ModColor.createColors(title));
                user.setSubTitleSize(subTitleSize);
            }
        });
    }
}
