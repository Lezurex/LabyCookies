package com.voxcrafterlp.statsaddon.utils.nickchecker;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.AtomicDouble;
import com.voxcrafterlp.statsaddon.objects.PlayerStats;
import lombok.Getter;
import net.labymod.main.LabyMod;
import net.minecraft.client.network.NetworkPlayerInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 11.03.2021
 * Time: 18:30
 * Project: LabyCookies
 */

@Getter
public class NickChecker {

    /**
     * Weights:
     *
     *  - BadlionCheck: 15%
     *  - ClanCheck: 20%
     *  - DefaultSkinCheck: 20%
     *  - PremiumCheck: 25%
     *  - StatsCheck: 20%
     */

    private final PlayerStats playerStats;
    private final NetworkPlayerInfo playerInfo;
    private final List<Check> checks = Lists.newCopyOnWriteArrayList();

    public NickChecker(PlayerStats playerStats) {
        this.playerStats = playerStats;
        this.playerInfo = this.playerStats.getPlayerInfo();
        checks.addAll(Arrays.asList(new BadlionCheck(), new ClanCheck(), new DefaultSkinCheck(), new PremiumCheck(), new StatsCheck(this.playerStats)));
    }

    /**
     * Returns a nick probability in percent based on different checks
     *
     * @return {@link Double} probability (0 - 100)
     */
    public double checkPlayer() {
        final String prefix = this.playerInfo.getPlayerTeam().getColorPrefix();
        if(prefix.equals("§a") || prefix.equals("§bSupreme §7| ") || prefix.contains("Dev") || prefix.contains("Mod") ||
                prefix.contains("Content") || prefix.contains("Sup") || prefix.contains("Admin")) return 0;

        this.checks.forEach(check -> check.performCheck(this.playerInfo));

        final List<Check> passedChecks = this.checks.stream()
                .filter(check -> check.isCheckSuccessful() && !check.ignore())
                .collect(Collectors.toList());

        AtomicDouble result = new AtomicDouble(0);
        passedChecks.forEach(check -> result.addAndGet(check.getWeight()));

        return result.get();
    }

}
