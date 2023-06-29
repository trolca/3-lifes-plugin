package me.trololo11.lifesplugin.listeners.lifeslisteners;

import me.trololo11.lifesplugin.LifesPlugin;
import me.trololo11.lifesplugin.events.LifesChangeEvent;
import me.trololo11.lifesplugin.utils.TeamsManager;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.sql.SQLException;

public class ChangeLifesListener implements Listener {

    private TeamsManager teamsManager;
    private LifesPlugin plugin = LifesPlugin.getPlugin();

    public ChangeLifesListener(TeamsManager teamsManager) {
        this.teamsManager = teamsManager;
    }


    @EventHandler
    public void onChange(LifesChangeEvent e) throws SQLException {

        int lifes = e.getNewLifes();
        Player p = e.getPlayer();

        //updates the lifes in the hashMap
        plugin.updateLifes(p.getUniqueId().toString(), lifes);

        //if the new plyer lifes is 0 or less then it means that they are dead and makes them dead and add them to the arrayList
        if (lifes <= 0) {
            p.setGameMode(GameMode.SPECTATOR);
            plugin.addDeadPlayer(p);
        }

        //if they have more than 0 lifes and they are dead it removes them from dead list
        if (lifes > 0 && plugin.getDeadPlayers().contains(p))
            plugin.removeDeadPlayer(p);

        //adds player to the according team
        if (lifes <= 3) teamsManager.getLifesTeamList().get(lifes).addEntry(p.getName());
        else teamsManager.getLifesTeamList().get(4).addEntry(p.getName());

    }
}
