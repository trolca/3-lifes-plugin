package me.trololo11.lifesplugin.listeners.questListeners;

import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.utils.QuestListener;
import me.trololo11.lifesplugin.utils.questUtils.QuestClass;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLevelChangeEvent;

import java.sql.SQLException;
import java.util.ArrayList;

public class PlayerLevelUpListener extends QuestListener implements Listener {

    private LifesPlugin plugin = LifesPlugin.getPlugin();

    @EventHandler
    public void onLevelUp(PlayerLevelChangeEvent e) throws SQLException {

        int howMuch = e.getNewLevel() - e.getOldLevel();

        if(howMuch <= 0) return;

        Player p = e.getPlayer();

        if(plugin.getDeadPlayers().contains(p)) return;

        for(QuestClass quest : getListenerArray()){

             if(!quest.hasCompleted(p)) quest.setProgress(p, quest.getProgress(p)+howMuch);

        }


    }

    @Override
    public ArrayList<QuestClass> getListenerArray() {
        return plugin.getListenerArrays().getLevelUpQuests();
    }
}
