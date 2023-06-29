package me.trololo11.lifesplugin.listeners.questListeners;

import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.utils.QuestListener;
import me.trololo11.lifesplugin.utils.questUtils.QuestClass;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityTameEvent;

import java.sql.SQLException;
import java.util.ArrayList;

public class TameEntityListener extends QuestListener {
    private LifesPlugin plugin = LifesPlugin.getPlugin();

    @EventHandler
    public void onTame(EntityTameEvent e) throws SQLException {
        Player tamer = (Player) e.getOwner();
        checkTarget(e.getEntity().getType(), tamer);
    }

    @Override
    public ArrayList<QuestClass> getListenerArray() {
        return plugin.getListenerArrays().getTameEntityQuests();
    }
}
