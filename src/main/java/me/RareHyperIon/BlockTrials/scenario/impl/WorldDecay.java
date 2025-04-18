package me.RareHyperIon.BlockTrials.scenario.impl;

import me.RareHyperIon.BlockTrials.BlockTrials;
import me.RareHyperIon.BlockTrials.scenario.Scenario;
import me.RareHyperIon.BlockTrials.utility.MathUtility;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorldDecay extends Scenario {

    public WorldDecay(final BlockTrials plugin) {
        super(plugin, "world_decay", Material.COBBLESTONE);
    }

    @Override
    protected void onStart() {
        this.repeat(() -> {
            int total = 0;

            final List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
            Collections.shuffle(onlinePlayers);

            for(final Player player : onlinePlayers) {
                for(int i = 0; i < 10 && total < 50; i++) {
                    total++;

                    final Location location = player.getLocation().clone().add(MathUtility.nextInt(-16, 16), 0, MathUtility.nextInt(-16, 16));
                    Block block = player.getWorld().getHighestBlockAt(location);

                    if(block.getType() == Material.BEDROCK) continue;

                    block.setType(Material.AIR);
                }
            }
        }, 20L);
    }

    @Override
    protected void onEnd() {}

}
