package me.trololo11.lifesplugin.tasks;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class LifesStackCheckTask extends BukkitRunnable {

    private Player p;

    public LifesStackCheckTask( Player p){
        this.p = p;
    }

    @Override
    public void run() {

        //check if theres a life item which is more than 1 and usnstacks it
        //(to unstack it it creats an random uuid in the localized name)
        for(ItemStack item : p.getInventory().getContents()  ) {

            if (item != null && item.hasItemMeta() && item.getItemMeta().hasLocalizedName()
                    && item.getItemMeta().getLocalizedName().equalsIgnoreCase("life-default-string") && item.getAmount() > 1) {

                p.getInventory().remove(item);

                for (int i = 0; i < item.getAmount(); i++) {
                    ItemStack item1 = item.clone();
                    item1.setAmount(1);

                    ItemMeta itemMeta = item1.getItemMeta();
                    itemMeta.setLocalizedName(UUID.randomUUID().toString());
                    item1.setItemMeta(itemMeta);
                    p.getInventory().addItem(item1);
                }


            }
        }
    }

}
