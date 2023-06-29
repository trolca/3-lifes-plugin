package me.trololo11.lifesplugin.listeners.lifeslisteners;

import me.trololo11.lifesplugin.Language;
import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.database.LanguageManager;
import me.trololo11.lifesplugin.events.LifesChangeEvent;
import me.trololo11.lifesplugin.tasks.SetItemTask;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class AddLifeListener implements Listener {

    LifesPlugin plugin = LifesPlugin.getPlugin();

    @EventHandler
    public void onUse(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if(e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(e.getHand() != EquipmentSlot.HAND) return;
        if(p.getInventory().getItemInMainHand().getType() == Material.AIR) return;
        if(!p.getInventory().getItemInMainHand().hasItemMeta()) return;
        if(!p.getInventory().getItemInMainHand().getItemMeta().hasCustomModelData()) return;
        if(p.getInventory().getItemInMainHand().getType() != Material.SCUTE) return;
         if(p.getInventory().getItemInMainHand().getItemMeta().getCustomModelData() != 8760001 &&
                p.getInventory().getItemInMainHand().getItemMeta().getCustomModelData() != 8760002) return;

        Language language = plugin.getPlayerLanguage(p);

        String uuid = p.getUniqueId().toString();
        //if player has 3 lifes or more it returns an error bcs player can have more than 3 lifes
        if(plugin.getLifes(uuid) >= 3){
            p.sendMessage(ChatColor.RED + LanguageManager.getLangLine("addlife-command-error", language));
            return;
        }

        //calls a lifes change event to update it the server
        plugin.getServer().getPluginManager().callEvent(new LifesChangeEvent(p, plugin.getLifes(uuid)+1));

        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
        p.sendMessage(ChatColor.GREEN + LanguageManager.getLangLine("addlife-success", language));

        //makes a seperate task for clearing the lifes item bcs you cannot modify inventories in this event
        SetItemTask setItemTask = new SetItemTask(p, new ItemStack(Material.AIR), p.getInventory().getHeldItemSlot());
        setItemTask.runTaskLater(plugin, 1L);

    }
}
