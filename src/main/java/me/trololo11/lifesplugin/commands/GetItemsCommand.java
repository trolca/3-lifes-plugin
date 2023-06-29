package me.trololo11.lifesplugin.commands;

import me.trololo11.lifesplugin.RecipesManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GetItemsCommand implements CommandExecutor {

    private RecipesManager recipesManager;

    public GetItemsCommand(RecipesManager recipesManager){
        this.recipesManager = recipesManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(sender instanceof Player p){

            if(!p.hasPermission("lifesplugins2.admin")){
                p.sendMessage(ChatColor.RED + "You dont have the permission to do that command!");
                return true;
            }

            p.getInventory().addItem(recipesManager.getLifeItem());
            p.getInventory().addItem(recipesManager.getReviveCardItem());
            p.getInventory().addItem(recipesManager.getLifeShardItem());
            p.getInventory().addItem(recipesManager.getReviveShardItem());

        }

        return true;
    }
}
