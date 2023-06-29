package me.trololo11.lifesplugin.listeners.questListeners;

import me.trololo11.lifesplugin.utils.QuestListener;
import me.trololo11.lifesplugin.utils.questUtils.QuestClass;
import org.bukkit.CropState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;

public class CropBreakListener extends QuestListener {

    @EventHandler
    public void onBreak(BlockBreakEvent e){

    }

    @Override
    public ArrayList<QuestClass> getListenerArray() {
        return null;
    }
}
