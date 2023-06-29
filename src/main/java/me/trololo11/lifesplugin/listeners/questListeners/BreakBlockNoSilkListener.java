package me.trololo11.lifesplugin.listeners.questListeners;

import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.utils.QuestListener;
import me.trololo11.lifesplugin.utils.questUtils.QuestClass;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.ArrayList;

public class BreakBlockNoSilkListener extends QuestListener {

    private LifesPlugin plugin = LifesPlugin.getPlugin();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) throws SQLException {
        Player p = e.getPlayer();

        ItemStack breakTool = p.getInventory().getItemInMainHand();
        if(breakTool.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) return;
        checkTarget(e.getBlock().getType(), p);
    }


    @Override
    public ArrayList<QuestClass> getListenerArray() {
        return plugin.getListenerArrays().getBreakBlocksNoSilkQuests();
    }
}
