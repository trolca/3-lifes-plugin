package me.trololo11.lifesplugin.listeners.questListeners;

import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.utils.QuestListener;
import me.trololo11.lifesplugin.utils.questUtils.QuestClass;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityBreedEvent;

import java.sql.SQLException;
import java.util.ArrayList;

public class BreedEntityListener extends QuestListener {

    private LifesPlugin plugin = LifesPlugin.getPlugin();

    @EventHandler
    public void onBreed(EntityBreedEvent e) throws SQLException {
        if(!(e.getBreeder() instanceof Player)) return;
        Player player = (Player) e.getBreeder();


        checkTarget(e.getEntity().getType(), player);
    }

    @Override
    public ArrayList<QuestClass> getListenerArray() {
        return plugin.getListenerArrays().getBreedEntity();
    }
}
