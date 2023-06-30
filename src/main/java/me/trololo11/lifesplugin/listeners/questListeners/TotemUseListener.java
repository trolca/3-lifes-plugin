package me.trololo11.lifesplugin.listeners.questListeners;

import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.utils.QuestListener;
import me.trololo11.lifesplugin.utils.questUtils.QuestClass;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import java.sql.SQLException;
import java.util.ArrayList;

public class TotemUseListener extends QuestListener {

    private LifesPlugin plugin = LifesPlugin.getPlugin();
    @EventHandler
    public void onUse(EntityDamageEvent e) throws SQLException {
        if(e.getEntity().getType() != EntityType.PLAYER) return;
        Player player = (Player) e.getEntity();
        double currHealth = player.getHealth() - e.getDamage();
        if(currHealth < 0.5){
            if(player.getInventory().getItemInOffHand().getType() != Material.TOTEM_OF_UNDYING
            && player.getInventory().getItemInMainHand().getType() != Material.TOTEM_OF_UNDYING)  return;

            checkTarget(null, player);
        }

    }
    @Override
    public ArrayList<QuestClass> getListenerArray() {
        return plugin.getListenerArrays().getTotemUseQuests();
    }
}
