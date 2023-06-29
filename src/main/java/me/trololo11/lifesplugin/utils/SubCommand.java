package me.trololo11.lifesplugin.utils;

import org.bukkit.entity.Player;

public abstract class SubCommand {

    public abstract String getName();

    public abstract void perform(Player p);

}
