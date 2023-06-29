package me.trololo11.lifesplugin.tabcompleters;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SetLifesTabCompleter implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        ArrayList<String> tabList = new ArrayList<>();

        if(!sender.hasPermission("lifesplugins2.admin")) return tabList;

        if(args.length == 1){
            for(Player player : Bukkit.getOnlinePlayers()) tabList.add(player.getName());
        }

        if(args.length == 2) tabList.add("<number of lifes>");


        return tabList;
    }
}
