package me.trololo11.lifesplugin.listeners.lifeslisteners;

import me.trololo11.lifesplugin.Language;
import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.database.LanguageManager;
import me.trololo11.lifesplugin.events.LifesChangeEvent;
import me.trololo11.lifesplugin.tasks.SetItemTask;
import me.trololo11.lifesplugin.utils.PlayerStats;
import me.trololo11.lifesplugin.utils.PluginUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class ReviveUseListener implements Listener {

    private final LifesPlugin plugin = LifesPlugin.getPlugin();
    private Random random = new Random();

    @EventHandler
    public void onReviveUse(PlayerInteractEvent e) throws SQLException {
        Player playerReviveUse = e.getPlayer();
        if(e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(e.getHand() != EquipmentSlot.HAND) return;
        if(playerReviveUse.getInventory().getItemInMainHand().getType() == Material.AIR) return;
        if(!playerReviveUse.getInventory().getItemInMainHand().hasItemMeta()) return;
        if(!playerReviveUse.getInventory().getItemInMainHand().getItemMeta().hasCustomModelData()) return;
        if(playerReviveUse.getInventory().getItemInMainHand().getType() != Material.SCUTE) return;
        if(playerReviveUse.getInventory().getItemInMainHand().getItemMeta().getCustomModelData() != 8760005) return;

        ArrayList<OfflinePlayer> deadPlayers = plugin.getDeadPlayers();
        //if no one is dead then the revive card can't be used
        if(deadPlayers.size() == 0){
            playerReviveUse.sendMessage(ChatColor.RED + LanguageManager.getLangLine("revive-card-error", plugin.getPlayerLanguage(playerReviveUse)));
            return;
        }


        //gets a random dead player from the dead player list
        OfflinePlayer randomDeadPlayer = deadPlayers.get(random.nextInt(deadPlayers.size()));

        //adds the revive player stats
        PlayerStats playerStatsUsePlayer = plugin.getPlayerStats(playerReviveUse);

        playerStatsUsePlayer.setRevivedSomeone(playerStatsUsePlayer.getRevivedSomeone()+1);

        plugin.setPlayerStats(playerReviveUse, playerStatsUsePlayer);

        if(randomDeadPlayer.isOnline()){
            Player p = randomDeadPlayer.getPlayer();
            assert p != null;

            //to revive someone we kill them and put them to survival

            //and no that revived players have only 2 lifes when they revive is not a bug
            //i put them to 3 bcs they die and instantly decreases to 2 lifes
            Bukkit.getServer().getPluginManager().callEvent(new LifesChangeEvent(p, 3));
            p.setHealth(0);
            p.setGameMode(GameMode.SURVIVAL);
            //so players have different languages so we have to go thru all of them and pick the appropriate message
            for(Player player : Bukkit.getOnlinePlayers()){
                player.sendMessage(PluginUtils.chat(LanguageManager.getLangLine("revive-someone-message", plugin.getPlayerLanguage(player))
                        .replace("<player>", "&e&l"+p.getName().toUpperCase()+"&6&l") ));
            }

            //adds the being revived stat
            PlayerStats playerStats = plugin.getPlayerStats(p);

            playerStats.setBeenRevived(playerStats.getBeenRevived()+1);

            plugin.setPlayerStats(p, playerStats);


        }else{
            //so players have different languages so we have to go thru all of them and pick the appropriate message
            for(Player player : Bukkit.getOnlinePlayers()){
                player.sendMessage(PluginUtils.chat(LanguageManager.getLangLine("revive-someone-message", plugin.getPlayerLanguage(player))
                        .replace("<player>", "&e&l"+randomDeadPlayer.getName().toUpperCase()+"&6&l") ));
                player.sendMessage(ChatColor.GOLD + LanguageManager.getLangLine("revive-someone-not-online-extra-message", plugin.getPlayerLanguage(player)));
            }

            //when the player is offline we just save to the database that they need to be properly revived on join

            plugin.removeDeadPlayer(randomDeadPlayer);
            plugin.getLifesDatabase().updatePlayerIsRevived(randomDeadPlayer.getUniqueId().toString(), true);


        }

        playerReviveUse.playSound(playerReviveUse.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
        //makes a seperate task for clearing the revive card item bcs you cannot modify inventories in this event
        SetItemTask setItemTask = new SetItemTask(playerReviveUse, new ItemStack(Material.AIR), playerReviveUse.getInventory().getHeldItemSlot());
        setItemTask.runTaskLater(plugin, 1L);


    }
}
