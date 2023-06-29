package me.trololo11.lifesplugin.tabcompleters;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class MenuCommandTabCompleter implements TabCompleter {


    @Override
    public List<String> onTabComplete(CommandSender sender,  Command command,  String label, String[] args) {


        ArrayList<String> completer = new ArrayList<>();
        if(args.length == 1) {
            completer.add("daily");
            completer.add("weekly");
            completer.add("stats");
        }


        return completer;
    }
}
