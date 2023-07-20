package me.trololo11.lifesplugin.commands;

import me.trololo11.lifesplugin.Language;
import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.database.LanguageManager;
import me.trololo11.lifesplugin.tasks.PvpTurnOnTask;
import me.trololo11.lifesplugin.utils.PluginUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StartServerCommand implements CommandExecutor {

    private LifesPlugin plugin = LifesPlugin.getPlugin();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("lifesplugins2.admin")){
            sender.sendMessage(ChatColor.RED + "Pls dont start this serwer withough permission :>");
            return true;
        }

        Bukkit.getWorld("world").setPVP(false);
        Bukkit.getWorld("world_nether").setPVP(false);
        Bukkit.getWorld("world_the_end").setPVP(false);

        new PvpTurnOnTask(1800).runTaskTimer(plugin, 20L, 20L);

        for(Player player : Bukkit.getOnlinePlayers()){
            Language language = plugin.getPlayerLanguage(player);

            player.sendMessage(PluginUtils.chat("&2&l"+LanguageManager.getLangLine("pvp-left-mins-announcement", language).replace("<time>", "&a&l30&2&l")));
        }

        return true;
    }
}
