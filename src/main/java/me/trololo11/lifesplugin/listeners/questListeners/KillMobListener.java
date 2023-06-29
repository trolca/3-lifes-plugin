package me.trololo11.lifesplugin.listeners.questListeners;

import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.utils.QuestListener;
import me.trololo11.lifesplugin.utils.questUtils.QuestClass;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.sql.SQLException;
import java.util.ArrayList;

public class KillMobListener extends QuestListener {

    //Soo in short it cycles every task which has the tag if KILL_MOB (or other idk) and checks if the value from the target is the same as the mob the player just killed
    //It kinda sucks but I think its the best option for custom tasks (or quests)
    //Oh and im not gonna explain the same system like a million times so if you are here from other classes     hello and the idea for the listener you came from is the same just listens for different things :>


    private LifesPlugin plugin = LifesPlugin.getPlugin();

    @Override
    public ArrayList<QuestClass> getListenerArray() {
        return plugin.getListenerArrays().getKillQuests();
    }

    @EventHandler
    public void onKill(EntityDeathEvent e) throws SQLException {
        if (e.getEntity().getKiller() == null) return;
        Player p = e.getEntity().getKiller();
        EntityType entityType = e.getEntityType();

        checkTarget(entityType, p);

    }
}



