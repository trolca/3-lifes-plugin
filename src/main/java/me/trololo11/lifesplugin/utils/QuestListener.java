package me.trololo11.lifesplugin.utils;

import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.utils.questUtils.QuestClass;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.sql.SQLException;
import java.util.ArrayList;

public abstract class QuestListener implements Listener {

    public abstract ArrayList<QuestClass> getListenerArray();
    private LifesPlugin plugin = LifesPlugin.getPlugin();

    protected void checkTarget(Object target, OfflinePlayer p) throws SQLException {
        for (QuestClass quest : getListenerArray()) {

            if ((quest.getQuestTarget() == target || quest.getQuestTarget() == null) && !quest.hasCompleted(p) && !plugin.getDeadPlayers().contains(p)) {

                quest.setProgress(p, quest.getProgress(p) + 1);

            }
        }
    }

    protected void checkTarget(Object target, OfflinePlayer p, ArrayList<QuestClass> customList) throws SQLException {
        for (QuestClass quest : customList) {

            if ((quest.getQuestTarget() == target || quest.getQuestTarget() == null) && !quest.hasCompleted(p) && !plugin.getDeadPlayers().contains(p)) {

                quest.setProgress(p, quest.getProgress(p) + 1);

            }
        }
    }

    protected void checkTarget(Object target, OfflinePlayer p, int customAddProgress) throws SQLException {
        for (QuestClass quest : getListenerArray()) {

            if ((quest.getQuestTarget() == target || quest.getQuestTarget() == null) && !quest.hasCompleted(p) && !plugin.getDeadPlayers().contains(p)) {

                quest.setProgress(p, quest.getProgress(p) + customAddProgress);

            }
        }
    }

}
