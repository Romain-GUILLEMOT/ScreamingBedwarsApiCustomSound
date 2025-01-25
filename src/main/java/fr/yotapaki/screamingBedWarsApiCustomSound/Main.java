package fr.yotapaki.screamingBedWarsApiCustomSound;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public final class Main extends JavaPlugin {

    private ConfigManager configManager;

    private EventManager eventManager;

    private Set<Player> debugPlayers = new HashSet<>();


    @Override
    public void onEnable() {
        getLogger().info("Bedwars Custom sounds is starting...");
        eventManager = new EventManager(this);
        configManager = new ConfigManager(this);
        configManager.loadConfig();

        // Enregistrer les commandes
        getCommand("bwcs").setExecutor(new CommandManager(this, eventManager));

        getLogger().info("Bedwars Custom sounds has been successfully enabled.");
    }
    @Override
    public void onDisable() {
        getLogger().info("Bedwars Custom sounds is shutting down...");
        EventManager.stopAllMusic();
        getLogger().info("Bedwars Custom sounds has been successfully disabled.");
    }
    public ConfigManager getConfigManager() {
        return configManager;
    }
    public void toggleDebugMode(Player player) {
        if (debugPlayers.contains(player)) {
            debugPlayers.remove(player);
        } else {
            debugPlayers.add(player);
        }
    }
    public boolean isDebugMode(Player player) {
        return debugPlayers.contains(player);
    }

    public void broadcastDebugMessage(String message) {
        getLogger().info(message);

        for (Player player : debugPlayers) {
            player.sendMessage(message);
        }
    }
    public Object getConfigValue(String key) {
        return configManager.getConfigValue(key);
    }
}
