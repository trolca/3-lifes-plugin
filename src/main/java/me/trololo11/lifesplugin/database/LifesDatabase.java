package me.trololo11.lifesplugin.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.trololo11.lifesplugin.Language;
import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.utils.PlayerStats;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class LifesDatabase {


    private LifesPlugin plugin = LifesPlugin.getPlugin();
    private HikariDataSource ds;

    public Connection getConnection() throws SQLException {
        if (ds != null) return ds.getConnection();

        String host = plugin.getConfig().getString("host");
        String port = plugin.getConfig().getString("port");
        String url = "jdbc:mysql://"+host+":"+port;
        String user = plugin.getConfig().getString("user");
        String password = plugin.getConfig().getString("password");
        String databaseName = plugin.getConfig().getString("lifes-database-name");
        if(databaseName == null || databaseName.isEmpty()){
            databaseName = "lifes_data";
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
        ds = new HikariDataSource(config);

        databaseCheck.close();


        return ds.getConnection();

    }

    /**
     * Closes all of the DataSources in the database
     */
    public void closeDatabase(){
        ds.close();
    }

    public void initalizeDatabase() throws SQLException, ClassNotFoundException { //setups the connection for the database if the database not exists then it creates it
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS players_lifes(uuid varchar(36) primary key, lifes int, isRevived boolean)");
        statement.close();
        statement = getConnection().createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS player_stats(uuid varchar(36) primary key, kills int, lifesCrafted int, " +
                "revivesCrafted int, beenRevived int, revivedSomeone int, allQuestCompleted int, dailyQuestCompleted int, weeklyQuestCompleted int)");
        statement.close();
        statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS player_language(uuid varchar(36) primary key, lang_name varchar(25))");
        statement.close();
        connection.close();
    }

    public void addPlayerToDatabase(String uuid, int lifes, boolean isRevived) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO players_lifes(uuid, lifes, isRevived) VALUES (?, ?, ?)");

        statement.setString(1, uuid);
        statement.setInt(2, lifes);
        statement.setBoolean(3, isRevived);

        statement.executeUpdate();

        statement.close();
        connection.close();

    }

    public ArrayList<OfflinePlayer> getAllDeadPlayers() throws SQLException {
        //We dont really have a proper attribiute for every dead person and it would be kinda useless so we get all the players which have 0 lifes lol
        ArrayList<OfflinePlayer> deadPlayers = new ArrayList<>();
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM players_lifes WHERE lifes = 0");


        ResultSet results = statement.executeQuery();

        while (results.next()) {
            deadPlayers.add(Bukkit.getOfflinePlayer(UUID.fromString(results.getString("uuid"))));
        }

        statement.close();
        connection.close();

        return deadPlayers;

    }

    public void addPlayerToStats(PlayerStats playerStats) throws SQLException {
        String sql = "INSERT INTO player_stats(uuid, kills, lifesCrafted, " +
                "revivesCrafted, beenRevived, revivedSomeone, allQuestCompleted, dailyQuestCompleted, weeklyQuestCompleted) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1,playerStats.getPlayer().getUniqueId().toString());
        statement.setInt(2, playerStats.getKills());
        statement.setInt(3, playerStats.getLifesCrafted());
        statement.setInt(4, playerStats.getRevivesCrafted());
        statement.setInt(5, playerStats.getBeenRevived());
        statement.setInt(6, playerStats.getRevivedSomeone());
        statement.setInt(7, playerStats.getAllQuestCompleted());
        statement.setInt(8, playerStats.getDailyQuestCompleted());
        statement.setInt(9, playerStats.getWeeklyQuestCompleted());

        statement.executeUpdate();
        statement.close();
        connection.close();

    }

    public void updatePlayerStats(PlayerStats playerStats) throws SQLException {
        String sql = "UPDATE player_stats SET kills = ?, lifesCrafted = ?, " +
                "revivesCrafted = ?, beenRevived = ?, revivedSomeone = ?, allQuestCompleted = ?, dailyQuestCompleted = ?, weeklyQuestCompleted = ? " +
                "WHERE uuid = ?";
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);


        statement.setInt(1, playerStats.getKills());
        statement.setInt(2, playerStats.getLifesCrafted());
        statement.setInt(3, playerStats.getRevivesCrafted());
        statement.setInt(4, playerStats.getBeenRevived());
        statement.setInt(5, playerStats.getRevivedSomeone());
        statement.setInt(6, playerStats.getAllQuestCompleted());
        statement.setInt(7, playerStats.getDailyQuestCompleted());
        statement.setInt(8, playerStats.getWeeklyQuestCompleted());
        statement.setString(9,playerStats.getPlayer().getUniqueId().toString());

        statement.executeUpdate();
        statement.close();
        connection.close();

    }





    public PlayerStats getPlayerStats(String uuid) throws SQLException {
        String sql = "SELECT * FROM player_stats WHERE uuid = ?";
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, uuid);

        ResultSet results = statement.executeQuery();

        if(results.next()){
            PlayerStats playerStats
                    = new PlayerStats(
                    Bukkit.getPlayer(UUID.fromString(uuid)),
                    results.getInt("kills"),
                    results.getInt("lifesCrafted"),
                    results.getInt("revivesCrafted"),
                    results.getInt("beenRevived"),
                    results.getInt("revivedSomeone"),
                    results.getInt("allQuestCompleted"),
                    results.getInt("dailyQuestCompleted"),
                    results.getInt("weeklyQuestCompleted")
            );

            statement.close();
            connection.close();

            return playerStats;

        }

        statement.close();
        connection.close();

        return null;

    }

    private void addPlayerLanguage(String uuid, Language language) throws SQLException {
        String sql = "INSERT INTO player_language(uuid, lang_name) VALUES (?, ?)";
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, uuid);
        statement.setString(2, language.toString());

        statement.executeUpdate();

        statement.close();
        connection.close();
    }

    public void updatePlayerLanguage(String uuid, Language language) throws SQLException {
        String sql = "UPDATE player_language SET lang_name = ? WHERE uuid = ?";
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, language.toString());
        statement.setString(2, uuid);

        statement.executeUpdate();

        statement.close();
        connection.close();
    }

    public Language getPlayerLanguage(String uuid) throws SQLException {
        String sql = "SELECT * FROM player_language WHERE uuid = ?";
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, uuid);

        ResultSet results = statement.executeQuery();
        Language language = null;
        if(results.next()){
            language = Language.valueOf(results.getString("lang_name"));
        }

        if(language != null){
            statement.close();
            connection.close();

            return language;
        }

        addPlayerLanguage(uuid, Language.POLISH);

        results = statement.executeQuery();
        if(results.next()){
            language = Language.valueOf(results.getString("lang_name"));
        }
        statement.close();
        connection.close();

        return language;
    }


    public int getPlayerLifes(String uuid) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement statement =connection.prepareStatement("SELECT * FROM players_lifes WHERE uuid = ?");

        statement.setString(1, uuid);

        ResultSet results = statement.executeQuery();

        if (results.next()) {
            int lifes = results.getInt("lifes");
            statement.close();
            connection.close();
            return lifes;
        }

        statement.close();
        connection.close();

        return -404;
    }

    public boolean getIsPlayerRevived(String uuid) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM players_lifes WHERE uuid = ?");

        statement.setString(1, uuid);

        ResultSet results = statement.executeQuery();

        if (results.next()) {
            boolean isRevived = results.getBoolean("isRevived");
            statement.close();
            connection.close();
            return isRevived;
        }

        statement.close();
        connection.close();

        return false;
    }

    public void updatePlayerLifes(String uuid, int lifes) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("UPDATE players_lifes SET lifes = ? WHERE uuid = ?");

        statement.setInt(1, lifes);
        statement.setString(2, uuid);

        statement.executeUpdate();

        statement.close();
        connection.close();
    }

    public void updatePlayerIsRevived(String uuid, boolean isRevived) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("UPDATE players_lifes SET isRevived = ? WHERE uuid = ?");

        statement.setBoolean(1, isRevived);
        statement.setString(2, uuid);

        statement.executeUpdate();

        statement.close();
        connection.close();
    }


}
