package me.trololo11.lifesplugin.commands;

import me.trololo11.lifesplugin.QuestsManager;
import me.trololo11.lifesplugin.utils.questUtils.QuestClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.InputMismatchException;

public class SetQuestProgressCommand implements CommandExecutor {

    private QuestsManager questsManager;

    public SetQuestProgressCommand(QuestsManager questsManager){
        this.questsManager = questsManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!sender.hasPermission("lifesplugins2.admin")){
            sender.sendMessage(ChatColor.RED + "You dont have the permission! Thats really sus");
            return true;
        }

        if(args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Napisz gracza ktoremu chcesz progress dac!");
            return true;
        }

        Player player = Bukkit.getPlayer(args[0]);
        if(player == null && !args[0].equalsIgnoreCase("*")){
            sender.sendMessage(ChatColor.RED + "Ten gracz nie jest online!");
            return true;
        }

        if(args.length == 1){
            sender.sendMessage(ChatColor.RED + "Napisz nazwe questa");
            return true;
        }

        QuestClass quest = null;

        for(QuestClass questClass : questsManager.getAllActiveQuests()){
            if(questClass.getTableName().equalsIgnoreCase(args[1])){
                quest = questClass;
                break;
            }
        }

        if(quest == null){
            sender.sendMessage(ChatColor.RED + "Nie ma takiego questa!");
            return true;
        }

        if(args.length == 2){
            sender.sendMessage(ChatColor.RED + "Napisz progress");
            return true;
        }
        int newProgress;
        try {
             newProgress = Integer.parseInt(args[2]);
        }catch (InputMismatchException e){
            sender.sendMessage(ChatColor.RED + "Daj correct liczbe pls");
            return true;
        }

        try {

            if(!args[0].equalsIgnoreCase("*")) quest.setProgress(player, newProgress);
            else{

                for(Player player1 : Bukkit.getOnlinePlayers()){
                    quest.setProgress(player1, newProgress);
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return true;
    }
}
