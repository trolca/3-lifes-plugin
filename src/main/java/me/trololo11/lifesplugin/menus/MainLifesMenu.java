package me.trololo11.lifesplugin.menus;

import me.trololo11.lifesplugin.Language;
import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.QuestsManager;
import me.trololo11.lifesplugin.RecipesManager;
import me.trololo11.lifesplugin.database.LanguageManager;
import me.trololo11.lifesplugin.utils.Menu;
import me.trololo11.lifesplugin.utils.Menus;
import me.trololo11.lifesplugin.utils.PluginUtils;
import me.trololo11.lifesplugin.utils.questTypes.QuestType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;

//yes menu logic
public class MainLifesMenu extends Menu {

    private LifesPlugin plugin = LifesPlugin.getPlugin();
    private QuestsManager questsManager;
    private RecipesManager recipesManager;

    public MainLifesMenu(QuestsManager questsManager, RecipesManager recipesManager){
        this.questsManager = questsManager;
        this.recipesManager = recipesManager;
    }

    @Override
    public String getMenuName(Player p) {
        return ChatColor.RED + "" + ChatColor.BOLD + LanguageManager.getLangLine("main-life-menu-name", plugin.getPlayerLanguage(p));
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        boolean hasFinishedDaily = questsManager.getPlayerCompletedDailyQuests(p) >= questsManager.getDailyQuests().size();
        boolean hasFinishedWeekly = questsManager.getPlayerCompletedWeeklyQuests(p) >= questsManager.getWeeklyQuests().size();

        switch (e.getCurrentItem().getType()) {
            case ENCHANTED_BOOK:
                if(e.getCurrentItem().getItemMeta().getLocalizedName().equalsIgnoreCase("daily-tasks")){
                    Menus.getQuestMenu(QuestType.DAILY).open(p);
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f);
                }
                if(e.getCurrentItem().getItemMeta().getLocalizedName().equalsIgnoreCase("weekly-tasks")){
                    Menus.getQuestMenu(QuestType.WEEKLY).open(p);
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f);
                }
                break;

            case PLAYER_HEAD:
                if(!e.getCurrentItem().getItemMeta().getLocalizedName().equalsIgnoreCase("player-stats")) return;

                Menus.getStatsMenu().open(p);
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f);

                break;

            case GOLD_NUGGET:
                if(!e.getCurrentItem().getItemMeta().getLocalizedName().equalsIgnoreCase("heart-piece")) return;
                if(!hasFinishedDaily) return;
                if(questsManager.getHasTakenDaily(p)) return;

                p.getInventory().addItem(recipesManager.getLifeShardItem());
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                questsManager.setPlayerHasTakenDaily(p, true);
                setMenuItems(p);
                p.updateInventory();

            break;

            case IRON_NUGGET:
                if(!e.getCurrentItem().getItemMeta().getLocalizedName().equalsIgnoreCase("revive-piece")) return;
                if(!hasFinishedWeekly) return;
                if(questsManager.getHasTakenWeekly(p)) return;

                p.getInventory().addItem(recipesManager.getReviveShardItem());
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                questsManager.setPlayerHasTakenWeekly(p, true);
                setMenuItems(p);
                p.updateInventory();

                break;

            case NAME_TAG:
                if(!e.getCurrentItem().getItemMeta().getLocalizedName().equalsIgnoreCase("language")) return;
                Language language = plugin.getPlayerLanguage(p);
                if(language == Language.POLISH) plugin.setPlayerLanguage(p, Language.ENGLISH);
                else plugin.setPlayerLanguage(p, Language.POLISH);

                setMenuItems(p);
                p.updateInventory();

                break;

            case RED_DYE:
                if(!e.getCurrentItem().getItemMeta().getLocalizedName().equalsIgnoreCase("exit")) return;
                if(e.getClick() != ClickType.RIGHT && e.getClick() != ClickType.LEFT && e.getClick() != ClickType.MIDDLE) return;

                p.closeInventory();
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f);
                break;
        }
    }

    @Override
    public void setMenuItems(Player p) {
        ItemStack filler = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemStack heartPiece;
        ItemStack revivePiece;
        ItemStack exit = new ItemStack(Material.RED_DYE);
        ItemStack languageSwitch = new ItemStack(Material.NAME_TAG);
        ItemStack darkFiller = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemStack notProgress = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemStack progress = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemStack dailyTasks = new ItemStack(Material.ENCHANTED_BOOK);
        ItemStack weeklyTasks = new ItemStack(Material.ENCHANTED_BOOK);
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        Language language = plugin.getPlayerLanguage(p);

        if(questsManager.getHasTakenDaily(p)) heartPiece = new ItemStack(Material.BARRIER);
        else heartPiece = new ItemStack(Material.GOLD_NUGGET);

        if(questsManager.getHasTakenWeekly(p)) revivePiece = new ItemStack(Material.BARRIER);
        else revivePiece = new ItemStack(Material.IRON_NUGGET);


        boolean hasFinishedDaily = questsManager.getPlayerCompletedDailyQuests(p) >= questsManager.getDailyQuests().size();
        boolean hasFinishedWeekly = questsManager.getPlayerCompletedWeeklyQuests(p) >= questsManager.getWeeklyQuests().size();

        ArrayList<String> lore = new ArrayList<>();

        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.setDisplayName(" ");

        ItemMeta darkFillerMeta = darkFiller.getItemMeta();
        darkFillerMeta.setDisplayName(" ");
        darkFillerMeta.setLocalizedName("filler");
        

        ItemMeta notProgressMeta = notProgress.getItemMeta();
        notProgressMeta.setDisplayName(" ");
        notProgressMeta.setLocalizedName("not-progress");

        ItemMeta progressMeta = progress.getItemMeta();
        progressMeta.setDisplayName(" ");
        progressMeta.setLocalizedName("progress");

        ItemMeta exitMeta = exit.getItemMeta();
        exitMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + LanguageManager.getLangLine("exit-item-name", language));
        exitMeta.setLocalizedName("exit");

        ItemMeta languageMeta = languageSwitch.getItemMeta();
        languageMeta.setDisplayName(PluginUtils.chat(
                "&a"+ LanguageManager.getLangLine("language-item-name", language).replace("<language>",
                        language == Language.ENGLISH ? "&leng&r&2/pl" : "&2eng/&r&a&lpl")));

        languageMeta.setLocalizedName("language");

        ItemMeta heartMeta = heartPiece.getItemMeta();
        if(!hasFinishedDaily && !questsManager.getHasTakenDaily(p))
            heartMeta.setDisplayName(ChatColor.RED + LanguageManager.getLangLine("daily-quests-progress", language));

        else if(hasFinishedDaily && !questsManager.getHasTakenDaily(p))
            heartMeta.setDisplayName(ChatColor.YELLOW + LanguageManager.getLangLine("take-life-shard-name", language));

        else
            heartMeta.setDisplayName(ChatColor.DARK_RED + LanguageManager.getLangLine("taken-life-shard-name", language));

        heartMeta.setCustomModelData(8760001);
        heartMeta.setLocalizedName("heart-piece");

        ItemMeta reviveMeta = revivePiece.getItemMeta();
        if(!hasFinishedWeekly && !questsManager.getHasTakenWeekly(p))
            reviveMeta.setDisplayName(ChatColor.WHITE + LanguageManager.getLangLine("weekly-quests-progress", language));

        else if(hasFinishedWeekly && !questsManager.getHasTakenWeekly(p))
            reviveMeta.setDisplayName(ChatColor.YELLOW + LanguageManager.getLangLine("take-revive-shard-name", language));

        else
            reviveMeta.setDisplayName(ChatColor.DARK_RED + LanguageManager.getLangLine("taken-revive-shard-name", language));

        reviveMeta.setCustomModelData(8760001);
        reviveMeta.setLocalizedName("revive-piece");

        ItemMeta dailyTasksMeta = dailyTasks.getItemMeta();
        dailyTasksMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + LanguageManager.getLangLine("daily-quests-page-item-name", language));
        lore.clear();
        lore.add(ChatColor.WHITE + LanguageManager.getLangLine("daily-quests-page-item-lore", language));
        lore.add("");

        lore.add(ChatColor.AQUA + LanguageManager.getLangLine("time-quests-general",language).replace("<time>", plugin.getDailyPageText()));

        lore.add(questsManager.hasFinishedAllDailyQuests(p) ? ChatColor.GREEN + "" +ChatColor.BOLD + LanguageManager.getLangLine("completed-progress-general", language)
                : ChatColor.DARK_GREEN + "" +ChatColor.BOLD + LanguageManager.getLangLine("progress-quests-general", language)
                .replace("<progress>",questsManager.getPlayerCompletedDailyQuests(p) + "/"+questsManager.getDailyQuests().size()));

        dailyTasksMeta.setLore(lore);
        dailyTasksMeta.setLocalizedName("daily-tasks");

        ItemMeta weeklyTasksMeta = weeklyTasks.getItemMeta();
        weeklyTasksMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + LanguageManager.getLangLine("weekly-quests-page-item-name", language));
        lore.clear();
        lore.add(ChatColor.WHITE + LanguageManager.getLangLine("weekly-quests-page-item-lore", language));
        lore.add("");
        lore.add(ChatColor.AQUA + LanguageManager.getLangLine("time-quests-general",language).replace("<time>", plugin.getWeeklyPageText()));

        lore.add(questsManager.hasFinishedAllWeeklyQuests(p) ? ChatColor.GREEN + "" +ChatColor.BOLD + LanguageManager.getLangLine("completed-progress-general", language)
                : ChatColor.DARK_GREEN + "" +ChatColor.BOLD + LanguageManager.getLangLine("progress-quests-general", language)
                .replace("<progress>",questsManager.getPlayerCompletedWeeklyQuests(p) + "/"+questsManager.getWeeklyQuests().size()));

        weeklyTasksMeta.setLore(lore);
        weeklyTasksMeta.setLocalizedName("weekly-tasks");

        SkullMeta playerHeadMeta = (SkullMeta) playerHead.getItemMeta();
        playerHeadMeta.setOwningPlayer(p);
        playerHeadMeta.setDisplayName(ChatColor.GREEN +  LanguageManager.getLangLine("player-stats-name",language));
        playerHeadMeta.setLocalizedName("player-stats");

        filler.setItemMeta(fillerMeta);
        heartPiece.setItemMeta(heartMeta);
        revivePiece.setItemMeta(reviveMeta);
        darkFiller.setItemMeta(darkFillerMeta);
        notProgress.setItemMeta(notProgressMeta);
        progress.setItemMeta(progressMeta);
        dailyTasks.setItemMeta(dailyTasksMeta);
        weeklyTasks.setItemMeta(weeklyTasksMeta);
        playerHead.setItemMeta(playerHeadMeta);
        languageSwitch.setItemMeta(languageMeta);
        exit.setItemMeta(exitMeta);

        for (int i = 0; i < getSlots(); i++) {
            inventory.setItem(i, filler);
        }

        float dailyPercentageProgress = (float) questsManager.getPlayerCompletedDailyQuests(p)/questsManager.getDailyQuests().size()*100;
        float weeklyPercentageProgress = (float) questsManager.getPlayerCompletedWeeklyQuests(p)/ questsManager.getWeeklyQuests().size()*100;
        //the varaible shows how many slots will be filled in the menu
        int fillDailySlots = (int) Math.floor((dailyPercentageProgress*8)/100.0);
        int fillWeeklySlots = (int) Math.floor((weeklyPercentageProgress*8)/100.0);
        //making of the daily quests progress bar (its on top btw)

        for(int i = 1; i <= fillDailySlots; i++){
            inventory.setItem(i, progress);
        }
        for(int i = fillDailySlots+1; i < 9; i++){
            inventory.setItem(i, notProgress);
        }

        for(int i = 10; i <= fillWeeklySlots+9; i++){
            inventory.setItem(i, progress);
        }

        for(int i = fillWeeklySlots+10; i < 18; i++){
            inventory.setItem(i, notProgress);
        }

        for (int i = 18; i < 27; i++) {
            inventory.setItem(i, darkFiller);
        }

        inventory.setItem(0, heartPiece);
        inventory.setItem(9, revivePiece);
        inventory.setItem(26, exit);
        inventory.setItem(25, languageSwitch);
        inventory.setItem(37, dailyTasks);
        inventory.setItem(43, weeklyTasks);
        inventory.setItem(40, playerHead);


    }

}
