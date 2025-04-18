package me.RareHyperIon.BlockTrials.scenario.impl;

import me.RareHyperIon.BlockTrials.BlockTrials;
import me.RareHyperIon.BlockTrials.scenario.Scenario;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.UUID;

public class TNTTrail extends Scenario {

    private final HashMap<UUID, Location> locationMap = new HashMap<>();

    public TNTTrail(final BlockTrials plugin) {
        super(plugin, "tnt_trail", Material.TNT);
    }

    @Override
    protected void onStart() {

    }

    @Override
    protected void onEnd() {

    }

    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        if(player.getGameMode() == GameMode.SPECTATOR) return;

        final Location lastLocation = this.locationMap.get(player.getUniqueId());

        if(lastLocation == null) {
            this.locationMap.put(player.getUniqueId(), player.getLocation());
            return;
        }

        final Location currentLocation = player.getLocation();

        if(this.shouldCreateTrail(lastLocation, currentLocation)) {
            player.getWorld().spawnEntity(lastLocation, EntityType.PRIMED_TNT);
            this.locationMap.put(player.getUniqueId(), currentLocation);
        }
    }

    private boolean shouldCreateTrail(final Location lastLocation, final Location currentLocation) {
        final Location lastLocation2D = lastLocation.clone();
        lastLocation2D.setY(0);

        final Location currentLocation2D = currentLocation.clone();
        currentLocation2D.setY(0);

        return currentLocation2D.distance(lastLocation2D) >= 8;
    }

}
