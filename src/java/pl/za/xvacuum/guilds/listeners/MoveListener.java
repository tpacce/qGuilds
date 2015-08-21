package pl.za.xvacuum.guilds.listeners;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import pl.za.xvacuum.guilds.managers.GuildManager;
import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.objects.Guild;
import pl.za.xvacuum.guilds.objects.User;
import pl.za.xvacuum.guilds.utils.Util;


public class MoveListener implements Listener{
	
	private static final HashMap<Guild, Long> times = new HashMap<>();

	@EventHandler(priority = EventPriority.MONITOR)
	public void onMove(PlayerMoveEvent e){
		final Player p = e.getPlayer();
		final Location nfrom = e.getFrom();
		final Location nto = e.getTo();
		
		if(nfrom == null || nto == null) return;
		if(nfrom.getBlockX() == nto.getBlockX() && nfrom.getBlockZ() == nto.getBlockZ()) return;
		
		if(Util.list.contains(p)){
			Util.list.remove(p);
			Util.sendMessage(p, Messages.TeleportCancelled);
		}
        Guild from = GuildManager.byLocation(e.getFrom());
        Guild to = GuildManager.byLocation(e.getTo());
       

        if ((from == null) && (to != null)) {
        	Util.sendMessage(p, Messages.RegionEnter.replace("%tag%", to.getTag()));
            if(to.getMembers().contains(User.get(e.getPlayer()))) return;
            Long time = times.get(to);
            if (time == null || System.currentTimeMillis() - time >= TimeUnit.MILLISECONDS.toSeconds(30)) {
                for(User m : to.getMembers()) {
                	Util.sendMessage(m.getPlayer(), Messages.RegionIntruder.replace("%intruder%", e.getPlayer().getName()));
                }
                times.put(to, System.currentTimeMillis());
            }
        }
        else if ((from != null) && (to == null)) {
        	Util.sendMessage(p, Messages.RegionLeave.replace("%tag%", from.getTag()));
        }
	}
}
