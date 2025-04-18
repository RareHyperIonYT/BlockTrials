package me.RareHyperIon.BlockTrials.scenario;

import me.RareHyperIon.BlockTrials.BlockTrials;
import me.RareHyperIon.BlockTrials.handler.LanguageHandler;
import me.RareHyperIon.BlockTrials.utility.StringUtility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Scenario implements Listener {

    private static final Map.Entry<Boolean, String> DEFAULT_ENABLE_RESULT = new AbstractMap.SimpleEntry<>(true, null);

    private final LanguageHandler language;
    protected final BlockTrials plugin;

    private final String id;
    private final Material icon;

    public final World world;

    private boolean enabled = false;

    private List<Integer> tasks = new ArrayList<>();

    public Scenario(final BlockTrials plugin, final String id, final Material icon) {
        this.language = plugin.getLanguage();
        this.plugin = plugin;
        this.world = Bukkit.getWorlds().getFirst();

        this.id = id;
        this.icon = icon;
    }

    public void start() {
        this.enabled = true;

        Bukkit.broadcastMessage(
                StringUtility.applyColor(this.language.get("scenario-start")
                        .replaceAll("\\{name}", this.language.get("scenarios." + this.id + ".name")))
        );;

        this.onStart();

        Bukkit.getPluginManager().registerEvents(this, this.plugin);
    }

    public void end() {
        this.enabled = false;

        Bukkit.broadcastMessage(
            StringUtility.applyColor(this.language.get("scenario-end")
                        .replaceAll("\\{name}", this.language.get("scenarios." + this.id + ".name")))
        );

        this.onEnd();

        this.tasks.forEach(Bukkit.getScheduler()::cancelTask);
        this.tasks.clear();

        HandlerList.unregisterAll(this);
    }

    protected abstract void onStart();
    protected abstract void onEnd();

    public void repeat(final Runnable runnable, final long delay) {
        final int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, runnable, delay, delay);
        this.tasks.add(id);
    }

    public void delay(final Runnable runnable, final long delay) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, runnable, delay);
    }

    public String getId() {
        return this.id;
    }

    public Material getIcon() {
        return this.icon;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public Object getData() {
        return null;
    }

    public void loadData(final Object data) {}

    public Map.Entry<Boolean, String> shouldEnable() {
        return DEFAULT_ENABLE_RESULT;
    }

}
