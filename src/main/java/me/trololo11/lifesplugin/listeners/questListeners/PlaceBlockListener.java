package me.trololo11.lifesplugin.listeners.questListeners;

import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.utils.QuestListener;
import me.trololo11.lifesplugin.utils.questUtils.QuestClass;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

import java.sql.SQLException;
import java.util.ArrayList;

public class PlaceBlockListener extends QuestListener {

    private LifesPlugin plugin = LifesPlugin.getPlugin();

    @EventHandler
    public void onPlace(BlockPlaceEvent e) throws SQLException {
        checkTarget(e.getBlock().getType(), e.getPlayer());
    }

    @Override
    public ArrayList<QuestClass> getListenerArray() {
        return plugin.getListenerArrays().getPlaceBlocksQuests();
    }
}
