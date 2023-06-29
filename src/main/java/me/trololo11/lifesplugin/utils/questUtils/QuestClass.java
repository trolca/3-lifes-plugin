package me.trololo11.lifesplugin.utils.questUtils;

import me.trololo11.lifesplugin.Language;
import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.database.LanguageManager;
import me.trololo11.lifesplugin.database.TasksDatabase;
import me.trololo11.lifesplugin.events.QuestFinishEvent;
import me.trololo11.lifesplugin.utils.PluginUtils;
import me.trololo11.lifesplugin.utils.questTypes.TaskListenerType;
import me.trololo11.lifesplugin.utils.questTypes.QuestType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class QuestClass {

    private final LifesPlugin plugin = LifesPlugin.getPlugin();
    private final HashMap<Player, Integer> playersProgress = new HashMap<>();
    //sooooo every QuestClass stores a individual hashMap of players progress because of the performance (more explenation in DatabaseManager class)
    private final HashMap<Language, String> names;
    private final HashMap<Language, ArrayList<String>> descriptions;
    private final String databaseName;
    private final Material icon;
    private final boolean showProgressBool;
    private final QuestType questType;
    private final int maxProgress;
    private final TaskListenerType listenerType;
    private final Object questTarget;


    public QuestClass(HashMap<Language, String> names,HashMap<Language, ArrayList<String>> decriptions, String databaseName, Material icon, boolean showProgressBool, QuestType questType, int maxProgress, TaskListenerType taskListenerType, Object questTarget, boolean isActive) {
        this.names = names;
        this.descriptions = decriptions;
        this.databaseName = databaseName;
        this.icon = icon;
        this.showProgressBool = showProgressBool;
        this.questType = questType;
        this.maxProgress = maxProgress;
        this.listenerType = taskListenerType;
        this.questTarget = questTarget;


        try {
            if(isActive)
                plugin.getTasksDatabase().createTaskTable(databaseName, questType.toString());
        } catch (SQLException e) {
            System.out.println("[LifesPluginS2] Error while creating table to quest "+ databaseName);
            System.out.println("[LifesPluginS2] For more info: ");
            e.printStackTrace();
        }
    }

    public String getName(Language language){
        return names.getOrDefault(language, names.getOrDefault(Language.ENGLISH, ChatColor.RED + "" + ChatColor.BOLD + "Error"));
    }

    public ArrayList<String> getDescription(Language language){
        ArrayList<String> description = descriptions.getOrDefault(language, descriptions.get(Language.ENGLISH));
        if(description == null){
            description = new ArrayList<>();
            description.add(ChatColor.WHITE + "Check your quest file cus it doesnt work lol");
        }

        return description;
    }

    public String getTableName() {
        return databaseName;
    }

    public Material getIcon() {
        return icon;
    }

    public int getProgress(OfflinePlayer player) throws SQLException {

        TasksDatabase tasksDatabase = plugin.getTasksDatabase();
        return player.isOnline() ? playersProgress.getOrDefault(player.getPlayer(), 0) : tasksDatabase.getTasksProgress(player.getUniqueId().toString(), getTableName(), getQuestType());
    }

    public boolean hasCompleted(OfflinePlayer p) throws SQLException {
        return getProgress(p) >= maxProgress;
    }

    public void setProgress(OfflinePlayer player, int newProgress) throws SQLException {
        if (player.isOnline()){
            playersProgress.put((Player) player, newProgress);
            if(hasCompleted(player.getPlayer())) Bukkit.getServer().getPluginManager().callEvent(new QuestFinishEvent(this, player.getPlayer()));
        }else {
            plugin.getTasksDatabase().setTaskProgress(player.getUniqueId().toString(), newProgress, getTableName(), getQuestType());
        }

    }

    public void setPluginSideProgress(Player player, int progress) throws SQLException {
        playersProgress.put(player, progress);
    }

    public void removePluginSideProgress(Player player) {
        playersProgress.remove(player);
    }

    public boolean showProgress() {
        return showProgressBool;
    }

    public QuestType getQuestType() {
        return questType;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public TaskListenerType getListenerType() {
        return listenerType;
    }

    public Object getQuestTarget() {
        return questTarget;
    }


}
