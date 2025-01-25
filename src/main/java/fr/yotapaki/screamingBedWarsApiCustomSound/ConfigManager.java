package fr.yotapaki.screamingBedWarsApiCustomSound;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {

    private final Main plugin;
    private JsonObject config; // Configuration en mémoire

    public ConfigManager(Main plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        File configFile = new File(plugin.getDataFolder(), "config.json");
        if (!configFile.exists()) {
            saveDefaultConfig(configFile);
        }

        try (FileReader reader = new FileReader(configFile)) {
            config = JsonParser.parseReader(reader).getAsJsonObject();
            plugin.getLogger().info("Configuration loaded into memory.");
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to load configuration: " + e.getMessage());
        }
    }

    private void saveDefaultConfig(File configFile) {
        try {
            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }

            JsonObject defaultConfig = new JsonObject();

            // Ajouter les sons par défaut directement dans la configuration principale
            defaultConfig.addProperty("killed", "minecraft:entity.player.death");
            defaultConfig.addProperty("kill", "minecraft:entity.player.levelup");
            defaultConfig.addProperty("team_dead", "minecraft:entity.wither.spawn");
            defaultConfig.addProperty("respawn_countdown", "minecraft:block.note_block.pling");
            defaultConfig.addProperty("respawn_done", "minecraft:entity.enderman.teleport");
            defaultConfig.addProperty("bed_destroyed", "minecraft:entity.ender_dragon.growl");
            defaultConfig.addProperty("my_bed_destroyed", "minecraft:entity.lightning_bolt.thunder");
            defaultConfig.addProperty("game_start", "minecraft:ui.button.click");
            defaultConfig.addProperty("start_countdown", "minecraft:ui.toast.in");
            defaultConfig.addProperty("respawn_5", "minecraft:ui.toast.in");
            defaultConfig.addProperty("respawn_4", "minecraft:ui.toast.in");
            defaultConfig.addProperty("respawn_3", "minecraft:ui.toast.in");
            defaultConfig.addProperty("respawn_2", "minecraft:ui.toast.in");
            defaultConfig.addProperty("respawn_1", "minecraft:ui.toast.in");



            try (FileWriter writer = new FileWriter(configFile)) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(defaultConfig, writer);
            }

            plugin.getLogger().info("Default configuration file created successfully.");
        } catch (IOException e) {
            plugin.getLogger().severe("Error creating default configuration file: " + e.getMessage());
        }
    }

    public String getConfigValue(String key) {
        if (config == null) {
            plugin.getLogger().severe("Configuration is not loaded into memory!");
            return null;
        }

        if (config.has(key)) {
            return config.get(key).getAsString();
        }

        plugin.getLogger().warning("Key not found in configuration: " + key);
        return null;
    }

    public JsonObject getConfig() {
        return config;
    }
}