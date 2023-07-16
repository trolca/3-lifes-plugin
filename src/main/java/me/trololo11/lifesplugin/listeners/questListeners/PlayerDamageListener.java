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

public class PlayerDamageListener extends QuestListener {
    private LifesPlugin plugin = LifesPlugin.getPlugin();

    @EventHandler
    public void onDamage(EntityDamageEvent e) throws SQLException {
        if(e.getEntity().getType() != EntityType.PLAYER) return;
        Player player = (Player) e.getEntity();

        checkTarget(e.getCause(), player);
    }
    @Override
    public ArrayList<QuestClass> getListenerArray() {
        return plugin.getListenerArrays().getPlayerDamageQuests();
    }
}
