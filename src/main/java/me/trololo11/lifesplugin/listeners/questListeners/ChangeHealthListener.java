package me.trololo11.lifesplugin.listeners.questListeners;

import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.utils.QuestListener;
import me.trololo11.lifesplugin.utils.questUtils.QuestClass;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import java.sql.SQLException;
import java.util.ArrayList;

public class ChangeHealthListener extends QuestListener {

    private LifesPlugin plugin = LifesPlugin.getPlugin();

    @EventHandler
    public void onChangeHealth(EntityDamageEvent e) throws SQLException {
        if(e.getEntity() instanceof Player p) checkCustomTarget((int) p.getHealth(), p);

    }

    @EventHandler
    public void onHealHeath(EntityRegainHealthEvent e) throws SQLException {
        if(e.getEntity() instanceof Player p) checkCustomTarget((int) p.getHealth(), p);
    }


    protected void checkCustomTarget(Integer target, Player p) throws SQLException {
        for (QuestClass quest : getListenerArray()) {


            if ((Integer) quest.getQuestTarget() >= target && !quest.hasCompleted(p) && !plugin.getDeadPlayers().contains(p)) {

                quest.setProgress(p, quest.getProgress(p) + 1);

            }
        }
    }

    @Override
    public ArrayList<QuestClass> getListenerArray() {
        return plugin.getListenerArrays().getHaveHeartQuests();
    }
}
