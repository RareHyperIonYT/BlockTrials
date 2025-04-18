package me.RareHyperIon.BlockTrials.scenario.impl;

import me.RareHyperIon.BlockTrials.BlockTrials;
import me.RareHyperIon.BlockTrials.scenario.Scenario;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.Collection;

public class LinkedHunger extends Scenario {

    public LinkedHunger(final BlockTrials plugin) {
        super(plugin, "linked_hunger", Material.COOKED_BEEF);
    }

    @Override
    protected void onStart() {
        final Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        final int highestFoodLevel = players.stream().mapToInt(Player::getFoodLevel).max().orElse(20);
        final float highestSaturation = (float)players.stream().mapToDouble(Player::getSaturation).max().orElse(20);

        for(final Player player : Bukkit.getOnlinePlayers()) {
            player.setFoodLevel(highestFoodLevel);
            player.setSaturation(highestSaturation);
        }
    }

    @Override
    protected void onEnd() {}

    @EventHandler
    public void onFoodChange(final FoodLevelChangeEvent event) {
        if(!(event.getEntity() instanceof Player player)) return;

        this.delay(() -> {
            final float saturation = player.getSaturation();
            final int foodLevel = player.getFoodLevel();

            for(final Player online : Bukkit.getOnlinePlayers()) {
                online.setSaturation(saturation);
                online.setFoodLevel(foodLevel);
            }
        }, 1L);
    }
}
