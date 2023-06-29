package me.trololo11.lifesplugin.database;

import me.trololo11.lifesplugin.Language;
import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.utils.PluginUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class LanguageManager {

    private static HashMap<Language, File> languageFileMap = new HashMap<>();
    private static ArrayList<Language> supportedLanguages = new ArrayList<>();
    private static LifesPlugin plugin = LifesPlugin.getPlugin();

    public LanguageManager(){
        languageFileMap.put(Language.ENGLISH, new File(plugin.getDataFolder()+"/quests-data/language-files/english.yml"));
        languageFileMap.put(Language.POLISH, new File(plugin.getDataFolder()+"/quests-data/language-files/polish.yml"));

        supportedLanguages.add(Language.ENGLISH);
        supportedLanguages.add(Language.POLISH);
    }


    public static String getLangLine(String lineCode, Language language){
        File file = languageFileMap.getOrDefault(language, new File(plugin.getDataFolder()+"/quests-data/language-files/english.yml"));
        FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);

        return fileConfig.getString(lineCode);

    }

    public static HashMap<Language, String> getQuestNames(FileConfiguration fileConfig){

        HashMap<Language, String> namesList = new HashMap<>();


        for(Language language : supportedLanguages){

            String name =  fileConfig.getString(language.toString().toLowerCase()+"."+"task-name");
            if(name != null) namesList.put(language, name);
        }

        return namesList;

    }

    public static HashMap<Language, ArrayList<String>> getQuestDescription(FileConfiguration fileConfig){

        HashMap<Language, ArrayList<String>> namesList = new HashMap<>();

        for(Language language : supportedLanguages){
            ArrayList<String> description = (ArrayList<String>) fileConfig.getStringList(language.toString().toLowerCase()+"."+"description");
            for(int i=0; i < description.size(); i++){
                description.set(i, ChatColor.WHITE + PluginUtils.chat(description.get(i)));
            }
            if(!description.isEmpty()) namesList.put(language, description);
        }

        return namesList;

    }


    public static ArrayList<String> getLangArrayLine(String lineCode, Language language){
        File file = languageFileMap.getOrDefault(language, new File(plugin.getDataFolder()+"/quests-data/language-files/english.yml"));
        FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);

        ArrayList<String> langArray = (ArrayList<String>) fileConfig.getStringList(lineCode);

        return langArray;
    }



}
