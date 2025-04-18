package me.RareHyperIon.BlockTrials.scenario.impl;

import me.RareHyperIon.BlockTrials.BlockTrials;
import me.RareHyperIon.BlockTrials.scenario.Scenario;
import me.RareHyperIon.BlockTrials.utility.Exempt;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Collection;

public class LinkedDamage extends Scenario {

    private long lastDamage = -1L;

    public LinkedDamage(final BlockTrials plugin) {
        super(plugin, "linked_damage", Material.APPLE);
    }

    @Override
    protected void onStart() {
        final Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        final double highestHealth = players.stream().mapToDouble(Player::getHealth).max().orElse(20);

        for(final Player player : Bukkit.getOnlinePlayers()) {
            player.setHealth(highestHealth);
        }
    }

    @Override
    protected void onEnd() {

    }

    @EventHandler
    public void onEntityDamage(final EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

        final long currentTime = System.currentTimeMillis();

        if(currentTime - this.lastDamage < 250L) return;
        this.lastDamage = currentTime;

        final double damageTaken = event.getFinalDamage();

        for(final Player player : Bukkit.getOnlinePlayers()) {
            if(player == event.getEntity()) continue;
            if(Exempt.gameMode(player)) continue;
            player.damage(damageTaken);
        }
    }

}
