package me.RareHyperIon.BlockTrials.scenario.impl;

import me.RareHyperIon.BlockTrials.BlockTrials;
import me.RareHyperIon.BlockTrials.inventories.SelectionScreen;
import me.RareHyperIon.BlockTrials.scenario.Scenario;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;

public class LinkedInventories extends Scenario {

    /*
        TODO:
         - Save shared inventory along with original inventories.
         - Handle players that weren't in the game when giving original inventories back. [DONE?]

        Won't be implemented any time soon, sorry!
     */

    private final Map<UUID, Map.Entry<ItemStack[], ItemStack[]>> savedInventories = new HashMap<>();
    public final Map<UUID, Map.Entry<ItemStack[], ItemStack[]>> missedReturns = new HashMap<>();


    public LinkedInventories(final BlockTrials plugin) {
        super(plugin, "linked_inventories", Material.HOPPER);
    }

    @Override
    protected void onStart() {
        for(final Player player : Bukkit.getOnlinePlayers()) {
            final PlayerInventory inventory = player.getInventory();
            this.savedInventories.put(player.getUniqueId(), new AbstractMap.SimpleEntry<>(inventory.getArmorContents().clone(), inventory.getContents().clone()));
            inventory.clear();
        }
    }

    @Override
    protected void onEnd() {
        for(final Player player : Bukkit.getOnlinePlayers()) {
            final Map.Entry<ItemStack[], ItemStack[]> contents = this.savedInventories.get(player.getUniqueId());
            if(contents == null) continue;

            player.getInventory().setArmorContents(contents.getKey());
            player.getInventory().setContents(contents.getValue());

            this.savedInventories.remove(player.getUniqueId());
        }

        for(final OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            if(!this.savedInventories.containsKey(player.getUniqueId()) || this.missedReturns.containsKey(player.getUniqueId())) continue;
            this.missedReturns.put(player.getUniqueId(), this.savedInventories.get(player.getUniqueId()));
        }
    }

    @Override
    public Object getData() {
        final Map<UUID, Map.Entry<List<Map<String, Object>>, List<Map<String, Object>>>> serializedData = new HashMap<>();

        for (Map.Entry<UUID, Map.Entry<ItemStack[], ItemStack[]>> entry : this.savedInventories.entrySet()) {
            final UUID uuid = entry.getKey();
            final ItemStack[] armorContents = entry.getValue().getKey();
            final ItemStack[] invContents = entry.getValue().getValue();

            final List<Map<String, Object>> serializedArmor = serializeItemStackArray(armorContents);
            final List<Map<String, Object>> serializedContents = serializeItemStackArray(invContents);

            serializedData.put(uuid, new AbstractMap.SimpleEntry<>(serializedArmor, serializedContents));
        }

        return serializedData;
    }

    private List<Map<String, Object>> serializeItemStackArray(ItemStack[] items) {
        List<Map<String, Object>> serializedItems = new ArrayList<>();
        for (ItemStack item : items) {
            serializedItems.add(item == null ? null : item.serialize());
        }
        return serializedItems;
    }

    private ItemStack[] deserializeItemStackArray(List<Map<String, Object>> serializedItems) {
        ItemStack[] items = new ItemStack[serializedItems.size()];
        for (int i = 0; i < serializedItems.size(); i++) {
            Map<String, Object> serializedItem = serializedItems.get(i);
            items[i] = serializedItem == null ? null : ItemStack.deserialize(serializedItem);
        }
        return items;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void loadData(final Object data) {
        if (data == null) return;

        if (!(data instanceof Map<?, ?>)) {
            throw new IllegalArgumentException("Error loading save data for LinkedInventories.");
        }

        final Map<UUID, Map.Entry<List<Map<String, Object>>, List<Map<String, Object>>>> serializedData =
                (Map<UUID, Map.Entry<List<Map<String, Object>>, List<Map<String, Object>>>>) data;

        for (Map.Entry<UUID, Map.Entry<List<Map<String, Object>>, List<Map<String, Object>>>> entry : serializedData.entrySet()) {
            final UUID uuid = entry.getKey();

            final List<Map<String, Object>> armorContents = entry.getValue().getKey();
            final List<Map<String, Object>> invContents = entry.getValue().getValue();

            final ItemStack[] deserializedArmor = deserializeItemStackArray(armorContents);
            final ItemStack[] deserializedContents = deserializeItemStackArray(invContents);

            this.savedInventories.put(uuid, new AbstractMap.SimpleEntry<>(deserializedArmor, deserializedContents));
        }
    }

    private void updateInventory(final Player player) {
        this.delay(() -> {
            final PlayerInventory inventory = player.getInventory();

            for(final Player online : Bukkit.getOnlinePlayers()) {
                if(player == online) continue;

                online.getInventory().setArmorContents(inventory.getArmorContents());
                online.getInventory().setContents(inventory.getContents());
            }
        }, 1L);
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player eventPlayer = event.getPlayer();

        if(!this.savedInventories.containsKey(eventPlayer.getUniqueId())) {
            final PlayerInventory inventory = eventPlayer.getInventory();
            this.savedInventories.put(eventPlayer.getUniqueId(), new AbstractMap.SimpleEntry<>(inventory.getArmorContents().clone(), inventory.getContents().clone()));
            this.updateInventory(eventPlayer);
        }

        for(final Player player : Bukkit.getOnlinePlayers()) {
            if(player == event.getPlayer()) continue;
            this.updateInventory(player);
            break;
        }
    }


    @EventHandler
    public void onItemDrop(final PlayerDropItemEvent event) {
        this.updateInventory(event.getPlayer());
    }

    @EventHandler
    public void onItemPickup(final PlayerPickupItemEvent event) {
        this.updateInventory(event.getPlayer());
    }

    @EventHandler
    public void onItemConsume(final PlayerItemConsumeEvent event) {
        this.updateInventory(event.getPlayer());
    }

    @EventHandler
    public void onItemDrop(final BlockPlaceEvent event) {
        this.updateInventory(event.getPlayer());
    }

    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent event) {
        if(event.getInventory() != null && event.getInventory().getHolder() instanceof SelectionScreen) return;
        this.updateInventory((Player) event.getWhoClicked());
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if(event.getClickedInventory() != null && event.getClickedInventory().getHolder() instanceof SelectionScreen) return;
        this.updateInventory((Player) event.getWhoClicked());
    }

    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent event) {
        this.updateInventory(event.getEntity());
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        this.updateInventory(event.getPlayer());
    }



}
