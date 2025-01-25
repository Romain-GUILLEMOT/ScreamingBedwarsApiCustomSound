package fr.yotapaki.screamingBedWarsApiCustomSound;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.screamingsandals.bedwars.api.events.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class EventManager implements Listener {

    private final Main plugin;

    public EventManager(Main main) {
        this.plugin = main;
        // Register EventManager as Listener
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void onPlayerKilled(BedwarsPlayerKilledEvent event) {
        String message;
        if (event.getKiller() != null) {
            message = ChatColor.RED + "[BW Custom Sounds] " + ChatColor.YELLOW +
                    "Player " + ChatColor.AQUA + event.getPlayer().getName() +
                    ChatColor.YELLOW + " was killed by " + ChatColor.AQUA + event.getKiller().getName() + ".";
            playSound(event.getKiller(), plugin.getConfigValue("kill").toString());
        } else {
            message = ChatColor.RED + "[BW Custom Sounds] " + ChatColor.YELLOW +
                    "Player " + ChatColor.AQUA + event.getPlayer().getName() +
                    ChatColor.YELLOW + " was killed by himself.";
            playSound(event.getPlayer(), plugin.getConfigValue("kill").toString());
        }
        plugin.broadcastDebugMessage(message);

        List<Player> playersOnTeam = event.getGame().getTeamOfPlayer(event.getPlayer()).getConnectedPlayers();
        boolean allDead = true;
        if(playersOnTeam.size() > 1 ) {
            for (Player player : playersOnTeam) {
                if (!player.isDead()) {
                    allDead = false;
                    break;
                }
            }
        }
        plugin.broadcastDebugMessage(message + " All dead: " + allDead);

        if (allDead) {
            for(Player player : playersOnTeam) {
                player.stopAllSounds();
                playSound(player, plugin.getConfigValue("team_dead").toString());
            }
            message = ChatColor.RED + "[BW Custom Sounds] " + ChatColor.YELLOW +
                    "Team " + ChatColor.AQUA + event.getGame().getTeamOfPlayer(event.getPlayer()).getName() +
                    ChatColor.YELLOW + " is dead.";
            plugin.broadcastDebugMessage(message);
            return;
        }

        AtomicInteger countdown = new AtomicInteger(5);
        Bukkit.getScheduler().runTaskTimer(plugin, task -> {
            if (countdown.get() <= 0) {
                Bukkit.getScheduler().cancelTask(task.getTaskId());
                return;
            }


            if (!event.getPlayer().isOnline()) {
                Bukkit.getScheduler().cancelTask(task.getTaskId());
                return;
            }
            plugin.broadcastDebugMessage("Play : " + "respawn_" + countdown.get());
            playSound(event.getPlayer(), plugin.getConfigValue("respawn_" + countdown.get()).toString());
            String countdownMessage = ChatColor.RED + "[BW Custom Sounds] " + ChatColor.YELLOW +
                    "Respawn in " + ChatColor.AQUA + countdown.get() + ChatColor.YELLOW +
                    " seconds for player " + ChatColor.AQUA + event.getPlayer().getName() + ".";
            plugin.broadcastDebugMessage(countdownMessage);

            countdown.getAndDecrement();
        }, 20, 20);

    }

    @EventHandler
    public void onBedDestroyed(BedwarsBedDestroyedMessageSendEvent event) {
        String victimTeam = event.getVictim() != null ? event.getVictim().getDisplayName() : "unknown";
        String destroyer = event.getDestroyer() != null ? event.getDestroyer().getDisplayName() : "unknown";
        if(victimTeam.equals(destroyer)) {
            return;
        }
        playSound(event.getDestroyer(), plugin.getConfigValue("bed_destroyed").toString());
        playSound(event.getVictim(), plugin.getConfigValue("my_bed_destroyed").toString());
        String message = ChatColor.RED + "[BW Custom Sounds] " + ChatColor.YELLOW +
                "The bed of team " + ChatColor.AQUA + victimTeam +
                ChatColor.YELLOW + " was destroyed by " + ChatColor.AQUA + destroyer + ".";
        plugin.broadcastDebugMessage(message);
    }

    @EventHandler
    public void onGameStarted(BedwarsGameStartEvent event) {

        String message = ChatColor.RED + "[BW Custom Sounds] " + ChatColor.GREEN +
                "The Bedwars game " + event.getGame().getName() + " has started!";
        for (Player player : event.getGame().getConnectedPlayers()) {
            player.stopAllSounds();
        }
        for (Player player : event.getGame().getConnectedPlayers()) {
            plugin.broadcastDebugMessage("Play : " + plugin.getConfigValue("game_start").toString());
            playSound(player, plugin.getConfigValue("game_start").toString());
        }
        plugin.broadcastDebugMessage(message);
    }
    @EventHandler
    public void onGameEnd(BedwarsGameEndEvent event) {
        String message = ChatColor.RED + "[BW Custom Sounds] " + ChatColor.GREEN +
                "The Bedwars game has ended.";
        for (Player player : event.getGame().getConnectedPlayers()) {
            player.stopAllSounds();
        }
        plugin.broadcastDebugMessage(message);
    }

    public static void stopAllMusic() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.stopAllSounds();
        }
        Bukkit.getLogger().info(ChatColor.RED + "[BW Custom Sounds] " + ChatColor.YELLOW +
                "All sounds have been stopped for all players.");
    }

    public static void playSound(Player player, String sound) {
        if(sound.isEmpty()) {
            return;
        }
        player.playSound(player.getLocation(), sound, SoundCategory.RECORDS, 1, 1);
    }


}