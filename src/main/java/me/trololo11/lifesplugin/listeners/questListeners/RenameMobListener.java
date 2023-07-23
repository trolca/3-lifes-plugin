package me.trololo11.lifesplugin.listeners.questListeners;

import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.utils.QuestListener;
import me.trololo11.lifesplugin.utils.questUtils.QuestClass;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.ArrayList;

public class RenameMobListener extends QuestListener {

    private LifesPlugin plugin = LifesPlugin.getPlugin();

    @EventHandler
    public void onRename(PlayerInteractEntityEvent e) throws SQLException {
        Player player = e.getPlayer();
        if(player.getInventory().getItemInMainHand().getType() != Material.NAME_TAG) return;
        ItemStack item = player.getInventory().getItemInMainHand();
        if(item.getItemMeta().getDisplayName().equals(new ItemStack(Material.NAME_TAG).getItemMeta().getDisplayName())) return;
        System.out.println(item.getItemMeta().getDisplayName());


        customCheckTarget(item.getItemMeta().getDisplayName(), player);

    }

    private void customCheckTarget(String name, Player p) throws SQLException {

        for (QuestClass quest : getListenerArray()) {
            if ((quest.getQuestTarget() == null || quest.getQuestTarget().equals(name.toUpperCase())) && !quest.hasCompleted(p) && !plugin.getDeadPlayers().contains(p)) {

                quest.setProgress(p, quest.getProgress(p) + 1);

            }
        }

    }
    @Override
    public ArrayList<QuestClass> getListenerArray() {
        return plugin.getListenerArrays().getRenameMobQuests();
    }
}
