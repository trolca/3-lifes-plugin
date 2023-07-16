package me.trololo11.lifesplugin.tasks;

import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.QuestsManager;
import me.trololo11.lifesplugin.utils.questTypes.QuestType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.Date;
import java.sql.SQLException;

public class ChangePageTimeTask extends BukkitRunnable {

    private LifesPlugin plugin = LifesPlugin.getPlugin();
    private QuestsManager questsManager;
    private final char whatChange;
    private final QuestType questType;
    private int startTime;
    private Date endDate;
    private String timerText;

    public ChangePageTimeTask(char whatChange, QuestType questType, QuestsManager questsManager, int startTime, Date endDate){
        this.whatChange = whatChange;
        this.questType = questType;
        this.startTime = startTime;
        this.questsManager = questsManager;
        this.endDate = endDate;
        updateText();
    }

    @Override
    public void run() {
        startTime -= 1;
        if(startTime >= 0) updateText();
        if(startTime < 0 || (startTime == 0 && whatChange != 'm')){
            try {
                //waits a second bcs of they small incarrucy of the calculations
                //(its hard to fix and like GLaDOS said 'the best solution to a problem is usually the easiest one')s
                Thread.sleep(1000);
                questsManager.checkPageQuestTimings();
            } catch (IOException | SQLException | InterruptedException e) {
                e.printStackTrace();
            }
            cancel();
        }
    }

    private void updateText(){
        //just updates the text
        if(whatChange == 'd') timerText = startTime + " d";
        if(whatChange == 'h') timerText =  "0 d "+startTime+" h";
        if(whatChange == 'm') timerText = (startTime+1) + " m";
        if(questType == QuestType.DAILY) plugin.setDailyPageText(timerText);
        else plugin.setWeeklyPageText(timerText);
    }
}
