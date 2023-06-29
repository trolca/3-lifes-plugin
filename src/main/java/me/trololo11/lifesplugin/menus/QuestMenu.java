package me.trololo11.lifesplugin.menus;

import me.trololo11.lifesplugin.Language;
import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.QuestsManager;
import me.trololo11.lifesplugin.database.LanguageManager;
import me.trololo11.lifesplugin.utils.Menu;
import me.trololo11.lifesplugin.utils.Menus;
import me.trololo11.lifesplugin.utils.PluginUtils;
import me.trololo11.lifesplugin.utils.questTypes.QuestType;
import me.trololo11.lifesplugin.utils.questTypes.TaskListenerType;
import me.trololo11.lifesplugin.utils.questUtils.QuestClass;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class QuestMenu extends Menu {

    private QuestType tasksType;
    private LifesPlugin plugin = LifesPlugin.getPlugin();
    private QuestsManager questsManager;
    private ArrayList<String> lore = new ArrayList<>();

    public QuestMenu(QuestType tasksType, QuestsManager questsManager) {
        this.tasksType = tasksType;
        this.questsManager = questsManager;
    }


    @Override
    public String getMenuName(Player p) {
        return tasksType == QuestType.DAILY ? ChatColor.RED + LanguageManager.getLangLine("daily-quests-page-name", plugin.getPlayerLanguage(p))
                : ChatColor.YELLOW + LanguageManager.getLangLine("weekly-quests-page-name", plugin.getPlayerLanguage(p));
    }

    @Override
    public int getSlots() {
        return 45;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {

        ItemStack currItem = e.getCurrentItem();
        Player p = (Player) e.getWhoClicked();

        switch (currItem.getType()) {
            case RED_DYE:
                if (!e.getCurrentItem().getItemMeta().getLocalizedName().equalsIgnoreCase("back")) return;

                Menus.getMainLifesMenu().open(p);
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f);


                break;
        }

    }

    @Override
    public void setMenuItems(Player p) {
        ArrayList<QuestClass> questsList = tasksType == QuestType.DAILY ? questsManager.getDailyQuests() : questsManager.getWeeklyQuests();

        ItemStack filler = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemStack fillerDark = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemStack back = new ItemStack(Material.RED_DYE);
        ItemStack completed = new ItemStack(Material.GREEN_WOOL);
        ItemStack notCompleted = new ItemStack(Material.RED_WOOL);
        Language language = plugin.getPlayerLanguage(p);

        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.setDisplayName(" ");
        fillerMeta.setLocalizedName("filler");

        ItemMeta darkFillerMeta = fillerDark.getItemMeta();
        darkFillerMeta.setDisplayName(" ");
        darkFillerMeta.setLocalizedName("filler");

        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + LanguageManager.getLangLine("back-item-name", language));
        backMeta.setLocalizedName("back");

        ItemMeta completedMeta = completed.getItemMeta();
        completedMeta.setDisplayName(ChatColor.GREEN + LanguageManager.getLangLine("completed-progress-general", language));
        completedMeta.setLocalizedName("completed");

        ItemMeta notCompletedMeta = notCompleted.getItemMeta();
        notCompletedMeta.setDisplayName(ChatColor.RED + LanguageManager.getLangLine("not-completed-quest", language));
        notCompletedMeta.setLocalizedName("not-completed");

        filler.setItemMeta(fillerMeta);
        fillerDark.setItemMeta(darkFillerMeta);
        back.setItemMeta(backMeta);
        completed.setItemMeta(completedMeta);
        notCompleted.setItemMeta(notCompletedMeta);

        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, fillerDark);
        }

        inventory.setItem(8, back);
        for (int i = 9; i < getSlots(); i++) {
            inventory.setItem(i, filler);
        }

        for (int i = 0; i < questsList.size(); i++) {
            QuestClass quest = questsList.get(i);

            ItemStack questIcon = new ItemStack(quest.getIcon());

            ItemMeta questMeta = questIcon.getItemMeta();




            questMeta.setDisplayName(ChatColor.RESET + PluginUtils.chat(quest.getName(language)));

            lore.clear();
            lore.addAll(quest.getDescription(language));
            if(quest.getListenerType() == TaskListenerType.BREAK_BLOCKS_NO_SILK)
                lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + LanguageManager.getLangLine("not-silk-touch", language));
            lore.add("");
            try {
                lore.add(quest.hasCompleted(p) ? PluginUtils.chat("&a&l"+LanguageManager.getLangLine("completed-progress-general", language)) :
                        quest.showProgress() ? PluginUtils.chat("&2&l"+ LanguageManager.getLangLine("progress-quests-general", language)
                                .replace("<progress>", quest.getProgress(p) + "/" + quest.getMaxProgress())) :
                                PluginUtils.chat("&c&l"+LanguageManager.getLangLine("not-completed-quest", language)));

            } catch (SQLException e) {
                e.printStackTrace();
            }
            questMeta.setLore(lore);
            lore = new ArrayList<>();

            questMeta.setLocalizedName(quest.getTableName());
            questMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            questMeta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
            questMeta.addItemFlags(ItemFlag.HIDE_DYE);


            questIcon.setItemMeta(questMeta);


            int slot; //sets the slot number for quests
            if (i < 4) slot = 9 * (i + 1);
            else if (i < 8) slot = (9 * (i - 3)) + 3;
            else slot = (9 * (i - 7)) + 6;

            if (slot >= 45) slot = 42;
            try {
                inventory.setItem(slot, quest.hasCompleted(p) ? completed : notCompleted);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            inventory.setItem(slot + 1, questIcon);
        }

    }
}
