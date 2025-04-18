package me.RareHyperIon.BlockTrials.handler;

import me.RareHyperIon.BlockTrials.BlockTrials;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class LanguageHandler {

    private final BlockTrials plugin;

    private final Map<String, String> translations = new HashMap<>();

    public LanguageHandler(final BlockTrials plugin) {
        this.plugin = plugin;
    }

    public final void load() {
        final Logger logger = this.plugin.getLogger();
        final String logType = this.plugin.logType();

        logger.info("[LanguageHandler] Loading language data...");

        final String language = this.plugin.getConfig().getString("language", "EN-US");

        if(!(new File(this.plugin.getDataFolder(), "language")).exists()) {
            if(logType.equals("FULL")) logger.info("[LanguageHandler] Language folder not found. Creating default languages...");
            this.saveDefault();
        }

        final File file = new File(plugin.getDataFolder(), "language/" + language + ".yml");

        if(!file.exists()) {
            throw new IllegalStateException("The specified language  \"" + language + "\" doesn't have any translations.");
        }

        if(logType.equals("FULL")) logger.info("[LanguageHandler] Loading translations from '" + language.toUpperCase() + ".yml'");

        final FileConfiguration lang = YamlConfiguration.loadConfiguration(file);
        final String prefix = lang.getString("prefix");

        for(final String key : lang.getKeys(false)) {
            this.translations.put(key, lang.getString(key).replaceAll("\\{prefix}", prefix));
        }

        if(logType.equals("FULL")) logger.info("[LanguageHandler] Language data loaded successfully.");
    }

    public void addScenario(final String scenarioId, final String fieldId, final String translation) {
        this.translations.put("scenarios." + scenarioId+ "." + fieldId, translation);
    }

    public final String get(final String key) {
        return this.translations.get(key);
    }

    private void saveDefault() {
        final File folder = new File(this.plugin.getDataFolder(), "language");
        if(!folder.mkdirs()) throw new IllegalStateException("Failed to create language folder.");

        for(final String language : List.of("EN-US.yml")) {
            final File out = new File(folder, language);

            try(final InputStream stream = this.plugin.getResource("language/" + language)) {
                if(stream == null) throw new IllegalStateException("Resource \"" + language + "\" was not found within jar.");
                Files.copy(stream, out.toPath());
                if(this.plugin.logType().equals("FULL")) this.plugin.getLogger().info("[LanguageHandler] Default language file created: " + language);
            } catch (final IOException e) {
                throw new IllegalStateException("Failed to create default language file: " + language, e);
            }
        }
    }

}
