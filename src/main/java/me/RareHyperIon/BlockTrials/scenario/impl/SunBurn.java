package me.RareHyperIon.BlockTrials.scenario.impl;

import me.RareHyperIon.BlockTrials.BlockTrials;
import me.RareHyperIon.BlockTrials.scenario.Scenario;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class SunBurn extends Scenario {

    public SunBurn(final BlockTrials plugin) {
        super(plugin, "sun_burn", Material.LAVA_BUCKET);
    }

    @Override
    protected void onStart() {
        this.repeat(() -> {
            for(final Player player : Bukkit.getOnlinePlayers()) {
                long worldTime = player.getWorld().getTime() % 24_000;
                if(!(worldTime > 0 && worldTime < 12_800)) continue;
                if(player.getGameMode() != GameMode.SURVIVAL && player.getGameMode() != GameMode.ADVENTURE) continue;
                if(player.isDead()) continue;

                if(this.isUnderLight(player)) {
                    player.setFireTicks(30);
                }
            }
        }, 10L);
    }

    @Override
    protected void onEnd() {}

    private boolean isUnderLight(final Player player) {
        final Location location = player.getLocation();

        Block currentBlock = location.getBlock();

        while(currentBlock.getY() < this.world.getMaxHeight()) {
            if(currentBlock.getType() != Material.AIR) return false;
            currentBlock = currentBlock.getRelative(BlockFace.UP);
        }

        return true;
    }

}
