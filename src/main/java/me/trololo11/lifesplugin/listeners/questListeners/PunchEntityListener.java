package me.trololo11.lifesplugin.listeners.questListeners;

import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.utils.QuestListener;
import me.trololo11.lifesplugin.utils.questUtils.QuestClass;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.sql.SQLException;
import java.util.ArrayList;

public class PunchEntityListener extends QuestListener {

    private LifesPlugin plugin = LifesPlugin.getPlugin();
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) throws SQLException {
        if(e.getDamager().getType() != EntityType.PLAYER) return;
        Player player = (Player) e.getDamager();

        checkTarget(e.getEntity().getType(), player);
    }

    @Override
    public ArrayList<QuestClass> getListenerArray() {
        return plugin.getListenerArrays().getPunchEntityQuests();
    }
}
