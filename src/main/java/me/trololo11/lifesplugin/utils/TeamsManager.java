package me.trololo11.lifesplugin.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;

public class TeamsManager {

    private Scoreboard epicScoreboard;
    private Team oneLife;
    private Team twoLifes;
    private Team threeLifes;
    private Team moreLifes;
    private Team dead;
    private ArrayList<Team> lifesTeamList = new ArrayList<>();

    /*
    sooo you have teams in a array list for diffrent number of lifes and the smart thing is that the number of teams in the array list corresponds to the number
    of lifes that you need to have to be in this team (ex team number 2 is the yellow team and if player has 2 lifes then you can just get the team from the number of lifes)

    btw I remebered about this fancy comments lol

     */

    public void registerEverything() {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        Scoreboard teamScoreboard = scoreboardManager.getMainScoreboard();

        dead = teamScoreboard.getTeam("dead");
        if (dead == null) dead = teamScoreboard.registerNewTeam("dead");
        dead.setColor(ChatColor.DARK_RED);
        dead.setPrefix("" + ChatColor.DARK_RED);
        lifesTeamList.add(dead);

        oneLife = teamScoreboard.getTeam("oneLife");
        if (oneLife == null) oneLife = teamScoreboard.registerNewTeam("oneLife");
        oneLife.setColor(ChatColor.RED);
        oneLife.setPrefix("" + ChatColor.RED);
        lifesTeamList.add(oneLife);

        twoLifes = teamScoreboard.getTeam("twoLifes");
        if (twoLifes == null) twoLifes = teamScoreboard.registerNewTeam("twoLifes");
        twoLifes.setColor(ChatColor.YELLOW);
        twoLifes.setPrefix("" + ChatColor.YELLOW);
        lifesTeamList.add(twoLifes);

        threeLifes = teamScoreboard.getTeam("threeLifes");
        if (threeLifes == null) threeLifes = teamScoreboard.registerNewTeam("threeLifes");
        threeLifes.setColor(ChatColor.GREEN);
        threeLifes.setPrefix("" + ChatColor.GREEN);
        lifesTeamList.add(threeLifes);

        moreLifes = teamScoreboard.getTeam("moreLifes");
        if (moreLifes == null) moreLifes = teamScoreboard.registerNewTeam("moreLifes");
        moreLifes.setColor(ChatColor.DARK_GREEN);
        moreLifes.setPrefix("" + ChatColor.DARK_GREEN);
        lifesTeamList.add(moreLifes);

    }

    public ArrayList<Team> getLifesTeamList() {
        return lifesTeamList;
    }

}
