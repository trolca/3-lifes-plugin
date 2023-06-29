package me.trololo11.lifesplugin.listeners.lifeslisteners;

import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.utils.PlayerStats;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerKillSomeoneListener implements Listener {

    private LifesPlugin plugin = LifesPlugin.getPlugin();

    @EventHandler
    public void onKill(PlayerDeathEvent e){
       if(e.getEntity().getKiller() == null) return;
       Player killer = e.getEntity().getKiller();

        //adds a kill stat when player is killed
        PlayerStats playerStats = plugin.getPlayerStats(killer);

        playerStats.setKills(playerStats.getKills()+1);
        plugin.setPlayerStats(killer, playerStats);
    }
}
