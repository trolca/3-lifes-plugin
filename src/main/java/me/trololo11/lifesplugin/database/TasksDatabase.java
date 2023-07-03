package me.trololo11.lifesplugin.database;

import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.utils.questTypes.QuestType;

import javax.naming.CommunicationException;
import java.sql.*;
import java.util.ArrayList;

public class TasksDatabase {

    private Connection weeklyConnection;
    private Connection dailyConnection;
    private Connection awardsConnection;
    private LifesPlugin plugin = LifesPlugin.getPlugin();


    //both of them do the usual stuff to get the connection to the database
    //if you really want to know then class LifesDatabase i invite you :>

    public Connection getWeeklyConnection() throws SQLException {
        if (weeklyConnection != null) return weeklyConnection;

        String host = plugin.getConfig().getString("host");
        String port = plugin.getConfig().getString("port");
        String url = "jdbc:mysql://"+host+":"+port;
        String user = plugin.getConfig().getString("user");
        String password = plugin.getConfig().getString("password");
        String databaseName = plugin.getConfig().getString("weekly-quests-database-name");
        if(databaseName == null || databaseName.isEmpty()){
            databaseName = "tasks_weekly";
        }
        try {
            Class.forName( "com.mysql.jdbc.Driver" );
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


        Connection databaseCheck = DriverManager.getConnection(url, user,  password);


        Statement databaseStatement = databaseCheck.createStatement();
        System.out.println("sususus");
        databaseStatement.execute("CREATE DATABASE IF NOT EXISTS "+databaseName);
        databaseStatement.close();

        databaseCheck.close();

        weeklyConnection = DriverManager.getConnection(url + "/"+databaseName, user, password);

        return weeklyConnection;
    }

    public Connection getDailyConnection() throws SQLException {
        if (dailyConnection != null) return dailyConnection;

        String host = plugin.getConfig().getString("host");
        String port = plugin.getConfig().getString("port");
        String url = "jdbc:mysql://"+host+":"+port;
        String user = plugin.getConfig().getString("user");
        String password = plugin.getConfig().getString("password");
        String databaseName = plugin.getConfig().getString("daily-quests-database-name");
        if(databaseName == null || databaseName.isBlank()){
            databaseName = "tasks_daily";
        }
        try {
            Class.forName( "com.mysql.jdbc.Driver" );
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Connection databaseCheck = DriverManager.getConnection(url, user, password);

        Statement databaseStatement = databaseCheck.createStatement();
        databaseStatement.execute("CREATE DATABASE IF NOT EXISTS "+databaseName);
        databaseStatement.close();

        databaseCheck.close();

        dailyConnection = DriverManager.getConnection(url + "/"+databaseName, user, password);

        return dailyConnection;
    }

    public void initializeDatabase() throws SQLException, ClassNotFoundException {
        getAwardsDataConnection();
        getDailyConnection();
        getWeeklyConnection();
    }

    public Connection getAwardsDataConnection() throws SQLException {
        if (awardsConnection != null) return awardsConnection;

        String host = plugin.getConfig().getString("host");
        String port = plugin.getConfig().getString("port");
        String url = "jdbc:mysql://"+host+":"+port;
        String user = plugin.getConfig().getString("user");
        String password = plugin.getConfig().getString("password");
        String databaseName = plugin.getConfig().getString("quests-awards-database-name");
        if(databaseName == null || databaseName.isBlank()){
            databaseName = "tasks_awards_data";
        }

        try {
            Class.forName( "com.mysql.jdbc.Driver" );
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Connection databaseCheck = DriverManager.getConnection(url, user, password);

        Statement databaseStatement = databaseCheck.createStatement();
        databaseStatement.execute("CREATE DATABASE IF NOT EXISTS "+databaseName);
        databaseStatement.close();

        databaseCheck.close();

        awardsConnection = DriverManager.getConnection(url + "/"+databaseName, user, password);

        Statement createTableStatement = awardsConnection.createStatement();
        createTableStatement.execute("CREATE TABLE IF NOT EXISTS awards_data(uuid varchar(36) primary key, hasTakenDaily boolean, hasTakenWeekly boolean)");
        createTableStatement.close();

        return awardsConnection;
    }



    //creates a new table for a task to store the players progress for this task because if we had only 1 table it would be hard to change number of tasks
    public void createTaskTable(String name, String taskType) throws SQLException {
        Statement statement = taskType.equalsIgnoreCase("daily") ? getDailyConnection().createStatement() : getWeeklyConnection().createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS " + name + "(uuid varchar(36) primary key, progress int)");
        statement.close();
    }

    //we can remove the table if the task is no longer used
    public void removeTaskTable(String name, String taskType) throws SQLException {
        Statement statement = taskType.equalsIgnoreCase("daily") ? getDailyConnection().createStatement() : getWeeklyConnection().createStatement();
        statement.execute("DROP TABLE " + name);
        statement.close();
    }



    public int getTasksProgress(String uuid, String taskName, QuestType questType) throws SQLException {
        PreparedStatement statement = questType == QuestType.DAILY ? getDailyConnection().prepareStatement("SELECT * FROM " + taskName + " WHERE uuid = ?") : getWeeklyConnection().prepareStatement("SELECT * FROM " + taskName + " WHERE uuid = ?");

        statement.setString(1, uuid);

        ResultSet results = statement.executeQuery();

        if (results.next()) {
            return results.getInt("progress");
        }
        //TODO Check if this causes the problem with task progress :>
        String sql = "INSERT INTO " + taskName + "(uuid, progress) VALUES (?, ?)";

        PreparedStatement createStatement = questType == QuestType.DAILY ? getDailyConnection().prepareStatement(sql) : getWeeklyConnection().prepareStatement(sql);
        createStatement.setString(1, uuid);
        createStatement.setInt(2, 0);

        createStatement.executeUpdate();
        statement.close();
        createStatement.close();


        return 0;
    }

    public void setTaskProgress(String uuid, int newProgress, String taskName, QuestType questType) throws SQLException {
        String sql = "UPDATE " + taskName + " SET progress = ? WHERE uuid = ?";
        PreparedStatement statement = questType == QuestType.DAILY ? getDailyConnection().prepareStatement(sql) : getWeeklyConnection().prepareStatement(sql);

        statement.setInt(1, newProgress);
        statement.setString(2, uuid);

        statement.executeUpdate();
        statement.close();

    }

    private void setupPlayerAwards(String uuid) throws SQLException {
        String sql = "INSERT INTO awards_data(uuid, hasTakenDaily, hasTakenWeekly) VALUES (?, ?, ?)";
        PreparedStatement statement = getAwardsDataConnection().prepareStatement(sql);

        statement.setString(1, uuid);
        statement.setBoolean(2, false);
        statement.setBoolean(3, false);
        statement.executeUpdate();
        statement.close();
    }

    public void setTakenAwards(String uuid, boolean hasTakenDaily, boolean hasTakenWeekly) throws SQLException {

        String sql = "UPDATE awards_data SET hasTakenDaily = ?, hasTakenWeekly = ? WHERE uuid = ?";

        PreparedStatement statement = getAwardsDataConnection().prepareStatement(sql);

        statement.setBoolean(1, hasTakenDaily);
        statement.setBoolean(2, hasTakenWeekly);
        statement.setString(3, uuid);

        statement.executeUpdate();
        statement.close();

    }

    public void removeAllTakenDailyAwards() throws SQLException {
        String sql = "UPDATE awards_data SET hasTakenDaily = false";

        PreparedStatement statement = getAwardsDataConnection().prepareStatement(sql);

        statement.executeUpdate();
        statement.close();

    }

    public void removeAllTakenWeeklyAwards() throws SQLException {
        String sql = "UPDATE awards_data SET hasTakenWeekly = false";

        PreparedStatement statement = getAwardsDataConnection().prepareStatement(sql);

        statement.executeUpdate();
        statement.close();

    }

    public ArrayList<Boolean> getTakenAwards(String uuid) throws SQLException {

        String sql = "SELECT * FROM awards_data WHERE uuid = ?";
        PreparedStatement statement = getAwardsDataConnection().prepareStatement(sql);

        statement.setString(1, uuid);

        ResultSet results = statement.executeQuery();

        if(results.next()){
            ArrayList<Boolean> takenAwards = new ArrayList<>();

            takenAwards.add(results.getBoolean("hasTakenDaily"));
            takenAwards.add(results.getBoolean("hasTakenWeekly"));
            statement.close();
            return takenAwards;
        }

        setupPlayerAwards(uuid);

        results = statement.executeQuery();

        if(results.next()){
            ArrayList<Boolean> takenAwards = new ArrayList<>();

            takenAwards.add(results.getBoolean("hasTakenDaily"));
            takenAwards.add(results.getBoolean("hasTakenWeekly"));
            statement.close();
            return takenAwards;
        }


        statement.close();

        return null;


    }


}
