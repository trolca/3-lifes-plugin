package me.trololo11.lifesplugin.utils;

import org.bukkit.entity.Player;

public class PlayerStats {

    private Player player;
    private int kills, lifesCrafted, revivesCrafted, beenRevived, revivedSomeone, allQuestCompleted, dailyQuestCompleted, weeklyQuestCompleted;

    //a class which easliy stores the player stats for the plugin
    public PlayerStats(Player player ,int kills, int lifesCrafted, int revivesCrafted, int beenRevived, int revivedSomeone, int allQuestCompleted, int dailyQuestCompleted, int weeklyQuestCompleted) {
        this.player = player;
        this.kills = kills;
        this.lifesCrafted = lifesCrafted;
        this.revivesCrafted = revivesCrafted;
        this.beenRevived = beenRevived;
        this.revivedSomeone = revivedSomeone;
        this.allQuestCompleted = allQuestCompleted;
        this.dailyQuestCompleted = dailyQuestCompleted;
        this.weeklyQuestCompleted = weeklyQuestCompleted;
    }


    public Player getPlayer() {
        return player;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getLifesCrafted() {
        return lifesCrafted;
    }

    public void setLifesCrafted(int lifesCrafted) {
        this.lifesCrafted = lifesCrafted;
    }

    public int getRevivesCrafted() {
        return revivesCrafted;
    }

    public void setRevivesCrafted(int revivesCrafted) {
        this.revivesCrafted = revivesCrafted;
    }

    public int getBeenRevived() {
        return beenRevived;
    }

    public void setBeenRevived(int beenRevived) {
        this.beenRevived = beenRevived;
    }

    public int getRevivedSomeone() {
        return revivedSomeone;
    }

    public void setRevivedSomeone(int revivedSomeone) {
        this.revivedSomeone = revivedSomeone;
    }

    public int getAllQuestCompleted() {
        return allQuestCompleted;
    }

    public void setAllQuestCompleted(int allQuestCompleted) {
        this.allQuestCompleted = allQuestCompleted;
    }

    public int getDailyQuestCompleted() {
        return dailyQuestCompleted;
    }

    public void setDailyQuestCompleted(int dailyQuestCompleted) {
        this.dailyQuestCompleted = dailyQuestCompleted;
    }

    public int getWeeklyQuestCompleted() {
        return weeklyQuestCompleted;
    }

    public void setWeeklyQuestCompleted(int weeklyQuestCompleted) {
        this.weeklyQuestCompleted = weeklyQuestCompleted;
    }

}
