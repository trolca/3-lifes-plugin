package me.trololo11.lifesplugin.listeners.questListeners;

import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.utils.QuestListener;
import me.trololo11.lifesplugin.utils.questUtils.QuestClass;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmithingInventory;

import java.sql.SQLException;
import java.util.ArrayList;

public class SmithingUseListener extends QuestListener {

    private LifesPlugin plugin = LifesPlugin.getPlugin();

    @EventHandler
    public void onSmith(InventoryClickEvent e) throws SQLException {
        if(e.getClick() == ClickType.MIDDLE) return;
        if(e.getClickedInventory() == null) return;
        if(e.getClickedInventory().getType() != InventoryType.SMITHING_NEW) return;
        if(e.getClick() == ClickType.NUMBER_KEY){
            e.setCancelled(true);
            return;
        }
        if(!(e.getWhoClicked() instanceof Player)) return;
        if(e.getSlot() != 3) return;
        if(e.getClickedInventory().getItem(e.getSlot()) == null || e.getCurrentItem().getType() == Material.AIR) return;
        ItemStack item = e.getClickedInventory().getItem(0);


        Player player = (Player) e.getWhoClicked();

        checkTarget(item.getType(), player);

    }


    @Override
    public ArrayList<QuestClass> getListenerArray() {
        return plugin.getListenerArrays().getSmithingUseQuests();
    }
}
