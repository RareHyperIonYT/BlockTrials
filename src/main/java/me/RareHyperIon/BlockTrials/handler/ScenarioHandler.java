package me.RareHyperIon.BlockTrials.handler;

import me.RareHyperIon.BlockTrials.BlockTrials;
import me.RareHyperIon.BlockTrials.scenario.Scenario;
import me.RareHyperIon.BlockTrials.scenario.impl.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ScenarioHandler {

    private final Map<String, Scenario> scenarioMap = new HashMap<>();
    private final BlockTrials plugin;

    public ScenarioHandler(final BlockTrials plugin) {
        this.plugin = plugin;
    }

    public final void load() {
        this.loadScenarios();
        this.loadLanguage();
        this.loadData();
    }

    public final Scenario get(final String scenarioId) {
        return this.scenarioMap.get(scenarioId);
    }

    public final List<Scenario> list() {
        return this.scenarioMap.values().stream().toList();
    }

    private void loadScenarios() {
        this.plugin.getLogger().info("[ScenarioHandler] Registering scenarios...");

        this.register(new AnvilRain(this.plugin));
        this.register(new BedrockTrail(this.plugin));
        this.register(new SunBurn(this.plugin));
        this.register(new SpeedSurge(this.plugin));
        this.register(new LinkedDamage(this.plugin));
        this.register(new TNTRain(this.plugin));
        this.register(new LinkedHunger(this.plugin));
        this.register(new LinkedInventories(this.plugin));
        this.register(new NoJump(this.plugin));
        this.register(new TNTTrail(this.plugin));
        this.register(new WorldDecay(this.plugin));
        this.register(new LavaRises(this.plugin));
        this.register(new Glitch(this.plugin));
    }

    public final void loadLanguage() {
        final Logger logger = this.plugin.getLogger();
        final String logType = this.plugin.logType();

        logger.info("[ScenarioHandler] Loading scenario language config...");

        final File scenarioFile = new File(this.plugin.getDataFolder(), "scenarios.yml");

        if(!scenarioFile.exists()) {
            if(logType.equals("FULL")) logger.info("[ScenarioHandler] 'scenarios.yml' not found, creating default configuration...");
            this.saveDefault();
        }

        if(!scenarioFile.exists()) {
            throw new IllegalStateException("Scenario configuration doesn't exist, cannot proceed.");
        }

        final FileConfiguration scenarios = YamlConfiguration.loadConfiguration(scenarioFile);
        final LanguageHandler language = this.plugin.getLanguage();

        for(final String scenarioId : scenarios.getKeys(false)) {
            if(!this.scenarioMap.containsKey(scenarioId)) continue;

            final String scenarioName = scenarios.getString(scenarioId + ".name");
            final String scenarioDescription = scenarios.getString(scenarioId + ".description");

            language.addScenario(scenarioId, "name", scenarioName);
            language.addScenario(scenarioId, "description", scenarioDescription);
        }

        if(logType.equals("FULL")) logger.info("[ScenarioHandler] Scenario locale loaded successfully.");
    }

    private void register(final Scenario scenario) {
        this.scenarioMap.put(scenario.getId(), scenario);
    }

    private void saveDefault() {
        final File out = new File(this.plugin.getDataFolder(), "scenarios.yml");

        try(final InputStream stream = this.plugin.getResource("scenarios.yml")) {
            if(stream == null) throw new IllegalStateException("Resource \"scenarios.yml\" was not found within jar.");
            Files.copy(stream, out.toPath());
            if(this.plugin.logType().equals("FULL")) this.plugin.getLogger().info("[ScenarioHandler] Default 'scenarios.yml' configuration has been created successfully.");
        } catch (final IOException e) {
            throw new IllegalStateException("Failed to create default file: scenarios.yml", e);
        }
    }

    public void saveData() {
        final Logger logger = this.plugin.getLogger();
        final String logType = this.plugin.logType();

        logger.info("[ScenarioHandler] Saving scenario data...");

        final File dataFolder = new File(this.plugin.getDataFolder(), "saveData");
        if(!dataFolder.exists() && !dataFolder.mkdirs()) throw new IllegalStateException("Failed to create save data folder.");

        for(final Scenario scenario : this.list()) {
            final File file = new File(dataFolder, scenario.getId() + ".bin");

            try(final ObjectOutputStream stream = new ObjectOutputStream(Files.newOutputStream(file.toPath()))) {
                if(!file.exists() && !file.createNewFile()) logger.warning("Failed to save data for " + scenario.getId());

                final Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("enabled", scenario.isEnabled());
                dataMap.put("data", scenario.getData());

                stream.writeObject(dataMap);
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        }

        if(logType.equals("FULL")) logger.info("[ScenarioHandler] Successfully saved scenario data.");
    }

    private void loadData() {
        final Logger logger = this.plugin.getLogger();
        final String logType = this.plugin.logType();

        logger.info("[ScenarioHandler] Loading scenario save data..");

        final File dataFolder = new File(this.plugin.getDataFolder(), "saveData");
        if(!dataFolder.exists()) return;

        for(final Scenario scenario : this.list()) {
            final File file = new File(dataFolder, scenario.getId() + ".bin");

            if(!file.exists()) continue;

            try(final ObjectInputStream stream = new ObjectInputStream(Files.newInputStream(file.toPath()))) {
                final Object object = stream.readObject();
                if(object == null) continue;
                if(!(object instanceof Map<?, ?> dataMap)) continue;
                if(dataMap.isEmpty() || !(dataMap.keySet().iterator().next() instanceof String)) continue;
                final Map<String, Object> scenarioData = (Map<String, Object>) dataMap;

                scenario.loadData(scenarioData.get("data"));
                if(scenarioData.get("enabled").equals(true)) scenario.start();
            } catch (final IOException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        }

        if(logType.equals("FULL")) logger.info("[ScenarioHandler] Successfully loaded scenario data.");
    }

}
