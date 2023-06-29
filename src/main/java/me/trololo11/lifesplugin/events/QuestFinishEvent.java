package me.trololo11.lifesplugin.events;

import me.trololo11.lifesplugin.utils.questUtils.QuestClass;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class QuestFinishEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private QuestClass quest;
    private Player player;

    public QuestFinishEvent(QuestClass quest, Player player){
        this.player = player;
        this.quest = quest;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    public QuestClass getQuest() {
        return quest;
    }

    public Player getPlayer() {
        return player;
    }
}
