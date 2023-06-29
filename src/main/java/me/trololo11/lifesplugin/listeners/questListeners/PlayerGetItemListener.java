package me.trololo11.lifesplugin.listeners.questListeners;

import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.utils.QuestListener;
import me.trololo11.lifesplugin.utils.questUtils.QuestClass;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.sql.SQLException;
import java.util.ArrayList;

public class PlayerGetItemListener extends QuestListener {

    private LifesPlugin plugin = LifesPlugin.getPlugin();

    @EventHandler
    public void onPickup(EntityPickupItemEvent e) throws SQLException {
        if(e.getEntity() instanceof Player p) checkTarget(e.getItem().getItemStack().getType(), p);
    }

    @EventHandler
    public void onInventoryGetting(InventoryClickEvent e) throws SQLException {
        if(e.isCancelled()) return;
        if(e.getCurrentItem() == null) return;
        if(e.getWhoClicked() instanceof Player p) checkTarget(e.getCurrentItem().getType(), p);
    }

    @Override
    public ArrayList<QuestClass> getListenerArray() {
        return plugin.getListenerArrays().getGetItemQuests();
    }
}
