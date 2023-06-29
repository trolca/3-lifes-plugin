package me.trololo11.lifesplugin.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class AnvilRenameFix implements Listener {

    @EventHandler
    public void onRename(InventoryClickEvent e){

        if(e.getClickedInventory() == null) return;
        if(e.getCurrentItem() == null) return;
        if(!e.getCurrentItem().hasItemMeta()) return;
        Player p = (Player) e.getWhoClicked();

        if(e.getClickedInventory().getType() == InventoryType.ANVIL){

            if(e.getSlot() == 2 & e.getCurrentItem().getItemMeta().hasCustomModelData()) {
                p.sendMessage(ChatColor.RED + "You cannot rename custom items!");
                e.setCancelled(true);
            }

        }
    }
}
