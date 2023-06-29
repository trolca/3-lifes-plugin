package me.trololo11.lifesplugin.listeners;

import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.QuestsManager;
import me.trololo11.lifesplugin.database.LifesDatabase;
import me.trololo11.lifesplugin.database.TasksDatabase;
import me.trololo11.lifesplugin.events.LifesChangeEvent;
import me.trololo11.lifesplugin.utils.PlayerStats;
import me.trololo11.lifesplugin.utils.questUtils.QuestClass;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;

public class DatabaseManager implements Listener {

    private final LifesPlugin plugin = LifesPlugin.getPlugin();
    private final QuestsManager questsManager;
    private final LifesDatabase lifesDatabase = plugin.getLifesDatabase();
    private final TasksDatabase tasksDatabase = plugin.getTasksDatabase();

    //sooooo bassically the system works this way that when player joins his values from database (so lifes or task progress) are stored locally in the plugin
    //to not have to every time we want to get a value it connects to the database and makes a SQL query and is slow
    //setting values DOES save in the plugin AND the database because i dont want to worry about reloads lol (yes im lazy)

    public DatabaseManager(QuestsManager questsManager) {
        this.questsManager = questsManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) throws SQLException {

        Player p = e.getPlayer();

        //gets lifes and when it gets code -404 (that means the player doesnt have a place in the table yet) adds this player to a database
        int lifes = lifesDatabase.getPlayerLifes(p.getUniqueId().toString());
        if (lifes == -404) {
            lifesDatabase.addPlayerToDatabase(p.getUniqueId().toString(), 3, false);
            lifes = 3;
        }

        plugin.getServer().getPluginManager().callEvent(new LifesChangeEvent(p, lifes));
        plugin.addPlayerLifes(p.getUniqueId().toString(), lifes);
        questsManager.setupTakenAwardsForPlayer(p);

        PlayerStats playerStats = lifesDatabase.getPlayerStats(p.getUniqueId().toString());
        if(playerStats == null){
            lifesDatabase.addPlayerToStats(new PlayerStats(p, 0, 0, 0, 0, 0, 0, 0, 0));
            playerStats = lifesDatabase.getPlayerStats(p.getUniqueId().toString());
        }

        plugin.setPlayerStats(p, playerStats);

        //when it detects that someone has revived the player while he was offline it revives him on join :>
        if(lifesDatabase.getIsPlayerRevived(p.getUniqueId().toString())){
            plugin.getServer().getPluginManager().callEvent(new LifesChangeEvent(p, 3));
            p.setHealth(0);
            p.setGameMode(GameMode.SURVIVAL);
            p.sendMessage(ChatColor.GOLD + "You have been revived!");
            //updates the database to say that he is not in need to be rivived on next join
            lifesDatabase.updatePlayerIsRevived(p.getUniqueId().toString(), false);

            playerStats.setBeenRevived(playerStats.getBeenRevived()+1);

            plugin.setPlayerStats(p, playerStats);
        }

        plugin.setPlayerLanguage(p, lifesDatabase.getPlayerLanguage(p.getUniqueId().toString()));





        //and you can say its unoptimized buttt i say its a feature :>
        for (QuestClass quest : questsManager.getAllActiveQuests()) {
            quest.setPluginSideProgress(p, tasksDatabase.getTasksProgress(p.getUniqueId().toString(), quest.getTableName(), quest.getQuestType()));
        }

        questsManager.calculatePlayersFinishedQuests(p);


    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) throws SQLException {
        Player p = e.getPlayer();

        lifesDatabase.updatePlayerLifes(p.getUniqueId().toString(), plugin.getLifes(p.getUniqueId().toString()));

        plugin.removeLifeStat(p.getUniqueId().toString());
        plugin.removePlayerLanguage(p);

        lifesDatabase.updatePlayerStats(plugin.getPlayerStats(p));
        plugin.removePlayerStats(p);

        //the same I said in the onJoin but x2 value :>>
        for (QuestClass quest : questsManager.getAllActiveQuests()) {
            tasksDatabase.setTaskProgress(p.getUniqueId().toString(), quest.getProgress(p), quest.getTableName(), quest.getQuestType());
            quest.removePluginSideProgress(p);
        }

        questsManager.removePlayerFromCompletedQuests(p);
        questsManager.removePlayerFromTakenHashMaps(p);
    }
}
