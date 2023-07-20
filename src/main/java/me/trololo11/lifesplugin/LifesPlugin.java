package me.trololo11.lifesplugin;

import me.trololo11.lifesplugin.commands.*;
import me.trololo11.lifesplugin.database.LanguageManager;
import me.trololo11.lifesplugin.database.LifesDatabase;
import me.trololo11.lifesplugin.database.QuestsTimings;
import me.trololo11.lifesplugin.database.TasksDatabase;
import me.trololo11.lifesplugin.listeners.*;
import me.trololo11.lifesplugin.listeners.lifeslisteners.*;
import me.trololo11.lifesplugin.listeners.questListeners.*;
import me.trololo11.lifesplugin.tabcompleters.MenuCommandTabCompleter;
import me.trololo11.lifesplugin.tabcompleters.SetLifesTabCompleter;
import me.trololo11.lifesplugin.tasks.SaveDataToDatabaseTask;
import me.trololo11.lifesplugin.utils.Menu;
import me.trololo11.lifesplugin.utils.Menus;
import me.trololo11.lifesplugin.utils.PlayerStats;
import me.trololo11.lifesplugin.utils.TeamsManager;
import me.trololo11.lifesplugin.utils.questUtils.QuestClass;
import me.trololo11.lifesplugin.utils.questUtils.TaskListenerArrays;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class LifesPlugin extends JavaPlugin {

    private HashMap<String, Integer> lifesNumbers = new HashMap<>();
    private ArrayList<OfflinePlayer> deadPlayers = new ArrayList<>();

    private HashMap<Player, PlayerStats> playerStats = new HashMap<>();
    private HashMap<Player, Language> playerLanguages = new HashMap<>();

    public Logger logger;

    private Properties globalDbProperties;

    private int dailyQuestNum,weeklyQuestNum;

    private String dailyPageText = "";
    private String weeklyPageText = "";
    private LifesDatabase lifesDatabase;
    private TasksDatabase tasksDatabase;
    private TaskListenerArrays listenerArrays;
    private QuestsTimings questsTimings;
    private QuestsManager questsManager;
    private RecipesManager recipesManager;

    /*
        Sooo this may seem like a lot and it is but it just setups the stuff I explained in different classes

        Contents:
        How the heck is the data stored what - DatabaseManager class
        How are the listeners for the task handled - KillMovListener class
        Database basics - LifesDatabase class
        Database confusing things in my plugin - TasksDatabase class
        Why the heck are the teams like that just why - TeamsManager class
        And how is everything setup - kinda this class yeah its for sure this class (LifesPlugin class)
     */

    @Override
    public void onEnable() {
        logger = Bukkit.getLogger();
        lifesDatabase = new LifesDatabase();
        tasksDatabase = new TasksDatabase();
        questsTimings = new QuestsTimings();
        recipesManager = new RecipesManager();
        questsManager = new QuestsManager(questsTimings, recipesManager);
        new Menus(questsManager, recipesManager);
        new LanguageManager();

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        globalDbProperties = new Properties();
        globalDbProperties.setProperty("minimumIdle", "1");
        globalDbProperties.setProperty("maximumPoolSize", "4");
        globalDbProperties.setProperty("initializationFailTimeout", "20000");


        recipesManager.initililazeRecipes();

        dailyQuestNum = getConfig().getInt("daily-quests-num");
        weeklyQuestNum = getConfig().getInt("weekly-quests-num");

        try {
            lifesDatabase.initalizeDatabase();
            tasksDatabase.initializeDatabase();
        } catch (SQLException | ClassNotFoundException e ) {
            try {
                lifesDatabase.initalizeDatabase();
                tasksDatabase.initializeDatabase();
            } catch (SQLException | ClassNotFoundException ex) {
                e.printStackTrace();

                logger.info("[LifesPluginS2] Couldn't connect to the database!");
                logger.info("[LifesPluginS2] Check the config file and see if all of the database informations are correct!");
                return;
            }
        }

        setupData();
        try {
            setDirs();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = new File(this.getDataFolder() + "/quests-data/all-daily");
        if(file.listFiles().length < dailyQuestNum) dailyQuestNum = file.listFiles().length;
        file = new File(this.getDataFolder() + "/quests-data/all-weekly");
        if(file.listFiles().length < weeklyQuestNum) weeklyQuestNum = file.listFiles().length;

        if(dailyQuestNum == 0){
            logger.info("[LifesPluginSeason2] Please create at least one daily quest to start this plugin!");
            logger.info("[LifesPluginSeason2] You can find more info in file questHelp.txt");
            return;
        }
        if(weeklyQuestNum == 0){
            logger.info("[LifesPluginSeason2] Please create at least one weekly quest to start this plugin!");
            logger.info("[LifesPluginSeason2] You can find more info in file questHelp.txt");
            return;
        }





        questsManager.setupActiveTasks();
        questsManager.setupUnactiveTasks();

        try {
            questsManager.checkPageQuestTimings();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

        try {
            setPlayerQuestsProgress();
        } catch (SQLException e) {
            System.out.println("Error while getting the players task data!");
        }

        try {
            questsManager.initializeAllFinishedQuests();
        } catch (SQLException e) {
            System.out.println("Error while calculating finsihhed quests!");
        }

        listenerArrays = new TaskListenerArrays(questsManager.getAllActiveQuests());


        TeamsManager teamsManager = new TeamsManager();
        teamsManager.registerEverything();

        new SaveDataToDatabaseTask(questsManager).runTaskTimer(this, 24000L, 24000L);

        getCommand("lifesmenu").setExecutor(new MainMenuCommand());
        getCommand("takelife").setExecutor(new TakeLifeCommand(recipesManager));
        getCommand("setlifes").setExecutor(new SetLifesCommand());
        getCommand("startlifes").setExecutor(new StartServerCommand() );

        getCommand("setlifes").setTabCompleter(new SetLifesTabCompleter());
        getCommand("lifesmenu").setTabCompleter(new MenuCommandTabCompleter());



        getServer().getPluginManager().registerEvents(new MenuHandlers(), this);
        getServer().getPluginManager().registerEvents(new DatabaseManager(questsManager), this);
        getServer().getPluginManager().registerEvents(new ChangeLifesListener(teamsManager), this);
        getServer().getPluginManager().registerEvents(new DeathListener(), this);
        getServer().getPluginManager().registerEvents(new ChatColorFix(teamsManager), this);
        getServer().getPluginManager().registerEvents(new QuestFinishListener(questsManager), this);
        getServer().getPluginManager().registerEvents(new AddLifeListener(), this);
        getServer().getPluginManager().registerEvents(new NonstackCraftingFix(recipesManager), this);
        getServer().getPluginManager().registerEvents(new ReviveUseListener(), this);
        getServer().getPluginManager().registerEvents(new CustomItemsCraftingFix(recipesManager), this);
        getServer().getPluginManager().registerEvents(new PlayerKillSomeoneListener(), this);
        getServer().getPluginManager().registerEvents(new AnvilRenameFix(), this);

        getServer().getPluginManager().registerEvents(new KillMobListener(), this);
        getServer().getPluginManager().registerEvents(new BreakBlockListener(), this);
        getServer().getPluginManager().registerEvents(new CraftItemListener(), this);
        getServer().getPluginManager().registerEvents(new EatItemListener(), this);
        getServer().getPluginManager().registerEvents(new MoveListeners(), this);
        getServer().getPluginManager().registerEvents(new TradeListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerGetItemListener(), this);
        getServer().getPluginManager().registerEvents(new ChangeHealthListener(), this);
        getServer().getPluginManager().registerEvents(new TameEntityListener(), this);
        getServer().getPluginManager().registerEvents(new PlaceBlockListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerLevelUpListener(), this);
        getServer().getPluginManager().registerEvents(new BreakItemListener(), this);
        getServer().getPluginManager().registerEvents(new BreakBlockNoSilkListener(), this);
        getServer().getPluginManager().registerEvents(new SuspiciousSandBreakListener(), this);
        getServer().getPluginManager().registerEvents(new BreedEntityListener(), this);
        getServer().getPluginManager().registerEvents(new SmithingUseListener(), this);
        getServer().getPluginManager().registerEvents(new InteractAtEntityListener(), this);
        getServer().getPluginManager().registerEvents(new VillagerCureListener(), this);
        getServer().getPluginManager().registerEvents(new PunchEntityListener(), this);
        getServer().getPluginManager().registerEvents(new UseItemListener(), this);
        getServer().getPluginManager().registerEvents(new SmeltItemListener(), this);
        getServer().getPluginManager().registerEvents(new TotemUseListener(), this);
        getServer().getPluginManager().registerEvents(new VillagerPayTradeListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerOnFireListener(), this);
        getServer().getPluginManager().registerEvents(new VillagerBuyTradeListener(), this);
        getServer().getPluginManager().registerEvents(new MobDropsListener(), this);

    }

    @Override
    public void onDisable(){

        for (QuestClass quest : questsManager.getAllActiveQuests()) {
            for(Player p : Bukkit.getOnlinePlayers()) {
                try {
                    tasksDatabase.setTaskProgress(p.getUniqueId().toString(), quest.getProgress(p), quest.getTableName(), quest.getQuestType());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                quest.removePluginSideProgress(p);
            }
        }

        lifesDatabase.closeDatabase();
        tasksDatabase.closeDatabase();

    }

    private void setDirs() throws IOException {


        File file = new File(this.getDataFolder() + "/quests-data");
        if (!file.exists()) file.mkdirs();

        file = new File(this.getDataFolder() + "/quests-data/all-daily");
        if (!file.exists()) file.mkdirs();

        file = new File(this.getDataFolder() + "/quests-data/all-weekly");
        if (!file.exists()) file.mkdirs();

        file = new File(this.getDataFolder() + "/quests-data/active-daily");
        if (!file.exists()) file.mkdirs();

        file = new File(this.getDataFolder() + "/quests-data/active-weekly");
        if (!file.exists()) file.mkdirs();

        file = new File(this.getDataFolder() + "/quests-data/language-files");
        if (!file.exists()) file.mkdirs();

        file = new File(this.getDataFolder() + "/quests-data/language-files/english.yml");
        if (!file.exists()) createTextFile(getResource("english.yml"), file);

        file = new File(this.getDataFolder() + "/quests-data/language-files/polish.yml");
        if (!file.exists()) createTextFile(getResource("polish.yml"), file);

        file = new File(this.getDataFolder() + "/quests-data/questsHelp.txt");
        if (!file.exists()) createTextFile(getResource("questsHelp.txt"), file);

        file = new File(this.getDataFolder() + "/quests-data/language-files/polish.yml");
        if (!file.exists()) createTextFile(getResource("polish.yml"), file);

        file = new File(this.getDataFolder() + "/quests-data/example-quest.yml");
        if (!file.exists()) createTextFile(getResource("example-quest.yml"), file);


    }

    private void createTextFile(InputStream is, File file) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8);
        int read = reader.read();
        while(read != -1){
            fileWriter.write((char) read);
            read = reader.read();
        }

        reader.close();
        fileWriter.close();
    }



    //converts an object from a string to a specific type of target

    //for more information DatabaseManager class
    private void setupData() {
        for (Player p : Bukkit.getOnlinePlayers()) {

            //it basically setups all the online players data

            try {
                int lifes = lifesDatabase.getPlayerLifes(p.getUniqueId().toString());
                if (lifes == -404) {
                    lifesDatabase.addPlayerToDatabase(p.getUniqueId().toString(), 3, false);

                    lifes = 3;
                }


                PlayerStats playerStats = lifesDatabase.getPlayerStats(p.getUniqueId().toString());
                if(playerStats == null){
                    lifesDatabase.addPlayerToStats(new PlayerStats(p, 0, 0, 0, 0, 0, 0, 0, 0));
                    playerStats = lifesDatabase.getPlayerStats(p.getUniqueId().toString());
                }

                setPlayerStats(p, playerStats);

                addPlayerLifes(p.getUniqueId().toString(), lifes);

                setPlayerLanguage(p, lifesDatabase.getPlayerLanguage(p.getUniqueId().toString()));

                deadPlayers = lifesDatabase.getAllDeadPlayers();
                questsManager.setupTakenAwardsForPlayer(p);



            } catch (SQLException e) {
                e.printStackTrace();
            }


        }
    }

    //for more information DatabaseManager class
    private void setPlayerQuestsProgress() throws SQLException {
        for (Player p : Bukkit.getOnlinePlayers()) {

            for (QuestClass quest : questsManager.getAllActiveQuests()) {

                quest.setPluginSideProgress(p, tasksDatabase.getTasksProgress(p.getUniqueId().toString(), quest.getTableName(), quest.getQuestType()));
            }


        }
    }

    public void restartListenerArrays(){
        listenerArrays = new TaskListenerArrays(questsManager.getAllQuests());
    }

    //setters and getters for data storages :p

    public int getLifes(String uuid) {
        return lifesNumbers.get(uuid);
    }

    public void addPlayerLifes(String uuid, int lifes) {
        lifesNumbers.put(uuid, lifes);
    }

    public void updateLifes(String uuid, int newLifes) throws SQLException {
        lifesDatabase.updatePlayerLifes(uuid, newLifes);
        lifesNumbers.replace(uuid, newLifes);
    }


    public void removeLifeStat(String uuid) {
        lifesNumbers.remove(uuid);
    }

    public ArrayList<OfflinePlayer> getDeadPlayers() {
        return deadPlayers;
    }

    public void removeDeadPlayer(OfflinePlayer player) {
        deadPlayers.remove(player);
    }

    public void addDeadPlayer(OfflinePlayer player) {
        deadPlayers.add(player);
    }

    public void setPlayerLanguage(Player player, Language language){
        try {
            lifesDatabase.updatePlayerLanguage(player.getUniqueId().toString(), language);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        playerLanguages.put(player, language);

    }

    public Language getPlayerLanguage(Player player){
        return playerLanguages.getOrDefault(player, Language.ENGLISH);
    }

    public void removePlayerLanguage(Player player){
        playerLanguages.remove(player);
    }

    public LifesDatabase getLifesDatabase() {
        return lifesDatabase;
    }

    public TasksDatabase getTasksDatabase() {
        return tasksDatabase;
    }

    public TaskListenerArrays getListenerArrays() {
        return listenerArrays;
    }

    public static LifesPlugin getPlugin() {
        return LifesPlugin.getPlugin(LifesPlugin.class);
    }

    public String getDailyPageText() {
        return dailyPageText;
    }

    public int getDailyQuestNum() {
        return dailyQuestNum;
    }

    public int getWeeklyQuestNum() {
        return weeklyQuestNum;
    }

    public Properties getGlobalDbProperties() {
        return globalDbProperties;
    }

    public PlayerStats getPlayerStats(Player player){
        return playerStats.get(player);
    }

    public void setPlayerStats(Player player, PlayerStats playerStatsClass){
        playerStats.put(player, playerStatsClass);
        try {
            lifesDatabase.updatePlayerStats(playerStatsClass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removePlayerStats(Player player){
        playerStats.remove(player);
    }


    public void setDailyPageText(String dailyPageText) {
        this.dailyPageText = dailyPageText;
        //soo if we want to live update text in the menu we need to go through all the players and check
        //if they are looking to a Menu menu (our menu class) and they we set menu item which resets
        //the items and we update the players inventory and ta da we did it lets go!
        for(Player p : Bukkit.getOnlinePlayers()){
            if(p.getOpenInventory().getTopInventory().getHolder() instanceof Menu menu){
                menu.setMenuItems(p);
                p.updateInventory();
            }
        }
    }

    public String getWeeklyPageText() {
        return weeklyPageText;
    }

    public void setWeeklyPageText(String weeklyPageText) {
        this.weeklyPageText = weeklyPageText;
        //bassicaly the same as the other one (litterary the same but for weekly page)
        for(Player p : Bukkit.getOnlinePlayers()){
            if(p.getOpenInventory().getTopInventory().getHolder() instanceof Menu menu){
                menu.setMenuItems(p);
                p.updateInventory();
            }
        }
    }
}
