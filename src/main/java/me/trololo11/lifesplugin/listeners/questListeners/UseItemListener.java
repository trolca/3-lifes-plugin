package me.trololo11.lifesplugin.listeners.questListeners;

import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.utils.QuestListener;
import me.trololo11.lifesplugin.utils.questUtils.QuestClass;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.sql.SQLException;
import java.util.ArrayList;

public class UseItemListener extends QuestListener {

    private LifesPlugin plugin = LifesPlugin.getPlugin();

    @EventHandler
    public void onInteract(PlayerInteractEvent e) throws SQLException {
        if(e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player player = e.getPlayer();
        if(player.getInventory().getItemInMainHand().getType() == Material.AIR && player.getInventory().getItemInOffHand().getType() == Material.AIR) return;
        Material material = player.getInventory().getItemInMainHand().getType() == Material.AIR ? player.getInventory().getItemInOffHand().getType() : player.getInventory().getItemInMainHand().getType();

        checkTarget(material, player);

    }

    @Override
    public ArrayList<QuestClass> getListenerArray() {
        return plugin.getListenerArrays().getUseItemQuests();
    }
}
