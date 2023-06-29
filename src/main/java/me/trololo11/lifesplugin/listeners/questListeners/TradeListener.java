package me.trololo11.lifesplugin.listeners.questListeners;

import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.utils.QuestListener;
import me.trololo11.lifesplugin.utils.questUtils.QuestClass;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.TradeSelectEvent;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.sql.SQLException;
import java.util.ArrayList;

public class TradeListener extends QuestListener {

    private LifesPlugin plugin = LifesPlugin.getPlugin();



    @EventHandler
    public void onTrade(TradeSelectEvent e) throws SQLException {
        Player p = (Player) e.getWhoClicked();

        if (!(e.getMerchant().getRecipe(e.getIndex()).getResult().getItemMeta() instanceof EnchantmentStorageMeta))
            return;

        EnchantmentStorageMeta villagerItemMeta = (EnchantmentStorageMeta) e.getMerchant().getRecipe(e.getIndex()).getResult().getItemMeta();

        for (QuestClass quest : getListenerArray()) { //so if you are here i wouldn't reccomend to have much villager trading tasks
            //NVM I WAS DUMB IT WAS SO MUCH EASIER HAVE THEM HOW MANY YOU WANT :D

            if ((villagerItemMeta.getStoredEnchants().containsKey(quest.getQuestTarget()) || quest.getQuestTarget() == null) && !quest.hasCompleted(p)) {
                quest.setProgress(p, quest.getProgress(p) + 1);
            }

        }

    }

    @Override
    public ArrayList<QuestClass> getListenerArray() {
        return plugin.getListenerArrays().getTradeQuests();
    }
}
