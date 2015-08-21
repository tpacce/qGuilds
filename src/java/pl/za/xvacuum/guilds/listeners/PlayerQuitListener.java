package pl.za.xvacuum.guilds.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import pl.za.xvacuum.guilds.antylogout.LogoutSystem;
import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.objects.Guild;
import pl.za.xvacuum.guilds.objects.User;
import pl.za.xvacuum.guilds.utils.Util;

public class PlayerQuitListener implements Listener{
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		User u = User.get(e.getPlayer());
		if(u.hasGuild()) {
			Guild g = u.getGuild();
			for(User m : g.getMembers())  {
				Util.sendMessage(m.getPlayer(), Messages.EventPlayerQuitNotify.replace("%player%", e.getPlayer().getName()));
			}
 		}
		Player p = e.getPlayer();
		if(LogoutSystem.getTimes().contains(p)) {
			p.setHealth(0.0);
			if(LogoutSystem.getLastAttacker(p) != null) {
				Player lastAttacker = LogoutSystem.getLastAttacker(p);
				User lastAtt = User.get(lastAttacker);
				LogoutSystem.notOnline(p);
				Bukkit.broadcastMessage(Util.setHEX(Messages.AntyLogoutBroadcast.replace("%player%", p.getName()).replace("%playerTag%", u.getGuild() == null ? "" : "[" + u.getGuild().getTag().toString() + "] ")));
				Bukkit.broadcastMessage(Util.setHEX(Messages.AntyLogoutBroadcastLastAttacker.replace("%player%", lastAttacker.getName()).replace("%playerTag%", lastAtt.getGuild() == null ? "" : "[" + lastAtt.getGuild().getTag().toString() + "] ")));
				return;
			}
			
			LogoutSystem.notOnline(p);
			Bukkit.broadcastMessage(Util.setHEX(Messages.AntyLogoutBroadcast.replace("%player%", p.getName()).replace("%playerTag%", u.getGuild() == null ? "" : "[" + u.getGuild().getTag().toString() + "] ")));
		}
		return;
	}

}
