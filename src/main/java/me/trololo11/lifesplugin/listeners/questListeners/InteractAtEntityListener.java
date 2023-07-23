package me.trololo11.lifesplugin.listeners.questListeners;

import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.utils.QuestListener;
import me.trololo11.lifesplugin.utils.questUtils.QuestClass;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.sql.SQLException;
import java.util.ArrayList;

public class InteractAtEntityListener extends QuestListener {

    private LifesPlugin plugin = LifesPlugin.getPlugin();

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e) throws SQLException {
        Player player = e.getPlayer();

        checkTarget(e.getRightClicked().getType(), player);
    }

    @EventHandler
    public void onSnowFoxInteract(PlayerInteractEntityEvent e) throws SQLException {
        if(e.getRightClicked().getType() != EntityType.FOX) return;
        Fox fox = (Fox) e.getRightClicked();
        if(fox.getFoxType() != Fox.Type.SNOW) return;

        Player player = e.getPlayer();

        checkTarget(null, player, plugin.getListenerArrays().getSnowFoxClickQuests());
    }

    @EventHandler
    public void onCreeperLighterInteract(PlayerInteractEntityEvent e) throws SQLException {
        if(e.getRightClicked().getType() != EntityType.CREEPER) return;
        if(e.getPlayer().getInventory().getItemInMainHand().getType() != Material.FLINT_AND_STEEL) return;

        checkTarget(null, e.getPlayer(), plugin.getListenerArrays().getLighterCreeperQuests());
    }

    @Override
    public ArrayList<QuestClass> getListenerArray() {
        return plugin.getListenerArrays().getRightClickEntityQuests();
    }
}
