package me.trololo11.lifesplugin.commands.subcommands;

import me.trololo11.lifesplugin.utils.Menus;
import me.trololo11.lifesplugin.utils.SubCommand;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class StatsQuestSubcommand extends SubCommand {
    @Override
    public String getName() {
        return "stats";
    }

    @Override
    public void perform(Player p) {
        Menus.getStatsMenu().open(p);
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f);
    }
}
