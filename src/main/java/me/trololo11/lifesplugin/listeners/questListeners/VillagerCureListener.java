package me.trololo11.lifesplugin.listeners.questListeners;

import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.utils.QuestListener;
import me.trololo11.lifesplugin.utils.questUtils.QuestClass;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.potion.PotionEffectType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class VillagerCureListener extends QuestListener {

    private LifesPlugin plugin = LifesPlugin.getPlugin();
    private HashMap<ZombieVillager, OfflinePlayer> playerZombiesStartCure = new HashMap<>();
    private ArrayList<ZombieVillager> allZombieVillagers = new ArrayList<>();

    @EventHandler
    public void onTransform(EntityTransformEvent e) throws SQLException {
        if(e.getEntity().getType() != EntityType.ZOMBIE_VILLAGER) return;
        if(e.getTransformReason() != EntityTransformEvent.TransformReason.CURED) return;
        ZombieVillager zombieVillager = (ZombieVillager) e.getEntity();
        if(!allZombieVillagers.contains(zombieVillager)) return;

        checkTarget(null, playerZombiesStartCure.get(zombieVillager));
        allZombieVillagers.remove(zombieVillager);
        playerZombiesStartCure.remove(zombieVillager);


    }

    @EventHandler
    public void onKill(EntityDeathEvent e){
        if(e.getEntity().getType() != EntityType.ZOMBIE_VILLAGER) return;
        ZombieVillager zombieVillager = (ZombieVillager) e.getEntity();
        if(!allZombieVillagers.contains(zombieVillager)) return;

        allZombieVillagers.remove(zombieVillager);
        playerZombiesStartCure.remove(zombieVillager);
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e){

        if(e.getRightClicked().getType() != EntityType.ZOMBIE_VILLAGER) return;
        ZombieVillager entity = (ZombieVillager) e.getRightClicked();
        if(!entity.hasPotionEffect(PotionEffectType.WEAKNESS)) return;

        Player player = e.getPlayer();

        playerZombiesStartCure.put(entity, player);
        allZombieVillagers.add(entity);

        playerZombiesStartCure.put(entity, player);


    }

    @Override
    public ArrayList<QuestClass> getListenerArray() {
        return plugin.getListenerArrays().getVillagerCureQuests();
    }
}
