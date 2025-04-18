package me.RareHyperIon.BlockTrials.inventories;

import de.tr7zw.changeme.nbtapi.NBT;
import me.RareHyperIon.BlockTrials.BlockTrials;
import me.RareHyperIon.BlockTrials.handler.LanguageHandler;
import me.RareHyperIon.BlockTrials.scenario.Scenario;
import me.RareHyperIon.BlockTrials.utility.StringUtility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

public class SelectionScreen implements InventoryHolder {

    private final LanguageHandler language;
    private final BlockTrials plugin;
    private final Inventory inventory;

    public SelectionScreen(final BlockTrials plugin) {
        this.inventory = Bukkit.createInventory(this, 18, "Scenarios");
        this.language = plugin.getLanguage();
        this.plugin = plugin;
        this.initialise();
    }

    private void initialise() {
        final List<Scenario> scenarios = this.plugin.getScenarioHandler().list();

        for(int i = 0; i < scenarios.size(); i++) {
            final Scenario scenario = scenarios.get(i);
            final ItemStack item = this.createScenarioItem(scenario);

            this.inventory.setItem(i, item);
        }
    }

    private ItemStack createScenarioItem(final Scenario scenario) {
        final ItemStack item = this.createItem(
                "§b" + this.language.get(String.format("scenarios.%s.name", scenario.getId())),
                scenario.getIcon(),
                List.of("§f" + this.language.get(String.format("scenarios.%s.description", scenario.getId())))
        );

        NBT.modify(item, nbt -> { nbt.setString("scenario_id", scenario.getId()); });

        this.applyMeta(item, scenario.isEnabled());

        return item;
    }

    private void applyMeta(final ItemStack item, boolean enabled) {
        final ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            if (enabled) meta.addEnchant(Enchantment.LUCK, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
    }

    private ItemStack createItem(final String name, final Material material, final List<String> lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(StringUtility.applyColor(name));
        meta.setLore(lore.stream().map(StringUtility::applyColor).collect(Collectors.toList()));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

}
