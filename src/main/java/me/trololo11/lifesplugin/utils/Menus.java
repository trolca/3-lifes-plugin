package me.trololo11.lifesplugin.utils;

import me.trololo11.lifesplugin.QuestsManager;
import me.trololo11.lifesplugin.RecipesManager;
import me.trololo11.lifesplugin.menus.MainLifesMenu;
import me.trololo11.lifesplugin.menus.QuestMenu;
import me.trololo11.lifesplugin.menus.StatsMenu;
import me.trololo11.lifesplugin.utils.questTypes.QuestType;

public class Menus {
    //a class which stores all of the menus because i dont want to make more mess in the main class lol (LifesPlugin class if you dont know what im talking about)

    private static QuestsManager questsManager;
    private static MainLifesMenu mainLifesMenu;
    private static RecipesManager recipesManager;

    public Menus(QuestsManager questsManager, RecipesManager recipesManager){
        Menus.questsManager = questsManager;
        Menus.recipesManager = recipesManager;
        mainLifesMenu = new MainLifesMenu(questsManager, recipesManager);
    }


    public static MainLifesMenu getMainLifesMenu() {
        return mainLifesMenu;
    }

    public static QuestMenu getQuestMenu(QuestType questType) {
        return new QuestMenu(questType, questsManager);
    }
    public static StatsMenu getStatsMenu(){ return new StatsMenu(); }

}
