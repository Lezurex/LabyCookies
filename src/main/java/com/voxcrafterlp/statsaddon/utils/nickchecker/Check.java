package com.voxcrafterlp.statsaddon.utils.nickchecker;

import net.minecraft.client.network.NetworkPlayerInfo;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 11.03.2021
 * Time: 20:20
 * Project: LabyCookies
 */

public interface Check {

    public void performCheck(NetworkPlayerInfo playerInfo);

    public boolean isCheckSuccessful();

    public boolean ignore();

    public int getWeight();

}
