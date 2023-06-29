package me.trololo11.lifesplugin.menus;

import me.trololo11.lifesplugin.Language;
import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.database.LanguageManager;
import me.trololo11.lifesplugin.utils.Menu;
import me.trololo11.lifesplugin.utils.Menus;
import me.trololo11.lifesplugin.utils.PlayerStats;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;

public class StatsMenu extends Menu {

    private LifesPlugin plugin = LifesPlugin.getPlugin();

    @Override
    public String getMenuName(Player p) {
        return ChatColor.GREEN + LanguageManager.getLangLine("stats-menu-name", plugin.getPlayerLanguage(p)).replace("<player>", p.getName());
    }

    @Override
    public int getSlots() {
        return 45;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if(e.getCurrentItem().getType() == Material.RED_DYE && e.getCurrentItem().getItemMeta().getLocalizedName().equalsIgnoreCase("back")){
            Menus.getMainLifesMenu().open(p);
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f);
        }

    }

    @Override
    public void setMenuItems(Player p) {
        ArrayList<String> lore;
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        ItemStack peopleKilled = new ItemStack(Material.DIAMOND_SWORD);
        ItemStack lifesCrafted = new ItemStack(Material.SCUTE);
        ItemStack revivesCrafted = new ItemStack(Material.SCUTE);
        ItemStack beenRevived = new ItemStack(Material.GOLDEN_APPLE);
        ItemStack revivedSomeone = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE);
        ItemStack allQuests = new ItemStack(Material.NETHERITE_PICKAXE);
        ItemStack dailyQuests = new ItemStack(Material.GOLD_NUGGET);
        ItemStack weeklyQuests = new ItemStack(Material.IRON_NUGGET);
        ItemStack filler = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemStack redFiller = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemStack back = new ItemStack(Material.RED_DYE);

        Language language = plugin.getPlayerLanguage(p);

        PlayerStats playerStats = plugin.getPlayerStats(p);

        SkullMeta playerHeadMeta = (SkullMeta) playerHead.getItemMeta();
        playerHeadMeta.setOwningPlayer(p);
        playerHeadMeta.setDisplayName(ChatColor.GREEN + LanguageManager.getLangLine("player-stats-item-name", language).replace("<player>", p.getName()));

        ItemMeta peopleKillMeta = peopleKilled.getItemMeta();
        peopleKillMeta.setDisplayName(ChatColor.DARK_RED + ""+ChatColor.BOLD +
                LanguageManager.getLangLine("killed-players-stat-name", language).replace("<num>", playerStats.getKills() + ""));

        lore = turnLoreWhite(LanguageManager.getLangArrayLine("killed-players-stat-lore", language));

        peopleKillMeta.setLore(lore);
        peopleKillMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        ItemMeta lifesCraftedMeta = lifesCrafted.getItemMeta();
        lifesCraftedMeta.setDisplayName(ChatColor.RED + ""+ChatColor.BOLD
                +LanguageManager.getLangLine("lifes-crafted-stat-name", language).replace("<num>", playerStats.getLifesCrafted() + ""));

        lifesCraftedMeta.setCustomModelData(8760001);

        lore = turnLoreWhite(LanguageManager.getLangArrayLine("lifes-crafted-stat-lore", language));

        lifesCraftedMeta.setLore(lore);

        ItemMeta revivesCraftedMeta = revivesCrafted.getItemMeta();
        revivesCraftedMeta.setDisplayName(ChatColor.AQUA + ""+ChatColor.BOLD
                +LanguageManager.getLangLine("revives-crafted-stat-name", language).replace("<num>", playerStats.getRevivesCrafted() + ""));

        revivesCraftedMeta.setCustomModelData(8760005);

        lore = turnLoreWhite(LanguageManager.getLangArrayLine("revives-crafted-stat-lore", language));

        revivesCraftedMeta.setLore(lore);

        ItemMeta beenRevivedMeta = beenRevived.getItemMeta();
        beenRevivedMeta.setDisplayName(ChatColor.DARK_AQUA + ""+ChatColor.BOLD
                +LanguageManager.getLangLine("been-revived-stat-name", language).replace("<num>", playerStats.getBeenRevived() + ""));

        lore = turnLoreWhite(LanguageManager.getLangArrayLine("been-revived-stat-lore", language));

        beenRevivedMeta.setLore(lore);

        ItemMeta revivedSomeoneMeta = revivedSomeone.getItemMeta();
        revivedSomeoneMeta.setDisplayName(ChatColor.BLUE + ""+ChatColor.BOLD
                +LanguageManager.getLangLine("revived-someone-stat-name", language).replace("<num>", playerStats.getRevivedSomeone() + ""));

        lore = turnLoreWhite(LanguageManager.getLangArrayLine("revived-someone-stat-lore", language));

        revivedSomeoneMeta.setLore(lore);

        ItemMeta allQuestsMeta = allQuests.getItemMeta();
        allQuestsMeta.setDisplayName(ChatColor.GOLD + ""+ChatColor.BOLD
                +LanguageManager.getLangLine("all-quests-stat-name", language).replace("<num>", playerStats.getAllQuestCompleted() + ""));

        allQuestsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        lore = turnLoreWhite(LanguageManager.getLangArrayLine("all-quests-stat-lore", language));

        allQuestsMeta.setLore(lore);

        ItemMeta dailyQuestsMeta = dailyQuests.getItemMeta();
        dailyQuestsMeta.setDisplayName(ChatColor.RED + ""+ChatColor.BOLD
                +LanguageManager.getLangLine("daily-quests-stat-name", language).replace("<num>", playerStats.getDailyQuestCompleted() + ""));

        dailyQuestsMeta.setCustomModelData(8760001);

        lore = turnLoreWhite(LanguageManager.getLangArrayLine("daily-quests-stat-lore", language));

        dailyQuestsMeta.setLore(lore);

        ItemMeta weeklyQuestsMeta = weeklyQuests.getItemMeta();
        weeklyQuestsMeta.setDisplayName(ChatColor.YELLOW + ""+ChatColor.BOLD
                +LanguageManager.getLangLine("weekly-quests-stat-name", language).replace("<num>", playerStats.getWeeklyQuestCompleted() + ""));

        weeklyQuestsMeta.setCustomModelData(8760001);

        lore = turnLoreWhite(LanguageManager.getLangArrayLine("weekly-quests-stat-lore", language));

        weeklyQuestsMeta.setLore(lore);

        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + LanguageManager.getLangLine("back-item-name", language));
        backMeta.setLocalizedName("back");

        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.setDisplayName(" ");

         playerHead.setItemMeta(playerHeadMeta);
         peopleKilled.setItemMeta(peopleKillMeta);
         lifesCrafted.setItemMeta(lifesCraftedMeta);
         revivesCrafted.setItemMeta(revivesCraftedMeta);
         beenRevived.setItemMeta(beenRevivedMeta);
         revivedSomeone.setItemMeta(revivedSomeoneMeta);
         allQuests.setItemMeta(allQuestsMeta);
         dailyQuests.setItemMeta(dailyQuestsMeta);
         weeklyQuests.setItemMeta(weeklyQuestsMeta);
         back.setItemMeta(backMeta);
         filler.setItemMeta(fillerMeta);
         redFiller.setItemMeta(fillerMeta);

         for(int i=0; i < 9; i++){
             inventory.setItem(i, redFiller);
         }

         for(int i=9; i < getSlots(); i++){
             inventory.setItem(i, filler);
         }

         inventory.setItem(8, back);
         inventory.setItem(4, playerHead);
         inventory.setItem(10, peopleKilled);
         inventory.setItem(19, lifesCrafted);
         inventory.setItem(28, revivesCrafted);
         inventory.setItem(37, beenRevived);
         inventory.setItem(16, revivedSomeone);
         inventory.setItem(25, allQuests);
         inventory.setItem(34, dailyQuests);
         inventory.setItem(43, weeklyQuests);





    }

    private ArrayList<String> turnLoreWhite(ArrayList<String> list){
        for(int i=0; i < list.size(); i++){
            list.set(i, ChatColor.WHITE + list.get(i));
        }

        return list;
    }
}
