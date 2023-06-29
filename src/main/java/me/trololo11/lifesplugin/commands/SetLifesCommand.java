package me.trololo11.lifesplugin.commands;

import me.trololo11.lifesplugin.events.LifesChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetLifesCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(sender instanceof Player p){

            if(!p.hasPermission("lifesplugins2.admin")){
                p.sendMessage(ChatColor.RED + "You dont have the permission! And kinda sus");
                return true;
            }

            if(args.length == 0){
                p.sendMessage(ChatColor.RED + "You need to type the name of the player you want to change lifes!");
                return true;
            }

            if(args.length == 1){
                p.sendMessage(ChatColor.RED + "You need to type the number of lifes to set!");
                return true;
            }

            Player playerSetLifes = Bukkit.getPlayerExact(args[0]);
            if(playerSetLifes == null){
                p.sendMessage(ChatColor.RED + "That player doesn't exist or simply isn't online!");
                return true;
            }

            int lifes;

            try {
                lifes = Integer.parseInt(args[1]);
            }catch (NumberFormatException e){
                p.sendMessage(ChatColor.RED + "Please type a correct number of new lifes!");
                return true;
            }

            Bukkit.getPluginManager().callEvent(new LifesChangeEvent(playerSetLifes, lifes));

            p.sendMessage(ChatColor.GREEN + "Successfully set "+lifes+" lifes for "+ playerSetLifes.getName());
            p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);


        }

        return true;
    }
}
