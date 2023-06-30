package me.trololo11.lifesplugin.listeners.questListeners;

import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.utils.QuestListener;
import me.trololo11.lifesplugin.utils.questUtils.QuestClass;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import java.sql.SQLException;
import java.util.ArrayList;

public class PlayerOnFireListener extends QuestListener {

    private LifesPlugin plugin = LifesPlugin.getPlugin();
    @EventHandler
    public void onDamage(EntityDamageEvent e) throws SQLException {
        if(e.getEntity().getType() != EntityType.PLAYER) return;
        if(e.getCause() != EntityDamageEvent.DamageCause.FIRE && e.getCause() != EntityDamageEvent.DamageCause.FIRE_TICK) return;
        Player player = (Player) e.getEntity();

        checkTarget(null, player);
    }

    @Override
    public ArrayList<QuestClass> getListenerArray() {
        return plugin.getListenerArrays().getOnFireQuests();
    }
}
