package me.trololo11.lifesplugin.tasks;

import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.QuestsManager;
import me.trololo11.lifesplugin.database.LifesDatabase;
import me.trololo11.lifesplugin.database.TasksDatabase;
import me.trololo11.lifesplugin.utils.questUtils.QuestClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;

public class SaveDataToDatabaseTask extends BukkitRunnable {
    private final LifesPlugin plugin = LifesPlugin.getPlugin();
    private final QuestsManager questsManager;
    private final LifesDatabase lifesDatabase = plugin.getLifesDatabase();
    private final TasksDatabase tasksDatabase = plugin.getTasksDatabase();

    public SaveDataToDatabaseTask(QuestsManager questsManager){
        this.questsManager = questsManager;
    }
    @Override
    public void run() {

        for(Player player : Bukkit.getOnlinePlayers()){
            try {
                lifesDatabase.updatePlayerLifes(player.getUniqueId().toString(), plugin.getLifes(player.getUniqueId().toString()));
                lifesDatabase.updatePlayerStats(plugin.getPlayerStats(player));
                for (QuestClass quest : questsManager.getAllActiveQuests()) {
                    tasksDatabase.setTaskProgress(player.getUniqueId().toString(), quest.getProgress(player), quest.getTableName(), quest.getQuestType());
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }

        plugin.logger.info("[LifesPluginS2] Successfully saved data!");

    }
}
