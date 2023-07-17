package me.trololo11.lifesplugin.tasks;

import me.trololo11.lifesplugin.Language;
import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.database.LanguageManager;
import me.trololo11.lifesplugin.utils.PluginUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PvpTurnOnTask extends BukkitRunnable {

    private int time;
    private int localTime = 0;
    private LifesPlugin plugin = LifesPlugin.getPlugin();

    public PvpTurnOnTask(int time){
        this.time = time;
    }

    @Override
    public void run() {
        time--;


        switch (time){
            case 600 -> announceMessage("pvp-left-mins-announcement", 10);
            case 300 -> announceMessage("pvp-left-mins-announcement", 5);
            case 60 -> announceMessage("pvp-left-1-min-announcement", 1);
            case 1 -> announceMessage("pvp-left-1-sec-announcement", 1);
        }

        if(time <= 10 && time > 1){
            announceMessage("pvp-left-secs-announcement", time);
        }else if(time <= 0){
            Bukkit.getWorld("world").setPVP(true);
            Bukkit.getWorld("world_nether").setPVP(true);
            Bukkit.getWorld("world_the_end").setPVP(true);


            for(Player player : Bukkit.getOnlinePlayers()){
                Language language = plugin.getPlayerLanguage(player);

                player.sendMessage(ChatColor.RED + String.valueOf(ChatColor.BOLD) + LanguageManager.getLangLine("pvp-turned-on-announcement", language).toUpperCase());
                player.sendTitle(ChatColor.RED + String.valueOf(ChatColor.BOLD) + LanguageManager.getLangLine("pvp-turned-on-announcement", language).toUpperCase(), "", 5, 20, 15);
                player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1f, 1f);

            }

            cancel();

        }


    }

    private void announceMessage(String key, int time){

        for(Player player : Bukkit.getOnlinePlayers()){
            Language language = plugin.getPlayerLanguage(player);
            String message = (PluginUtils.chat("&2&l"+ LanguageManager.getLangLine(key, language)));
            message = key.equalsIgnoreCase("pvp-left-1-sec-announcement") || key.equalsIgnoreCase("pvp-left-1-min-announcement") ?
                    message.replace("1", PluginUtils.chat("&a&l1&2&l")) : message.replace("<time>", PluginUtils.chat("&a&l"+time + "&2&l"));

            player.sendMessage(message);

        }

    }
}
