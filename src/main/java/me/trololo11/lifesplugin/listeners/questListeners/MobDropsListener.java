package me.trololo11.lifesplugin.listeners.questListeners;

import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.utils.QuestListener;
import me.trololo11.lifesplugin.utils.questUtils.QuestClass;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import javax.swing.text.html.parser.Entity;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MobDropsListener extends QuestListener {
    private LifesPlugin plugin = LifesPlugin.getPlugin();
    @EventHandler
    public void onKill(EntityDeathEvent e) throws SQLException {
        LivingEntity entity = e.getEntity();
        if(entity.getKiller() == null) return;
        Player player = entity.getKiller();

        customCheckTarget(e.getDrops(), player);
    }
    private void customCheckTarget(List<ItemStack> drops, OfflinePlayer player) throws SQLException {
        for (QuestClass quest : getListenerArray()) {
            ItemStack itemStack = checkIfHasItem(drops, quest);
            if ((itemStack != null || quest.getQuestTarget() == null) && !quest.hasCompleted(player) && !plugin.getDeadPlayers().contains(player)) {

                quest.setProgress(player, quest.getProgress(player) + itemStack.getAmount());

            }
        }
    }

    private ItemStack checkIfHasItem(List<ItemStack> drops, QuestClass quest){

        for(ItemStack drop : drops){
            if(drop.getType().equals(quest.getQuestTarget())) return drop;
        }

        return null;

    }


    @Override
    public ArrayList<QuestClass> getListenerArray() {
        return plugin.getListenerArrays().getItemFromMobQuests();
    }
}
