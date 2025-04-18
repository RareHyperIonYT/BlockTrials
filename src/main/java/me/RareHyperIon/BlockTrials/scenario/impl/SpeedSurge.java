package me.RareHyperIon.BlockTrials.scenario.impl;

import me.RareHyperIon.BlockTrials.BlockTrials;
import me.RareHyperIon.BlockTrials.scenario.Scenario;
import me.RareHyperIon.BlockTrials.utility.StringUtility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.UUID;

public class SpeedSurge extends Scenario {

    private final HashMap<UUID, Integer> levelMap = new HashMap<>();

    public SpeedSurge(final BlockTrials plugin) {
        super(plugin, "speed_surge", Material.LEATHER_BOOTS);
    }

    @Override
    protected void onStart() {
        this.repeat(() -> {
            for(final Player player : Bukkit.getOnlinePlayers()) {
                final UUID playerId = player.getUniqueId();
                final int newLevel = this.levelMap.getOrDefault(playerId, -1) + 1;
                this.levelMap.put(playerId, newLevel);

                this.applySpeed(player, newLevel);
            }
        }, 20L * 60);
    }

    @Override
    protected void onEnd() {
        this.levelMap.clear();

        for(final Player player : Bukkit.getOnlinePlayers()) {
            player.removePotionEffect(PotionEffectType.SPEED);
        }
    }

    public void applySpeed(final Player player, final int level) {
        player.removePotionEffect(PotionEffectType.SPEED);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60, level, true, false));
    }
}
