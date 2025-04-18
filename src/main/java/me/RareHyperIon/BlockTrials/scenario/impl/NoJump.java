package me.RareHyperIon.BlockTrials.scenario.impl;

import me.RareHyperIon.BlockTrials.BlockTrials;
import me.RareHyperIon.BlockTrials.scenario.Scenario;
import me.RareHyperIon.BlockTrials.utility.Exempt;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NoJump extends Scenario {

    private final Map<UUID, Long> lastExempt = new HashMap<>();

    public NoJump(final BlockTrials plugin) {
        super(plugin, "no_jump", Material.RABBIT_FOOT);
    }

    @Override
    protected void onStart() {
        this.repeat(() -> {
            for(final Player player : Bukkit.getOnlinePlayers()) {
                if(Exempt.gameMode(player)) continue;

                final Location location = player.getLocation();
                final World world = location.getWorld();
                final double x = location.getX();
                final double y = location.getY();
                final double z = location.getZ();

                final double expand = 0.3D;

                check: {
                    for(double dx = -expand; dx < expand; dx += 0.1) {
                        for(double dz = -expand; dz < expand; dz += 0.1) {
                            for(double dy = -0.5; dy < 0.5; dy += 0.5) {
                                final Location newLocation = new Location(world, x + dx, y + dy, z + dz);

                                final Material type = newLocation.getBlock().getType();

                                if(type == Material.SLIME_BLOCK || type == Material.WATER || type == Material.LAVA) {
                                    this.lastExempt.put(player.getUniqueId(), System.currentTimeMillis());
                                    break check;
                                }
                            }
                        }
                    }
                }
            }
        }, 3L); // Runs every 150ms.
    }

    @Override
    protected void onEnd() {}

    @EventHandler
    public void onEntityDamage(final EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player player)) return;
        this.lastExempt.put(player.getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler
    public void onStatisticIncrement(final PlayerStatisticIncrementEvent event) {
        final Statistic statistic = event.getStatistic();

        if(statistic == Statistic.JUMP) {
            final Player player = event.getPlayer();

            if(Exempt.gameMode(player) || (System.currentTimeMillis() - this.lastExempt.getOrDefault(player.getUniqueId(), -1L)) < 500) return;

            player.damage(player.getMaxHealth());
            event.setCancelled(true);
        }
    }

}
