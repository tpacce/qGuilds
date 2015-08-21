package pl.za.xvacuum.guilds.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import pl.za.xvacuum.guilds.managers.RegionManager;
import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.objects.Region;
import pl.za.xvacuum.guilds.objects.User;
import pl.za.xvacuum.guilds.utils.Util;

public class BlockBreakListener implements Listener{
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		User u = User.get(p);
		
		Region in = RegionManager.inWhich(e.getBlock().getLocation());
		if (in == null) {
			e.setCancelled(false);
			return;
		}
		if(!u.hasGuild()) {
			e.setCancelled(true);
			Util.sendMessage(p, Messages.EventBlockBreak);
			return;
		}
		if(u.getGuild().getTag() == in.getGuild().getTag()) {
			e.setCancelled(false);
			return;
		}
		e.setCancelled(true);
		Util.sendMessage(p, Messages.EventBlockBreak);
		return;
	}

}
