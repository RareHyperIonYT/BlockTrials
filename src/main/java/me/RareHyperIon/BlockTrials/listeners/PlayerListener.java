package me.RareHyperIon.BlockTrials.listeners;

import com.cryptomorin.xseries.XSound;
import de.tr7zw.changeme.nbtapi.NBT;
import me.RareHyperIon.BlockTrials.BlockTrials;
import me.RareHyperIon.BlockTrials.handler.ScenarioHandler;
import me.RareHyperIon.BlockTrials.inventories.SelectionScreen;
import me.RareHyperIon.BlockTrials.scenario.Scenario;
import me.RareHyperIon.BlockTrials.scenario.impl.LinkedInventories;
import me.RareHyperIon.BlockTrials.utility.StringUtility;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;


public class PlayerListener implements Listener {

    private final BlockTrials plugin;

    public PlayerListener(final BlockTrials plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final LinkedInventories scenario = (LinkedInventories) this.plugin.getScenarioHandler().get("linked_inventories");
        if(scenario.isEnabled() || scenario.missedReturns.isEmpty()) return;

        final Player player = event.getPlayer();

        if(scenario.missedReturns.containsKey(player.getUniqueId())) {
            final Map.Entry<ItemStack[], ItemStack[]> contents = scenario.missedReturns.get(player.getUniqueId());

            if(contents == null) return;

            player.getInventory().setArmorContents(contents.getKey());
            player.getInventory().setContents(contents.getValue());

            scenario.missedReturns.remove(player.getUniqueId());
        }
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        final Inventory inventory = event.getClickedInventory();

        if(inventory == null || !(inventory.getHolder() instanceof SelectionScreen)) return;

        event.setCancelled(true);

        final Player player = (Player) event.getWhoClicked();
        final ItemStack clickedItem = event.getCurrentItem();

        if(clickedItem == null) {
            return;
        }

        final ScenarioHandler scenarioHandler = this.plugin.getScenarioHandler();

        NBT.get(clickedItem, nbt -> {
            final String scenarioId = nbt.getOrDefault("scenario_id", "unknown");
            final Scenario scenario = scenarioHandler.get(scenarioId);

            if(scenario == null) {
                return;
            }

            final Map.Entry<Boolean, String> shouldEnable = scenario.shouldEnable();

            if(!shouldEnable.getKey()) {
                player.sendTitle("", StringUtility.applyColor(shouldEnable.getValue()));
                player.closeInventory();
                player.playSound(player.getLocation(), XSound.BLOCK_ANVIL_PLACE.parseSound(), 1.2F, 0.65F);
                return;
            }

            if(scenario.isEnabled()) {
                scenario.end();
            } else {
                scenario.start();
            }

            final float soundPitch = scenario.isEnabled() ? 1.5F : 0.4F;
            player.playSound(player.getLocation(), XSound.BLOCK_NOTE_BLOCK_HARP.parseSound(), 1.4F, soundPitch);

            final ItemMeta itemMeta = clickedItem.getItemMeta();

            if (itemMeta != null) {
                if(scenario.isEnabled()) {
                    itemMeta.addEnchant(Enchantment.LUCK, 1, true);
                    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                } else {
                    itemMeta.removeEnchant(Enchantment.LUCK);
                }

                clickedItem.setItemMeta(itemMeta);
            }
        });
    }

}
