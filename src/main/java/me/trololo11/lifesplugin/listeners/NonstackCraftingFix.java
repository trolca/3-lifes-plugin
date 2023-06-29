package me.trololo11.lifesplugin.listeners;

import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.RecipesManager;
import me.trololo11.lifesplugin.database.LanguageManager;
import me.trololo11.lifesplugin.tasks.LifesStackCheckTask;
import me.trololo11.lifesplugin.utils.PlayerStats;
import me.trololo11.lifesplugin.utils.questTypes.QuestType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class NonstackCraftingFix implements Listener {

    private RecipesManager recipesManager;
    private LifesPlugin plugin = LifesPlugin.getPlugin();

    public NonstackCraftingFix(RecipesManager recipesManager){
        this.recipesManager = recipesManager;
    }

    @EventHandler
    public void onCraft(CraftItemEvent e){
        //checks if the recipe is any of the lifes custom recipes
        if(e.getRecipe().getResult().equals(recipesManager.getLifesRecipe().getResult())) setItems(e, recipesManager.getLifeItem(), QuestType.DAILY);
        else if(e.getRecipe().getResult().equals(recipesManager.getReviveCardRecipe().getResult())) setItems(e, recipesManager.getReviveCardItem(), QuestType.WEEKLY);

    }

    private void setItems(CraftItemEvent e, ItemStack itemStack, QuestType questType){
        e.setCurrentItem(itemStack);
        Player p = (Player) e.getWhoClicked();

        if(e.getClick() == ClickType.SHIFT_RIGHT || e.getClick() == ClickType.SHIFT_LEFT) {
            //if it is crafted with the shift click it only makes 1 life or revive card unstackable
            //so we have to do it manually and this checks if there is enough space for that
            System.out.println(recipesManager.getRecipesUtils().getEmptySlots(p.getInventory().getContents()));
            if(recipesManager.getRecipesUtils().getMaxCraftAmount(e.getInventory()) > recipesManager.getRecipesUtils().getEmptySlots(p.getInventory().getContents())){
                p.sendMessage(ChatColor.RED + LanguageManager.getLangLine("lifes-craft-stack-error", plugin.getPlayerLanguage(p)));
                e.setCancelled(true);
                return;
            }

            //more is explained inside of the class
            LifesStackCheckTask lifesStackCheckTask = new LifesStackCheckTask(p);
            lifesStackCheckTask.runTaskLater(plugin, 1L);

        }

        //sets stats ya know
        PlayerStats playerStats = plugin.getPlayerStats(p);

        if(questType == QuestType.DAILY) playerStats.setLifesCrafted(playerStats.getLifesCrafted()+1);
        else playerStats.setRevivesCrafted(playerStats.getRevivesCrafted()+1);

        plugin.setPlayerStats(p, playerStats);

    }


}
