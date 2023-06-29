package me.trololo11.lifesplugin.tasks;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class SetItemTask extends BukkitRunnable {

    private Player p;
    private ItemStack item;
    private int slot;

    public SetItemTask(Player p, ItemStack item, int slot){
        this.p = p;
        this.item = item;
        this.slot = slot;
    }

    @Override
    public void run() {

        p.getInventory().setItem(slot, item);

    }
}
