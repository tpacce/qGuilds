package pl.za.xvacuum.guilds.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import pl.za.xvacuum.guilds.managers.RankManager;
import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.objects.User;
import pl.za.xvacuum.guilds.utils.Util;

public class PlayerDeathListener implements Listener{
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Entity entVictim = e.getEntity();
		Entity entDamager = e.getEntity().getKiller();
		
		if(!(entVictim instanceof Player)) {
			return;
		}
		if(!(entDamager instanceof Player)) {
			return;
		}
		
		Player playerVictim = (Player)entVictim;
		Player playerAttacker = (Player)entDamager;
		
		User victim = User.get(playerVictim);
		User killer = User.get(playerAttacker);
		
		int points = RankManager.calc(playerVictim, playerAttacker);
		
		e.setDeathMessage(Util.setHEX(Messages.EventDeathFormat
				.replace("%victimTag%", victim.getGuild() == null ? "" : "[" + victim.getGuild().getTag().toString() + "] ")
				.replace("%killerTag%", killer.getGuild() == null ? "" : "[" + killer.getGuild().getTag().toString() + "] ")
				
				.replace("%points%", String.valueOf(points))
				
				.replace("%victimName%", victim.getName())
				.replace("%killerName%", killer.getName())
				));
	}

}
