package me.trololo11.lifesplugin.listeners.questListeners;

import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.utils.QuestListener;
import me.trololo11.lifesplugin.utils.questUtils.QuestClass;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.ArrayList;

public class SmeltItemListener extends QuestListener {

    private LifesPlugin plugin = LifesPlugin.getPlugin();

    @EventHandler
    public void onSmelt(InventoryClickEvent e) throws SQLException {
        if(e.getClick() == ClickType.MIDDLE) return;
        if(e.getCurrentItem() == null) return;
        if(e.getClickedInventory() == null) return;
        if(e.getSlot() != 2) return;
        if(!(e.getWhoClicked() instanceof Player)) return;
        Inventory inventory = e.getClickedInventory();
        if(inventory.getType() != InventoryType.BLAST_FURNACE
        && inventory.getType() != InventoryType.FURNACE
        && inventory.getType() != InventoryType.SMOKER) return;
        if(e.getClick() == ClickType.NUMBER_KEY){
            e.setCancelled(true);
            return;
        }
        ItemStack item = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();

        checkTarget(item.getType(), player, item.getAmount());
    }

    @Override
    public ArrayList<QuestClass> getListenerArray() {
        return plugin.getListenerArrays().getSmeltItemQuests();
    }
}
