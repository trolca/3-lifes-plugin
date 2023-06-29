package me.trololo11.lifesplugin.listeners.lifeslisteners;

import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.events.LifesChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    private LifesPlugin plugin = LifesPlugin.getPlugin();


    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        //it just decreases the number of lifes by 1
        Player p = e.getEntity();
        int lifes = plugin.getLifes(p.getUniqueId().toString());
        Bukkit.getServer().getPluginManager().callEvent(new LifesChangeEvent(p, lifes - 1));

    }
}
