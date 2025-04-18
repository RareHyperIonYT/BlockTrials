package me.RareHyperIon.BlockTrials.scenario.impl;

import me.RareHyperIon.BlockTrials.BlockTrials;
import me.RareHyperIon.BlockTrials.scenario.Scenario;
import me.RareHyperIon.BlockTrials.utility.MathUtility;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;

public class Glitch extends Scenario {

    private final PotionEffectType[] potionTypes = { PotionEffectType.BLINDNESS, PotionEffectType.LEVITATION, PotionEffectType.CONFUSION };
    private final Random random = new Random();


    public Glitch(final BlockTrials plugin) {
        super(plugin, "glitch", Material.END_PORTAL_FRAME);
    }

    @Override
    protected void onStart() {
        this.repeat(() -> {
            for(final Player player : Bukkit.getOnlinePlayers()) {
                if(this.random.nextBoolean()) continue;

                final int option = this.random.nextInt(8);

                switch (option) {
                    case 0: {
                        player.teleport(player.getLocation().clone().add(0, -0.1D, 0));
                        break;
                    }

                    case 1: {
                        if(this.random.nextBoolean()) {
                            player.setVelocity(new Vector(0, 0, 0));
                        } else {
                            final Vector velocity = player.getVelocity();
                            player.setVelocity(new Vector(velocity.getX(), this.random.nextBoolean() ? 0.21 : -0.21, velocity.getZ()));
                        }
                        break;
                    }

                    case 2: {
                        final ItemStack[] contents = player.getInventory().getContents();
                        final List<ItemStack> items = new ArrayList<>(Arrays.asList(contents));
                        Collections.shuffle(items);
                        for(int i = 0; i < contents.length; i++) contents[i] = (i < items.size() ? items.get(i) : null);
                        player.getInventory().setContents(contents);
                        break;
                    }

                    case 3: {
                        player.setFireTicks(5);
                        break;
                    }

                    case 4: {
                        final PotionEffectType potionEffect = this.potionTypes[this.random.nextInt(this.potionTypes.length)];
                        player.addPotionEffect(new PotionEffect(potionEffect, 20 * MathUtility.nextInt(1, 5), MathUtility.nextInt(1, 3)));
                        break;
                    }

                    case 5: {
                        for(int i = 0; i < MathUtility.nextInt(4, 20); i++) {
                            final Location center = player.getLocation().clone().add(this.random.nextInt(5) - 2, this.random.nextInt(5) - 2, this.random.nextInt(5) - 2);
                            final World w = player.getWorld();
                            final int dx = this.random.nextInt(5) - 2;
                            final int dy = this.random.nextInt(5) - 2;
                            final int dz = this.random.nextInt(5) - 2;
                            final Block b1 = w.getBlockAt(center);
                            final Block b2 = w.getBlockAt(center.clone().add(dx, dy, dz));
                            final Material m1 = b1.getType();
                            final Material m2 = b2.getType();

                            b1.setType(m2);
                            b2.setType(m1);
                        }
                        break;
                    }

                    case 6: {
                        for(int i = 0; i < MathUtility.nextInt(4, 20); i++) {
                            final Location center = player.getLocation().clone().add(this.random.nextInt(10) - 4, this.random.nextInt(5) - 2, this.random.nextInt(10) - 4);
                            final World w = player.getWorld();
                            final int dx = this.random.nextInt(5) - 2;
                            final int dy = this.random.nextInt(5) - 2;
                            final int dz = this.random.nextInt(5) - 2;
                            final Block b2 = w.getBlockAt(center.clone().add(dx, dy, dz));

                            Material material = Material.values()[this.random.nextInt(Material.values().length)];
                            while(!material.isBlock()) material = Material.values()[this.random.nextInt(Material.values().length)];

                            b2.setType(material);
                        }
                        break;
                    }

                    case 7: {
                        final Location spawn = player.getLocation().clone().add(0, MathUtility.nextInt(5, 10), 0);

                        Material material = Material.values()[this.random.nextInt(Material.values().length)];
                        while(!material.isBlock()) material = Material.values()[this.random.nextInt(Material.values().length)];

                        final FallingBlock fallingBlock = player.getWorld().spawnFallingBlock(spawn, material.createBlockData());
                        fallingBlock.setDropItem(false);
                        break;
                    }
                }
            }
        }, 20 * 10);
    }

    @Override
    protected void onEnd() {}

    private double randomOffset() {
        return -2 + (4 * this.random.nextDouble());
    }

}
