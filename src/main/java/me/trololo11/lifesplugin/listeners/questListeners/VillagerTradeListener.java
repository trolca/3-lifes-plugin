package me.trololo11.lifesplugin.listeners.questListeners;

import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.utils.QuestListener;
import me.trololo11.lifesplugin.utils.questUtils.QuestClass;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;

import java.sql.SQLException;
import java.util.ArrayList;

public class VillagerTradeListener extends QuestListener {

    private LifesPlugin plugin = LifesPlugin.getPlugin();
    @EventHandler
    public void onTrade(InventoryClickEvent e) throws SQLException {
        if(!(e.getWhoClicked() instanceof Player)) return;
        Player player = (Player) e.getWhoClicked();
        if(e.getClickedInventory() == null) return;
        if(e.getClickedInventory().getType() != InventoryType.MERCHANT) return;
        MerchantInventory merchantInventory = (MerchantInventory) e.getClickedInventory();
        if(merchantInventory.getSelectedRecipe() == null) return;
        if(merchantInventory.getSelectedRecipe().getAdjustedIngredient1() == null) return;
        MerchantRecipe merchantRecipe = merchantInventory.getSelectedRecipe();
        ItemStack item = merchantInventory.getSelectedRecipe().getAdjustedIngredient1();
        ItemStack itemSus = merchantInventory.getItem(0);
        if(itemSus == null) return;
        if(item.getType() != Material.EMERALD)  return;

        if(e.getClick() != ClickType.SHIFT_LEFT && e.getClick() != ClickType.SHIFT_RIGHT){

            checkTarget(null, player, item.getAmount());

        }else{
            int maxUsages = merchantRecipe.getMaxUses();
            int thisUses = itemSus.getAmount()/item.getAmount();
            if(thisUses > maxUsages){
                checkTarget(item.getType(), player, maxUsages*item.getAmount());
            }else{
                checkTarget(item.getType(), player, thisUses*item.getAmount());
            }

        }
    }

    @Override
    public ArrayList<QuestClass> getListenerArray() {
        return plugin.getListenerArrays().getVillagerTradeQuests();
    }
}
