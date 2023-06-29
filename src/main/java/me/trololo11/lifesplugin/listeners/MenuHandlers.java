package me.trololo11.lifesplugin.listeners;

import me.trololo11.lifesplugin.utils.Menu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class MenuHandlers implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        InventoryHolder holder = e.getInventory().getHolder();

        if (holder instanceof Menu) {

            e.setCancelled(true);

            ItemStack item = e.getCurrentItem();

            if (item == null) return;
            if (!item.hasItemMeta()) return;
            if (!item.getItemMeta().hasLocalizedName()) return;

            Menu menu = (Menu) holder;
            menu.handleMenu(e);

        }
    }
}
