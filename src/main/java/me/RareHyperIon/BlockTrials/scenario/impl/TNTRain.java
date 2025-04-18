package me.RareHyperIon.BlockTrials.scenario.impl;

import me.RareHyperIon.BlockTrials.BlockTrials;
import me.RareHyperIon.BlockTrials.scenario.Scenario;
import me.RareHyperIon.BlockTrials.utility.MathUtility;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class TNTRain extends Scenario {

    public TNTRain(final BlockTrials plugin) {
        super(plugin, "tnt_rain", Material.TNT);
    }

    @Override
    protected void onStart() {
        this.repeat(() -> {
            for(final Player player : Bukkit.getOnlinePlayers()) {
                final Location location = player.getLocation().clone();

                if(player.getWorld() != this.world) continue;

                for(int x = location.getBlockX() - 20; x < location.getBlockX() + 20; x += MathUtility.nextInt(1, 8)) {
                    for(int z = location.getBlockZ() - 20; z < location.getBlockZ() + 20; z += MathUtility.nextInt(1, 8)) {
                        final int y = 100;
                        this.world.spawnEntity(this.world.getBlockAt(x, y, z).getLocation(), EntityType.PRIMED_TNT);
                    }
                }
            }
        }, 20L * 60);
    }

    @Override
    protected void onEnd() {}

}
