package me.trololo11.lifesplugin.listeners.questListeners;

import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.utils.QuestListener;
import me.trololo11.lifesplugin.utils.questUtils.QuestClass;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;

import java.sql.SQLException;
import java.util.ArrayList;

public class MoveListeners extends QuestListener {

    private LifesPlugin plugin = LifesPlugin.getPlugin();

    @EventHandler
    public void onWalk(PlayerMoveEvent e) throws SQLException {
        Player p = e.getPlayer();
        if (e.getTo() == null) return;
        if(e.getPlayer().isInsideVehicle()) return;
        if(e.getPlayer().isGliding()) return;

        setQuestsProgress(getListenerArray(), p, getDistance(e.getFrom(), e.getTo()));

    }

    @EventHandler
    public void onFly(PlayerMoveEvent e) throws SQLException {
        if (!e.getPlayer().isGliding()) return;
        Player p = e.getPlayer();
        if (e.getTo() == null) return;

        setQuestsProgress(plugin.getListenerArrays().getFlyQuests(), p, getDistance(e.getFrom(), e.getTo()));


    }

    @EventHandler
    public void onRide(PlayerMoveEvent e) throws SQLException {
        if (!e.getPlayer().isInsideVehicle()) return;
        Player p = e.getPlayer();
        if(p.getVehicle() == null) return;
        if (e.getTo() == null) return;

        setQuestsProgress(plugin.getListenerArrays().getRideQuests(), p, getDistance(e.getFrom(), e.getTo()), p.getVehicle().getType());

    }

    @EventHandler
    public void onMinecartRide(VehicleMoveEvent e) throws SQLException {
        if(e.getVehicle().getType() != EntityType.MINECART) return;
        if(e.getVehicle().getPassengers().isEmpty()) return;

        for(Entity entity : e.getVehicle().getPassengers()){

            if(entity.getType() == EntityType.PLAYER){
                Player player = (Player) entity;
                setQuestsProgress(plugin.getListenerArrays().getMinecartMoveQuests(), player, getDistance(e.getFrom(), e.getTo()));
            }

        }
    }

    @EventHandler
    public void onSwim(PlayerMoveEvent e) throws SQLException {
        if (!e.getPlayer().isSwimming()) return;
        Player p = e.getPlayer();
        if (e.getTo() == null) return;

        setQuestsProgress(plugin.getListenerArrays().getSwimBlocks(), p, getDistance(e.getFrom(), e.getTo()));
    }

    public int getDistance(Location from, Location to) {
        return Math.abs((from.getBlockX() - to.getBlockX()) + (from.getBlockZ() - to.getBlockZ()));
    }

    private void setQuestsProgress(ArrayList<QuestClass> questsList, Player p, int distance) throws SQLException {
        if (distance == 0) return;
        for (QuestClass quest : questsList) {


            if (!quest.hasCompleted(p) && !plugin.getDeadPlayers().contains(p)) {

                quest.setProgress(p, quest.getProgress(p) + distance);

            }
        }
    }

    private void setQuestsProgress(ArrayList<QuestClass> questsList, Player p, int distance, EntityType target) throws SQLException {
        if (distance == 0) return;
        for (QuestClass quest : questsList) {


            if (!quest.hasCompleted(p) && (target == quest.getQuestTarget() || (quest.getQuestTarget() == EntityType.BOAT && target == EntityType.CHEST_BOAT) ) && !plugin.getDeadPlayers().contains(p)) {

                quest.setProgress(p, quest.getProgress(p) + distance);

            }
        }
    }

    @Override
    public ArrayList<QuestClass> getListenerArray() {
        return plugin.getListenerArrays().getWalkQuests();
    }
}
