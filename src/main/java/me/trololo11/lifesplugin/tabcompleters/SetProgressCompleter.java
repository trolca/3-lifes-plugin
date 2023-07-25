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

public class SetProgressCompleter implements TabCompleter {


    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!sender.hasPermission("lifesplugins2.admin")) return null;

        ArrayList<String> complete = new ArrayList<>();


        switch (args.length){

            case 1 ->{

                for(Player player : Bukkit.getOnlinePlayers()){
                    complete.add(player.getName());
                }
                complete.add("*");

            }

            case 2 -> complete.add("<table name of quest>");
            case 3 -> complete.add("<progress>");
        }

        return complete;
    }
}
