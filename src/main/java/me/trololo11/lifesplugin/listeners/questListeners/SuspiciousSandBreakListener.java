package me.trololo11.lifesplugin.listeners.questListeners;

import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.utils.QuestListener;
import me.trololo11.lifesplugin.utils.questUtils.QuestClass;
import org.bukkit.GameEvent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.GenericGameEvent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class SuspiciousSandBreakListener extends QuestListener {

    private HashMap<Player, Boolean> isInteracting = new HashMap<>();
    private HashMap<Player, ArrayList<Block>> blockSeen = new HashMap<>();
    private LifesPlugin plugin = LifesPlugin.getPlugin();

    @EventHandler
    public void onEvent(GenericGameEvent e) throws SQLException {
        if(!(e.getEntity() instanceof Player)) return;
        GameEvent gameEvent = e.getEvent();
        Player player = (Player) e.getEntity();

        if(gameEvent == GameEvent.ITEM_INTERACT_START){
            Block playerBlock = player.getTargetBlockExact(5);
            isInteracting.put(player, true);

            if(playerBlock != null && (playerBlock.getType() == Material.SUSPICIOUS_SAND || playerBlock.getType() == Material.SUSPICIOUS_GRAVEL) ){
                addIfDoesntContain(playerBlock, player);
            }

        }else if(gameEvent == GameEvent.ITEM_INTERACT_FINISH){

            isInteracting.put(player, false);

            ArrayList<Block> blocks = blockSeen.get(player);

            if(blocks == null) return;

            for(Block block : blocks){
                if(block.getType() == Material.SAND || block.getType() == Material.GRAVEL){
                   checkTarget(null, player);
                }
            }

            blockSeen.remove(player);

        }

    }

    @EventHandler
    public void onLook(PlayerMoveEvent e){
        Player player = e.getPlayer();
        if(!isInteracting.getOrDefault(player, false)) return;

        Block playerBlock = player.getTargetBlockExact(5);

        if(playerBlock != null && (playerBlock.getType() == Material.SUSPICIOUS_SAND || playerBlock.getType() == Material.SUSPICIOUS_GRAVEL) ){
            addIfDoesntContain(playerBlock, player);
        }

    }

    private void addIfDoesntContain(Block block, Player player){

        ArrayList<Block> blocks = blockSeen.get(player);
        if(blocks == null) blocks = new ArrayList<>();

        for(Block block1 : blocks){
            if(block1.getLocation().equals(block.getLocation())) return;
        }


        blocks.add(block);
        blockSeen.put(player, blocks);

    }

    @Override
    public ArrayList<QuestClass> getListenerArray() {
        return plugin.getListenerArrays().getSusBlocksBreak();
    }
}
