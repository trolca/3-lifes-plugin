package me.trololo11.lifesplugin.commands;

import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.RecipesManager;
import me.trololo11.lifesplugin.database.LanguageManager;
import me.trololo11.lifesplugin.events.LifesChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.bukkit.Color;

public class TakeLifeCommand implements CommandExecutor {

    private LifesPlugin plugin = LifesPlugin.getPlugin();
    private RecipesManager recipesManager;

    public TakeLifeCommand(RecipesManager recipesManager){
        this.recipesManager = recipesManager;
    }

    @Override
    public boolean onCommand( CommandSender sender,  Command command,  String label,  String[] args) {

        if(sender instanceof Player p){

            int lifes = plugin.getLifes(p.getUniqueId().toString());

            if(lifes <= 1){
                p.sendMessage(ChatColor.RED + LanguageManager.getLangLine("takelife-command-error", plugin.getPlayerLanguage(p)));
                return true;
            }


            Bukkit.getServer().getPluginManager().callEvent(new LifesChangeEvent(p, lifes-1));
            p.getInventory().addItem(recipesManager.getPlayersLife(p));
            p.playSound(p.getLocation(), Sound.BLOCK_METAL_BREAK, 10f, 1.5f);
            p.spawnParticle(Particle.REDSTONE, p.getLocation(), 500,  1, 1, 1, new Particle.DustOptions(Color.fromBGR(0, 0, 255), 1f));


        }

        return true;
    }
}
