package me.RareHyperIon.BlockTrials.utility;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public final class Exempt {

    public static boolean gameMode(final Player player) {
        final GameMode mode = player.getGameMode();
        return mode == GameMode.CREATIVE || mode == GameMode.SPECTATOR || defaults(player);
    }

    private static boolean defaults(final Player player) {
        return player.isDead();
    }

}
