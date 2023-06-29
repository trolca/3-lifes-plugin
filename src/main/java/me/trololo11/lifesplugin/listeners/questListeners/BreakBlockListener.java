package me.trololo11.lifesplugin.listeners.questListeners;

import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.utils.QuestListener;
import me.trololo11.lifesplugin.utils.questUtils.QuestClass;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import java.sql.SQLException;
import java.util.ArrayList;

public class BreakBlockListener extends QuestListener {

    private LifesPlugin plugin = LifesPlugin.getPlugin();

    @EventHandler
    public void onBreak(BlockBreakEvent e) throws SQLException {
        Player p = e.getPlayer();

        checkTarget(e.getBlock().getType(), p);
    }

    @Override
    public ArrayList<QuestClass> getListenerArray() {
        return plugin.getListenerArrays().getBreakBlocksQuests();
    }
}
