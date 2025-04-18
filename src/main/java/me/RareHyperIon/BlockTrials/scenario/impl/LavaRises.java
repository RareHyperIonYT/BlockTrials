package me.RareHyperIon.BlockTrials.scenario.impl;

import com.cryptomorin.xseries.XMaterial;
import com.fastasyncworldedit.core.util.TaskManager;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockTypes;
import me.RareHyperIon.BlockTrials.BlockTrials;
import me.RareHyperIon.BlockTrials.scenario.Scenario;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.plugin.PluginManager;

import java.util.AbstractMap;
import java.util.Map;

public class LavaRises extends Scenario {

    private int lavaHeight = 0;
    private int fillSpeed = 16 * 8;

    public LavaRises(final BlockTrials plugin) {
        super(plugin, "lava_rises", XMaterial.LAVA_BUCKET.parseMaterial());
    }

    @Override
    protected void onStart() {
        final WorldBorder worldBorder = this.world.getWorldBorder();
        final double size = worldBorder.getSize();

        final Location min = new Location(this.world, -size / 2, 0, -size / 2);
        final Location max = new Location(this.world, size / 2, 0, size / 2);

        if(this.lavaHeight == 0) {
            for(int i = 0; i > -300; i--) {
                final Block block = this.world.getBlockAt(0, i, 0);
                if(block != null && block.getType() == Material.BEDROCK) {
                    this.lavaHeight = i;
                    break;
                }
            }
        }

        this.repeat(() -> {
            if(this.lavaHeight >= this.world.getMaxHeight()) return;

            final World world = BukkitAdapter.adapt(this.world);

            TaskManager.taskManager().async(() -> {
                try(final EditSession session = WorldEdit.getInstance().newEditSession(world)) {
                    for(int x = min.getBlockX(); x < max.getBlockX(); x++) {
                        for(int z = min.getBlockZ(); z < max.getBlockZ(); z++) {
                            Location lavaLocation = new Location(this.world, x, this.lavaHeight, z);
                            if (!this.world.getBlockAt(lavaLocation).getType().isSolid()) {
                                session.setBlock(x, this.lavaHeight, z, BlockTypes.LAVA.getDefaultState());
                            }
                        }
                    }

                    session.flushQueue();
                }
            });

            this.lavaHeight++;
        }, 20L * 3);
    }

    @Override
    protected void onEnd() {}

    @Override
    public Object getData() {
        return this.lavaHeight;
    }

    @Override
    public void loadData(Object data) {
        if(data instanceof Integer) {
            this.lavaHeight = (int) data;
        }
    }

    @Override
    public Map.Entry<Boolean, String> shouldEnable() {
        final PluginManager manager = this.plugin.getServer().getPluginManager();

        if(manager.getPlugin("FastAsyncWorldEdit") == null || !manager.getPlugin("FastAsyncWorldEdit").isEnabled()) {
            return new AbstractMap.SimpleEntry<>(false, "&cThe plugin \"FastAsyncWorldEdit\" is required for this scenario.");
        }

        if(this.world.getWorldBorder().getSize() > 301) {
            return new AbstractMap.SimpleEntry<>(false, "&cThe world border cannot be larger than 300.");
        }

        return super.shouldEnable();
    }
}
