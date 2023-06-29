package me.trololo11.lifesplugin.commands.subcommands;

import me.trololo11.lifesplugin.utils.Menus;
import me.trololo11.lifesplugin.utils.SubCommand;
import me.trololo11.lifesplugin.utils.questTypes.QuestType;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class DailyQuestSubcommand extends SubCommand {



    @Override
    public String getName() {
        return "daily";
    }

    @Override
    public void perform(Player p) {
        Menus.getQuestMenu(QuestType.DAILY).open(p);
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f);

    }
}
