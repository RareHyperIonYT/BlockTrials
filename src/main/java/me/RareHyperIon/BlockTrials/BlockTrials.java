package me.RareHyperIon.BlockTrials;

import com.fastasyncworldedit.core.Fawe;
import de.tr7zw.changeme.nbtapi.utils.MinecraftVersion;
import me.RareHyperIon.BlockTrials.commands.BlockTrialsCommand;
import me.RareHyperIon.BlockTrials.handler.LanguageHandler;
import me.RareHyperIon.BlockTrials.handler.ScenarioHandler;
import me.RareHyperIon.BlockTrials.listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class BlockTrials extends JavaPlugin {

    private final LanguageHandler languageHandler;
    private final ScenarioHandler scenarioHandler;

    public BlockTrials() {
        this.saveDefaultConfig();

        this.languageHandler = new LanguageHandler(this);
        this.scenarioHandler = new ScenarioHandler(this);

        MinecraftVersion.disableBStats(); // Ew, tracking, let's get rid of it.

    }

    @Override
    public void onEnable() {
        final BlockTrialsCommand command = new BlockTrialsCommand(this, this.languageHandler);
        this.getCommand("blocktrials").setExecutor(command);
        this.getCommand("blocktrials").setTabCompleter(command);

        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        this.languageHandler.load();
        this.scenarioHandler.load();
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Cancelling tasks...");
        Bukkit.getScheduler().cancelTasks(this);

        this.getLogger().info("Unregistering events.");
        HandlerList.unregisterAll(this);

        this.scenarioHandler.saveData();
    }

    public LanguageHandler getLanguage() {
        return this.languageHandler;
    }

    public ScenarioHandler getScenarioHandler() {
        return this.scenarioHandler;
    }

    public void reload() {
        this.getLogger().info("Reloading...");
        this.reloadConfig();
        this.languageHandler.load();
        this.scenarioHandler.loadLanguage();
    }

    public String logType() {
        return this.getConfig().getString("logging").toUpperCase();
    }

}
