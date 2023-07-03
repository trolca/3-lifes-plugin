package me.trololo11.lifesplugin;

import me.trololo11.lifesplugin.database.LanguageManager;
import me.trololo11.lifesplugin.database.QuestsTimings;
import me.trololo11.lifesplugin.database.TasksDatabase;
import me.trololo11.lifesplugin.tasks.ChangePageTimeTask;
import me.trololo11.lifesplugin.utils.questTypes.TaskListenerType;
import me.trololo11.lifesplugin.utils.questTypes.QuestType;
import me.trololo11.lifesplugin.utils.questUtils.QuestClass;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class QuestsManager {

    private LifesPlugin plugin = LifesPlugin.getPlugin();
    private QuestsTimings questsTimings;
    private TasksDatabase tasksDatabase = plugin.getTasksDatabase();
    private HashMap<Player, Integer> playerCompletedDailyQuests = new HashMap<>();
    private HashMap<Player, Integer> playerCompletedWeeklyQuests = new HashMap<>();
    private HashMap<Player, Boolean> hasTakenDaily = new HashMap<>();
    private HashMap<Player, Boolean> hasTakenWeekly = new HashMap<>();
    private boolean isDailyTaskRunning=false,isWeeklyTaskRunning=false;

    private ArrayList<QuestClass> dailyQuests = new ArrayList<>();
    private ArrayList<QuestClass> weeklyQuests = new ArrayList<>();
    private ArrayList<QuestClass> allQuests = new ArrayList<>();
    private ArrayList<QuestClass> allActiveQuests = new ArrayList<>();

    /*
        Hiiiii its me again and Im gonna explain how are the tasks read and how to they work (its not about databases and stuff you can find that elsewhere aka KillMobListener and TasksDatabase class)

        Sooo in the main data folder of this plugin you can find 2 folders and as you can imagine they are for storing custom tasks that you can make
        You have the daily folder where you put all of the daily tasks you want
        And you have the weekly folder where you put all of the weekly tasks you want

        You have to create a .yml file with the specifed attributes:
        task-name - the name of the task (String)
        description - description of the task (ArrayList<String>)
        database-name - the name of the table that will be created in the mySQL database (String)
        icon - the item that is gonna be the icon of that task (Material)
        max-progres - because every progress if stored with an int you specify the number to finish this task (int)
        listener-type - listener type which is from TaskListenerType remember all caps (String but also enum TaskListenerType)
        target - its an object which is converted to a specific target you need to do to finish any given task (for example if you had KILL_MOBS listener type you would need to type the entity itd)

        and all of these information get converted to a new QuestClass :D

     */

    public QuestsManager(QuestsTimings questsTimings){
        this.questsTimings = questsTimings;
    }

    //counts all of the players quests that they finished
    public void initializeAllFinishedQuests() throws SQLException {
        for(Player player : Bukkit.getOnlinePlayers()){
            int finishedWeekly=0;
            int finishedDaily=0;

            //goes through all fo the quests and adds one if the player has finished it
            for(QuestClass quest : allActiveQuests){
                if(quest.hasCompleted(player) && quest.getQuestType() == QuestType.DAILY) finishedDaily++;
                else if(quest.hasCompleted(player)) finishedWeekly++;
            }

            playerCompletedDailyQuests.put(player, finishedDaily);
            playerCompletedWeeklyQuests.put(player, finishedWeekly);

        }
    }

    //this function is for handling the players that already joined and it does almost the same thing as the initalization
    public void calculatePlayersFinishedQuests(Player player) throws SQLException {

        int finishedWeekly=0;
        int finishedDaily=0;

        //calculates all of players finished quest
        for(QuestClass quest : allActiveQuests){

            if(quest.hasCompleted(player) && quest.getQuestType() == QuestType.DAILY) finishedDaily++;
            else if(quest.hasCompleted(player)) finishedWeekly++;
        }

        playerCompletedDailyQuests.put(player, finishedDaily);
        playerCompletedWeeklyQuests.put(player, finishedWeekly);

    }

    //checks the date for the daily and weekly quests to test if they are after today or they are recalulating the timer
    public void checkPageQuestTimings() throws IOException, SQLException {
        Date dailyDate = questsTimings.getDailyEndDate();
        Date weeklyDate = questsTimings.getWeeklyEndDate();

        checkDate(dailyDate, 86400000-1, QuestType.DAILY);
        checkDate(weeklyDate, 604800000, QuestType.WEEKLY);
    }

    /*
    soo this is a kida hard function so im gonna explain it as best as I can lol
    in the plugin data folder we have a file which stores the date when weekly and daily pages of quests have to end
    so this function calculates the timer logic for the pages and checks if its after today
    the function above is responcible (thats not how you spell is it) for calling it
    (its explained im more detail in the function)

     */
    private void checkDate(Date date, long newTime, QuestType questType) throws IOException, SQLException {
        Date todayDate = new Date();
        if (date.after(todayDate)) {

            long timeLeftMils = date.getTime() - todayDate.getTime();

            //caculates the days (or hours) left by dividing the substraction of today and the date and dividng it by days or hours (written in milliseconds)
            int dateDays = (int) Math.floor((date.getTime() - todayDate.getTime()) / 86400000.0);
            int dateHours = (int) Math.floor((date.getTime() - todayDate.getTime()) / 3600000.0);
            int dateMinutes = (int) Math.floor((date.getTime() - todayDate.getTime()) / 60000.0);
            ChangePageTimeTask changePageTask;


            if (dateDays > 1 ) {

                //if the days are above 1 then start the timer

                //and this is basicly setting up the timerso that the days count down in real time (ypu can see i was tired while writing this lol)
                changePageTask = new ChangePageTimeTask('d', questType, this, dateDays, date);
                long startDelay = ((long) Math.ceil(timeLeftMils / 1000f) - ((dateDays) * 86400L)) * 20;


                changePageTask.runTaskTimer(plugin, startDelay, 1728000L);

            } else if(dateHours >= 1) {

                //this is basicly the same as the up one but counts hours
                changePageTask = new ChangePageTimeTask('h', questType,this, dateHours, date);
                long startDelay = (((long) Math.ceil(timeLeftMils / 1000f) - ((dateHours) * 3600L)) * 20)+60;




                changePageTask.runTaskTimer(plugin, startDelay, 72000L);
            }else{

                changePageTask = new ChangePageTimeTask('m', questType, this, dateMinutes, date);
                long startDelay = (((long) Math.ceil(timeLeftMils / 1000f) - ((dateMinutes) * 60L)) * 20)+60;


                changePageTask.runTaskTimer(plugin, startDelay, 1200L);
            }

            if(questType == QuestType.DAILY) isDailyTaskRunning = true;
            else isWeeklyTaskRunning = true;


        }else{
            if(questType == QuestType.DAILY) isDailyTaskRunning = false;
            else isWeeklyTaskRunning = false;

            createNewDate(questType, newTime);
        }
    }


    //this function is responcible for generating a new date for the weekly or daily quest page
    // it also randomises the quests for the new quests
    private void createNewDate(QuestType questType, long newTime) throws IOException, SQLException {
        Date todayDate = new Date();
        Date newDate = new Date(todayDate.getTime()+newTime);
        Random r = new Random();
        File path;
        File pathActive;

        //depending on the type it chooses specific folders
        path = new File(plugin.getDataFolder() + "/quests-data/"+ (questType == QuestType.DAILY ? "all-daily" : "all-weekly"));
        pathActive = new File(plugin.getDataFolder() + "/quests-data/"+ (questType == QuestType.DAILY ? "active-daily" : "active-weekly"));

        File[] fileQuests = path.listFiles();

        //sets a date when the quest page will reset again
        if(questType == QuestType.DAILY) questsTimings.setDailyEndDate(newDate);
        else questsTimings.setWeeklyEndDate(newDate);

        //it resets the taken awards (shards) so player can take them again
        for(Player player : Bukkit.getOnlinePlayers()){
            if(questType == QuestType.DAILY) setPlayerHasTakenDaily(player, false);
            else setPlayerHasTakenWeekly(player, false);
        }

        if(fileQuests == null) return;
        //delets all of the tables in the database to replace by the new ones
        destoryDatabaseQuestsTables(questType);

        //gets how namy quest are there in total
        int howManyQuests = questType == QuestType.DAILY ? plugin.getDailyQuestNum() : plugin.getWeeklyQuestNum();

        //puts for every player online that they have finished 0 quests from every page bcs they are getting reset lol
        for(Player player : Bukkit.getOnlinePlayers()){
            if(questType == QuestType.DAILY) playerCompletedDailyQuests.put(player, 0);
            else playerCompletedWeeklyQuests.put(player ,0);
        }

        if(questType == QuestType.DAILY) tasksDatabase.removeAllTakenDailyAwards();
        else tasksDatabase.removeAllTakenWeeklyAwards();

        //sooo this block randomises the quests that will be on the new page but
        //they cannot be the same so i must create a list that stores their names
        // (bcs i cannot check if it contains the class bcs every class is different)
        //and if they list contains the name of the quest it repets until there are not
        ArrayList<String> questsNames = new ArrayList<>();
        for(int i=0; i < howManyQuests ; i++){

            //gets a random file from the quest-data/all-daily (or /all-weekly)
            File file = fileQuests[r.nextInt(fileQuests.length)];
            FileConfiguration questConfig = YamlConfiguration.loadConfiguration(file);
            //gets the name of it
            String name = questConfig.getString("database-name");

            //if the temporlaly list of all the name of quests that have been chosen contains
            //a name it gets a new quest
            while(questsNames.contains(name)){
                file = fileQuests[r.nextInt(fileQuests.length)];
                questConfig = YamlConfiguration.loadConfiguration(file);
                name = questConfig.getString("database-name");
            }

            //just copies the quest file to to the quest-data/active-daily (or active-weekly)
            Files.copy(file.toPath(), Path.of(pathActive.toPath()+"/"+file.getName()), StandardCopyOption.REPLACE_EXISTING);
            QuestClass quest = createQuest(questConfig, questType,true);

            //adds quest to the list which stores all of the dailt or weekly quests
            if(questType == QuestType.DAILY) dailyQuests.add(quest);
            else weeklyQuests.add(quest);
            allActiveQuests.add(quest);

            //if all of that has been completed successfully add the name of the quest to the temporlaly array

            questsNames.add(questConfig.getString("database-name"));


        }
        checkDate(newDate, newTime, questType);
    }



    //goes through every quests thats active and adds it to the list
    public void setupActiveTasks() {
        File weeklyQuestsDir = new File(plugin.getDataFolder() + "/quests-data/active-weekly");
        File dailyQuestsDir = new File(plugin.getDataFolder() + "/quests-data/active-daily");

        if (weeklyQuestsDir.listFiles() != null) { //converts
            for (File quest : weeklyQuestsDir.listFiles()) {

                FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(quest);

                QuestClass questClass = createQuest(fileConfig, QuestType.WEEKLY, true);

                weeklyQuests.add(questClass);
                allActiveQuests.add(questClass);

            }
        }

        if (dailyQuestsDir.listFiles() != null) { //still converts
            for (File quest : dailyQuestsDir.listFiles()) {

                FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(quest);

                QuestClass questClass = createQuest(fileConfig, QuestType.DAILY,true);

                dailyQuests.add(questClass);
                allActiveQuests.add(questClass);

            }
        }
    }

    //goes through all the unactive quests (so bassicaly all of them) and adds to the list which stores all the quests
    public void setupUnactiveTasks() {
        File weeklyQuestsDir = new File(plugin.getDataFolder() + "/quests-data/all-weekly");
        File dailyQuestsDir = new File(plugin.getDataFolder() + "/quests-data/all-daily");

        if (weeklyQuestsDir.listFiles() != null) { //converts
            for (File quest : weeklyQuestsDir.listFiles()) {

                FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(quest);

                QuestClass questClass = createQuest(fileConfig, QuestType.WEEKLY, false);


                allQuests.add(questClass);


            }
        }

        if (dailyQuestsDir.listFiles() != null) { //still converts
            for (File quest : dailyQuestsDir.listFiles()) {

                FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(quest);

                QuestClass questClass = createQuest(fileConfig, QuestType.DAILY, false);

                allQuests.add(questClass);
            }
        }
    }

    //soo to now waste space it delets all of the tables of the quests that no loger be in use (its scary but noys)
    public void destoryDatabaseQuestsTables(QuestType questType) throws SQLException {

        File activePath = new File(plugin.getDataFolder() + (questType == QuestType.DAILY ? "/quests-data/active-daily" : "/quests-data/active-weekly"));
        for(QuestClass questClass : questType == QuestType.DAILY ? dailyQuests : weeklyQuests){
            plugin.getTasksDatabase().removeTaskTable(questClass.getTableName(), questClass.getQuestType().toString());
            allActiveQuests.remove(questClass);
        }

        if(activePath.listFiles() != null) {
            for (File file : activePath.listFiles()) {
                file.delete();
            }
        }

        if(questType == QuestType.DAILY) dailyQuests.clear();
        else weeklyQuests.clear();

    }

    //gets the file config of the quest and creates a new QuestClass based on it
    private QuestClass createQuest(FileConfiguration fileConfig, QuestType questType, boolean isActive){


        String databaseName = fileConfig.getString("database-name");
        Material icon = Material.BARRIER;
        try {
            icon = Material.valueOf(fileConfig.getString("icon").toUpperCase());
        }catch (IllegalArgumentException e){
            System.out.println("[LifesPluginS2] Error while trying to read the icon for quest "+ databaseName);
            System.out.println("[LifesPluginS2] More information: ");
            e.printStackTrace();
        }

        boolean showProgressBool = fileConfig.getBoolean("show-progress");
        QuestType taskType = questType;
        int maxProgress = fileConfig.getInt("max-progress");
        TaskListenerType listenerType = null;
        try {
            listenerType = TaskListenerType.valueOf(fileConfig.getString("listener-type").toUpperCase());
        }catch(IllegalArgumentException e){
            System.out.println("[LifesPluginS2] Error while reading the listener type for quest: "+ databaseName);
            System.out.println("[LifesPluginS2] Check the file for possible errors");
        }

        String stringTarget = fileConfig.getString("target");
        Object target = getTargetType(stringTarget == null ? null : stringTarget.toUpperCase(), listenerType, fileConfig);



        return new QuestClass(LanguageManager.getQuestNames(fileConfig),LanguageManager.getQuestDescription(fileConfig), databaseName, icon, showProgressBool, taskType, maxProgress, listenerType, target, isActive);
    }




    //Soo it just converts the target to a specific type so it isn't always a string
    private Object getTargetType(String targetType, TaskListenerType type, FileConfiguration fileConfig) {

        if(targetType == null) return null;
        switch (type) {
            case KILL_MOBS,RIDE_DISTANCE,TAME_ANIMAL, BREED_ENTITY, RIGHT_CLICK_ENTITY, PUNCH_ENTITY -> {
                return EntityType.valueOf(targetType);
            }
            case BREAK_BLOCKS, CRAFT, EAT, GET_ITEM, PLACE_BLOCKS, BREAK_ITEM, BREAK_BLOCKS_NO_SILK, SMITHING_USE, USE_ITEM, SMELT_ITEM, VILLAGER_TRADE_SPEND, VILLAGER_TRADE_BUY, GET_ITEM_BY_MOB   -> {
                return Material.valueOf(targetType);
            }
            case PLAYER_HEART -> {
                return Integer.parseInt(targetType);
            }
            case VILLAGER_TRADE -> {
                return EnchantmentWrapper.getByKey(NamespacedKey.minecraft(fileConfig.getString("target")));
            }
            case WALK_DISTANCE, FLY_DISTANCE -> {
                return fileConfig.getInt("target");
            }

        }

        return targetType;

    }

    //getters and setters but with a twist

    public boolean hasFinishedAllDailyQuests(Player player){
        return playerCompletedDailyQuests.getOrDefault(player, 0) >= dailyQuests.size();
    }

    public boolean hasFinishedAllWeeklyQuests(Player player){
        return playerCompletedWeeklyQuests.getOrDefault(player, 0) >= weeklyQuests.size();
    }


    public ArrayList<QuestClass> getDailyQuests() {
        return dailyQuests;
    }

    public ArrayList<QuestClass> getWeeklyQuests() {
        return weeklyQuests;
    }

    public ArrayList<QuestClass> getAllQuests() {
        return allQuests;
    }

    public ArrayList<QuestClass> getAllActiveQuests() {
        return allActiveQuests;
    }

    public int getPlayerCompletedDailyQuests(Player player) {
        return playerCompletedDailyQuests.getOrDefault(player, 0);
    }


    public void addPlayerCompletedDailyQuest(Player player) throws SQLException {
        //if the player doesnt have the counter (for some reason) it makes a new one
        if(playerCompletedDailyQuests.get(player) == null) calculatePlayersFinishedQuests(player);
        //adds 1 to the complteded daily quests
        int currProgress = playerCompletedDailyQuests.getOrDefault(player, 0);
        playerCompletedDailyQuests.put(player, currProgress+1);

    }

    public int getPlayerCompletedWeeklyQuests(Player player) {
        return playerCompletedWeeklyQuests.getOrDefault(player, 0);
    }

    public void addPlayerCompletedWeeklyQuest(Player player) throws SQLException {
        //if the player doesnt have the counter (for some reason) it makes a new one
        if(playerCompletedWeeklyQuests.get(player) == null) calculatePlayersFinishedQuests(player);
        //adds 1 to the complteded weekly quests
        int currProgress = playerCompletedWeeklyQuests.getOrDefault(player, 0);
        playerCompletedWeeklyQuests.put(player, currProgress+1);
    }


    //the rest if self explanatory i hope

    public void removePlayerFromCompletedQuests(Player player){
        playerCompletedDailyQuests.remove(player);
        playerCompletedWeeklyQuests.remove(player);
    }

    public void setPlayerHasTakenDaily(Player player, boolean hasTaken){
        try {
            tasksDatabase.setTakenAwards(player.getUniqueId().toString(), hasTaken, hasTakenWeekly.getOrDefault(player, false));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        hasTakenDaily.put(player, hasTaken);
    }

    public void setPlayerHasTakenWeekly(Player player, boolean hasTaken){
        try {
            tasksDatabase.setTakenAwards(player.getUniqueId().toString(), hasTakenDaily.getOrDefault(player, false), hasTaken);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        hasTakenWeekly.put(player, hasTaken);
    }

    public void setupTakenAwardsForPlayer(Player player) throws SQLException {
        ArrayList<Boolean> takenAwards = tasksDatabase.getTakenAwards(player.getUniqueId().toString());
        hasTakenDaily.put(player, takenAwards.get(0));
        hasTakenWeekly.put(player, takenAwards.get(1));
    }

    public boolean getHasTakenDaily(Player player){
       return hasTakenDaily.getOrDefault(player, false);
    }

    public boolean getHasTakenWeekly(Player player){
        return hasTakenWeekly.getOrDefault(player, false);
    }

    public void removePlayerFromTakenHashMaps(Player player){
        hasTakenDaily.remove(player);
        hasTakenWeekly.remove(player);
    }








}
