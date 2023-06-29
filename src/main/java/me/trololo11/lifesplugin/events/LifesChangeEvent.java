package me.trololo11.lifesplugin.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LifesChangeEvent extends Event {

    //events i dont have to explain do I?

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private Player player;
    private int newLifes;

    public LifesChangeEvent(Player player, int newLifes) {
        this.player = player;
        this.newLifes = newLifes;
    }

    public Player getPlayer() {
        return player;
    }

    public int getNewLifes() {
        return newLifes;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }


}
