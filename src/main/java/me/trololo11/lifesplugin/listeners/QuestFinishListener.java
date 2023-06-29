package me.trololo11.lifesplugin.listeners;

import me.trololo11.lifesplugin.Language;
import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.QuestsManager;
import me.trololo11.lifesplugin.database.LanguageManager;
import me.trololo11.lifesplugin.events.QuestFinishEvent;
import me.trololo11.lifesplugin.utils.PlayerStats;
import me.trololo11.lifesplugin.utils.PluginUtils;
import me.trololo11.lifesplugin.utils.questTypes.QuestType;
import me.trololo11.lifesplugin.utils.questUtils.QuestClass;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.sql.SQLException;
import java.util.ArrayList;

public class QuestFinishListener implements Listener {

    private QuestsManager questsManager;
    private LifesPlugin plugin = LifesPlugin.getPlugin();

    public QuestFinishListener(QuestsManager questsManager){
        this.questsManager = questsManager;
    }

    @EventHandler
    public void onFinish(QuestFinishEvent e) throws SQLException {
        Player p = e.getPlayer();
        QuestClass quest = e.getQuest();
        Language language = plugin.getPlayerLanguage(p);

        //sends the completed quest message
        p.sendMessage(PluginUtils.chat("&a"+ LanguageManager.getLangLine("quest-complete-text", language).replace("<quest>", "&f["+quest.getName(language)+"&f]")));
        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
        PlayerStats playerStats = plugin.getPlayerStats(p);

        playerStats.setAllQuestCompleted(playerStats.getAllQuestCompleted()+1);
        //in order to count all completed quests from a page it has to add one for every page
        if(quest.getQuestType() == QuestType.DAILY){
            questsManager.addPlayerCompletedDailyQuest(p);
            playerStats.setDailyQuestCompleted(playerStats.getDailyQuestCompleted()+1);
        }
        else{
            questsManager.addPlayerCompletedWeeklyQuest(p);
            playerStats.setWeeklyQuestCompleted(playerStats.getWeeklyQuestCompleted()+1);
        }

        plugin.setPlayerStats(p, playerStats);

        //if player completes a page it send the custom message
        if(questsManager.getPlayerCompletedDailyQuests(p) >= questsManager.getDailyQuests().size() && e.getQuest().getQuestType() == QuestType.DAILY){
            ArrayList<String> completedText = LanguageManager.getLangArrayLine("daily-quest-page-completed", language);
            p.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + completedText.get(0));
            p.sendMessage(ChatColor.YELLOW  + completedText.get(1));
            p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE,1f,1f);
        }

        if(questsManager.getPlayerCompletedWeeklyQuests(p) >= questsManager.getWeeklyQuests().size() && e.getQuest().getQuestType() == QuestType.WEEKLY){
            ArrayList<String> completedText = LanguageManager.getLangArrayLine("weekly-quest-page-completed", language);
            p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD  + completedText.get(0));
            p.sendMessage(ChatColor.GOLD + completedText.get(1));
            p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE,1f,1f);
        }

    }
}
