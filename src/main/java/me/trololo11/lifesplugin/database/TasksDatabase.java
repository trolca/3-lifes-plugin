package me.trololo11.lifesplugin.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.utils.questTypes.QuestType;

import javax.naming.CommunicationException;
import java.sql.*;
import java.util.ArrayList;

public class TasksDatabase {

    private HikariDataSource weeklySource;
    private HikariDataSource dailySource;
    private HikariDataSource awardsSource;
    private LifesPlugin plugin = LifesPlugin.getPlugin();


    //both of them do the usual stuff to get the connection to the database
    //if you really want to know then class LifesDatabase i invite you :>

    public Connection getWeeklyConnection() throws SQLException {
        if (weeklySource != null) return weeklySource.getConnection();

        String host = plugin.getConfig().getString("host");
        String port = plugin.getConfig().getString("port");
        String url = "jdbc:mysql://"+host+":"+port;
        String user = plugin.getConfig().getString("user");
        String password = plugin.getConfig().getString("password");
        String databaseName = plugin.getConfig().getString("weekly-quests-database-name");
        if(databaseName == null || databaseName.isEmpty()){
            databaseName = "tasks_weekly";
        }

        Connection databaseCheck = DriverManager.getConnection(url, user,  password);


        Statement databaseStatement = databaseCheck.createStatement();
        databaseStatement.execute("CREATE DATABASE IF NOT EXISTS "+databaseName);
        databaseStatement.close();


        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(url + "/" + databaseName);
        config.setUsername(user);
        config.setPassword(password);
        config.setDataSourceProperties(plugin.getGlobalDbProperties());
        weeklySource = new HikariDataSource(config);

        databaseCheck.close();

        return weeklySource.getConnection();
    }

    public Connection getDailyConnection() throws SQLException {
        if (dailySource != null) return dailySource.getConnection();

        String host = plugin.getConfig().getString("host");
        String port = plugin.getConfig().getString("port");
        String url = "jdbc:mysql://"+host+":"+port;
        String user = plugin.getConfig().getString("user");
        String password = plugin.getConfig().getString("password");
        String databaseName = plugin.getConfig().getString("daily-quests-database-name");
        if(databaseName == null || databaseName.isBlank()){
            databaseName = "tasks_daily";
        }


        Connection databaseCheck = DriverManager.getConnection(url, user, password);

        Statement databaseStatement = databaseCheck.createStatement();
        databaseStatement.execute("CREATE DATABASE IF NOT EXISTS "+databaseName);
        databaseStatement.close();

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(url + "/" + databaseName);
        config.setUsername(user);
        config.setPassword(password);
        config.setDataSourceProperties(plugin.getGlobalDbProperties());
        dailySource = new HikariDataSource(config);

        databaseCheck.close();

        return dailySource.getConnection();
    }

    public void initializeDatabase() throws SQLException, ClassNotFoundException {
        getAwardsDataConnection();
        getDailyConnection();
        getWeeklyConnection();
    }

    public Connection getAwardsDataConnection() throws SQLException {
        if (awardsSource != null) return awardsSource.getConnection();

        String host = plugin.getConfig().getString("host");
        String port = plugin.getConfig().getString("port");
        String url = "jdbc:mysql://"+host+":"+port;
        String user = plugin.getConfig().getString("user");
        String password = plugin.getConfig().getString("password");
        String databaseName = plugin.getConfig().getString("quests-awards-database-name");
        if(databaseName == null || databaseName.isBlank()){
            databaseName = "tasks_awards_data";
        }


        Connection databaseCheck = DriverManager.getConnection(url, user, password);

        Statement databaseStatement = databaseCheck.createStatement();
        databaseStatement.execute("CREATE DATABASE IF NOT EXISTS "+databaseName);
        databaseStatement.close();

        databaseCheck.close();

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(url + "/" + databaseName);
        config.setUsername(user);
        config.setPassword(password);
        config.setDataSourceProperties(plugin.getGlobalDbProperties());
        awardsSource = new HikariDataSource(config);

        Connection connection = awardsSource.getConnection();
        Statement createTableStatement = connection.createStatement();
        createTableStatement.execute("CREATE TABLE IF NOT EXISTS awards_data(uuid varchar(36) primary key, hasTakenDaily boolean, hasTakenWeekly boolean)");
        createTableStatement.close();
        connection.close();

        return awardsSource.getConnection();
    }



    //creates a new table for a task to store the players progress for this task because if we had only 1 table it would be hard to change number of tasks
    public void createTaskTable(String name, String taskType) throws SQLException {
        Connection connection = taskType.equalsIgnoreCase("daily") ? getDailyConnection() : getWeeklyConnection();

        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS " + name + "(uuid varchar(36) primary key, progress int)");
        statement.close();
        connection.close();
    }

    //we can remove the table if the task is no longer used
    public void removeTaskTable(String name, String taskType) throws SQLException {
        Connection connection = taskType.equalsIgnoreCase("daily") ? getDailyConnection() : getWeeklyConnection();

        Statement statement = connection.createStatement();
        statement.execute("DROP TABLE " + name);
        statement.close();
        connection.close();
    }

    /**
     * Closes all of the DataSources in the database
     */
    public void closeDatabase(){
        awardsSource.close();
        dailySource.close();
        weeklySource.close();
    }




    public int getTasksProgress(String uuid, String taskName, QuestType questType) throws SQLException {
        Connection connection = questType == QuestType.DAILY ? getDailyConnection() : getWeeklyConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + taskName + " WHERE uuid = ?");

        statement.setString(1, uuid);

        ResultSet results = statement.executeQuery();

        if (results.next()) {
            int progress = results.getInt("progress");
            statement.close();
            connection.close();
            return progress;
        }
        //TODO Check if this causes the problem with task progress :>
        String sql = "INSERT INTO " + taskName + "(uuid, progress) VALUES (?, ?)";

        PreparedStatement createStatement = connection.prepareStatement(sql);
        createStatement.setString(1, uuid);
        createStatement.setInt(2, 0);

        createStatement.executeUpdate();
        statement.close();
        createStatement.close();
        connection.close();

        return 0;
    }

    public void setTaskProgress(String uuid, int newProgress, String taskName, QuestType questType) throws SQLException {
        String sql = "UPDATE " + taskName + " SET progress = ? WHERE uuid = ?";
        Connection connection = questType == QuestType.DAILY ? getDailyConnection() : getWeeklyConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, newProgress);
        statement.setString(2, uuid);

        statement.executeUpdate();
        statement.close();
        connection.close();

    }

    private void setupPlayerAwards(String uuid) throws SQLException {
        String sql = "INSERT INTO awards_data(uuid, hasTakenDaily, hasTakenWeekly) VALUES (?, ?, ?)";
        Connection connection = getAwardsDataConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, uuid);
        statement.setBoolean(2, false);
        statement.setBoolean(3, false);
        statement.executeUpdate();
        statement.close();
        connection.close();
    }

    public void setTakenAwards(String uuid, boolean hasTakenDaily, boolean hasTakenWeekly) throws SQLException {

        String sql = "UPDATE awards_data SET hasTakenDaily = ?, hasTakenWeekly = ? WHERE uuid = ?";
        Connection connection = getAwardsDataConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setBoolean(1, hasTakenDaily);
        statement.setBoolean(2, hasTakenWeekly);
        statement.setString(3, uuid);

        statement.executeUpdate();
        statement.close();
        connection.close();

    }

    public void removeAllTakenDailyAwards() throws SQLException {
        String sql = "UPDATE awards_data SET hasTakenDaily = false";
        Connection connection = getAwardsDataConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.executeUpdate();
        statement.close();
        connection.close();

    }

    public void removeAllTakenWeeklyAwards() throws SQLException {
        String sql = "UPDATE awards_data SET hasTakenWeekly = false";
        Connection connection = getAwardsDataConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.executeUpdate();
        statement.close();
        connection.close();

    }

    public ArrayList<Boolean> getTakenAwards(String uuid) throws SQLException {

        String sql = "SELECT * FROM awards_data WHERE uuid = ?";
        Connection connection = getAwardsDataConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, uuid);

        ResultSet results = statement.executeQuery();

        if(results.next()){
            ArrayList<Boolean> takenAwards = new ArrayList<>();

            takenAwards.add(results.getBoolean("hasTakenDaily"));
            takenAwards.add(results.getBoolean("hasTakenWeekly"));
            statement.close();
            connection.close();
            return takenAwards;
        }

        setupPlayerAwards(uuid);

        results = statement.executeQuery();

        if(results.next()){
            ArrayList<Boolean> takenAwards = new ArrayList<>();

            takenAwards.add(results.getBoolean("hasTakenDaily"));
            takenAwards.add(results.getBoolean("hasTakenWeekly"));
            statement.close();
            connection.close();
            return takenAwards;
        }


        statement.close();
        connection.close();

        return null;


    }


}
