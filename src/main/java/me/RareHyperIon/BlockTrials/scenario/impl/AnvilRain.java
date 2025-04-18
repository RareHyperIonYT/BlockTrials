package me.RareHyperIon.BlockTrials.scenario.impl;

import me.RareHyperIon.BlockTrials.BlockTrials;
import me.RareHyperIon.BlockTrials.scenario.Scenario;
import me.RareHyperIon.BlockTrials.utility.MathUtility;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class AnvilRain extends Scenario {

    private final int heightFromGround = 50;

    public AnvilRain(final BlockTrials plugin) {
        super(plugin, "anvil_rain", Material.ANVIL);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void onStart() {
        this.repeat(() -> {
            for(final Player player : Bukkit.getOnlinePlayers()) {
                final Location location = player.getLocation().clone();

                if(player.getWorld() != this.world) continue;

                for(int x = location.getBlockX() - 20; x < location.getBlockX() + 20; x += MathUtility.nextInt(1, 8)) {
                    for(int z = location.getBlockZ() - 20; z < location.getBlockZ() + 20; z += MathUtility.nextInt(1, 8)) {
                        final int highestPoint = this.world.getHighestBlockYAt(x, z);
                        final int y = highestPoint + this.heightFromGround;

                        final FallingBlock fallingBlock = this.world.spawnFallingBlock(this.world.getBlockAt(x, y, z).getLocation(), Material.ANVIL, (byte) 0);
                        fallingBlock.setMetadata("anvil_rain", new FixedMetadataValue(this.plugin, true));
                        fallingBlock.setDropItem(false);
                    }
                }
            }
        }, 20L * 60);
    }

    @Override
    protected void onEnd() {}

    @EventHandler
    public void onBlockFall(final EntityChangeBlockEvent event) {
        if(event.getEntityType() != EntityType.FALLING_BLOCK) return;

        final FallingBlock block = (FallingBlock) event.getEntity();

        if(block.getMaterial() == Material.ANVIL && block.hasMetadata("anvil_rain")) {
            event.setCancelled(true);
            block.remove();

            for(final Entity entity : block.getNearbyEntities(1.1, 1.1, 1.1)) {
                if(!(entity instanceof LivingEntity)) continue;
                ((LivingEntity) entity).damage(MathUtility.nextInt(6, 12));
            }
        }
    }

}
