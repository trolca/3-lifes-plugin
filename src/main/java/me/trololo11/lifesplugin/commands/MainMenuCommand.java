package me.trololo11.lifesplugin.commands;

import me.trololo11.lifesplugin.commands.subcommands.DailyQuestSubcommand;
import me.trololo11.lifesplugin.commands.subcommands.StatsQuestSubcommand;
import me.trololo11.lifesplugin.commands.subcommands.WeeklyQuestSubCommand;
import me.trololo11.lifesplugin.utils.Menus;
import me.trololo11.lifesplugin.utils.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class MainMenuCommand implements CommandExecutor {

    private ArrayList<SubCommand> subCommands = new ArrayList<>();

    public MainMenuCommand(){
        subCommands.add(new DailyQuestSubcommand());
        subCommands.add(new WeeklyQuestSubCommand());
        subCommands.add(new StatsQuestSubcommand());
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            if(args.length == 0) Menus.getMainLifesMenu().open(p);
            else{
                String arg = args[0];

                for(SubCommand subCommand : subCommands){


                    if(subCommand.getName().equalsIgnoreCase(arg)){
                        subCommand.perform(p);
                        break;
                    }


                }
            }
        }

        return true;
    }
}
