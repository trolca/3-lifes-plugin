package me.trololo11.lifesplugin.listeners;

import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.RecipesManager;
import me.trololo11.lifesplugin.database.LanguageManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class CustomItemsCraftingFix implements Listener {

    private RecipesManager recipesManager;
    private LifesPlugin plugin = LifesPlugin.getPlugin();

    public CustomItemsCraftingFix(RecipesManager recipesManager){
        this.recipesManager = recipesManager;
    }

    @EventHandler
    public void onCraft(CraftItemEvent e){
        //this class protects wrong items being crafted using custom items
        //(fe. crafting an iron ingot with revive shards)
        ItemStack resultItem = e.getRecipe().getResult();
        if(resultItem.hasItemMeta() && resultItem.getItemMeta().hasCustomModelData()
                && (resultItem.getItemMeta().getCustomModelData() == 8760001 || resultItem.getItemMeta().getCustomModelData() == 8760005)) return;

        Player p = (Player) e.getWhoClicked();

        for(ItemStack item : e.getInventory().getMatrix()){

            if(item != null && item.hasItemMeta() && item.getItemMeta().hasCustomModelData()){
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + LanguageManager.getLangLine("craft-with-custom-items-error", plugin.getPlayerLanguage(p)));
                break;
            }

        }


    }
}
