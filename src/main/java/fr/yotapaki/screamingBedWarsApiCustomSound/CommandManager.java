package fr.yotapaki.screamingBedWarsApiCustomSound;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor {

    private final Main plugin;
    private final EventManager eventManager;

    public CommandManager(Main plugin, EventManager eventManager) {
        this.plugin = plugin;
        this.eventManager = eventManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            showHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "help":
                showHelp(sender);
                break;

            case "reload":
                if (!sender.hasPermission("bedwarscustomsounds.reload")) {
                    sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission de recharger la configuration.");
                    return true;
                }
                plugin.getConfigManager().loadConfig();
                sender.sendMessage(ChatColor.GREEN + "Configuration rechargée avec succès !");
                break;

            case "debug":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "Le mode debug ne peut être activé que par un joueur.");
                    return true;
                }
                if (!sender.hasPermission("bedwarscustomsounds.debug")) {
                    sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'activer le mode debug.");
                    return true;
                }
                Player player = (Player) sender;
                plugin.toggleDebugMode(player);
                if(plugin.isDebugMode(player)) {
                    sender.sendMessage(ChatColor.YELLOW + "Mode debug activé pour " + player.getName() + ".");
                } else {
                    sender.sendMessage(ChatColor.YELLOW + "Mode debug désactiver pour " + player.getName() + ".");
                }
                // Exemple : Activer/désactiver un mode debug (logique à ajouter selon ton besoin)
                break;

            default:
                sender.sendMessage(ChatColor.RED + "Commande inconnue. Utilisez /" + label + " help pour la liste des commandes.");
                break;
        }
        return true;
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "======== " + ChatColor.AQUA + "Bedwars Custom Sounds" + ChatColor.GOLD + " ========");
        sender.sendMessage(ChatColor.YELLOW + "/becs help" + ChatColor.GRAY + " - Affiche ce message d'aide.");
        sender.sendMessage(ChatColor.YELLOW + "/becs reload" + ChatColor.GRAY + " - Recharge la configuration du plugin.");
        sender.sendMessage(ChatColor.YELLOW + "/becs debug" + ChatColor.GRAY + " - Active le mode debug (joueurs uniquement).");
        sender.sendMessage(ChatColor.GOLD + "=======================================");
    }
}