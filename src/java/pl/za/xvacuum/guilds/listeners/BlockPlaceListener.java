package pl.za.xvacuum.guilds.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import pl.za.xvacuum.guilds.managers.RegionManager;
import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.objects.Region;
import pl.za.xvacuum.guilds.objects.User;
import pl.za.xvacuum.guilds.utils.BuildUtils;
import pl.za.xvacuum.guilds.utils.ExplodeUtils;
import pl.za.xvacuum.guilds.utils.Util;

public class BlockPlaceListener implements Listener{
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		User u = User.get(p);
		
		Region in = RegionManager.inWhich(e.getBlock().getLocation());
		if (in == null) {
			e.setCancelled(false);
			return;
		}
		if(!u.hasGuild()) {
			e.setCancelled(true);
			Util.sendMessage(p, Messages.EventBlockPlace);
			return;
		}
		if(u.getGuild().getTag() == in.getGuild().getTag()) {
			for(Location l : BuildUtils.sphere(in.getCenter(), 4, 4, false, true, 0)) {
				if(e.getBlock().getLocation().equals(l)) {
					e.setCancelled(true);
					return;
				}
			}
			if(e.getBlock().getType().equals(Material.PISTON_BASE)) {
				e.setCancelled(true);
				return;
			}
			if(ExplodeUtils.isBlocked(u.getGuild())) {
				e.setCancelled(true);
				Util.sendMessage(p, Messages.EventBlockPlaceTNT.replace("%time%", String.valueOf(ExplodeUtils.timeRemain(u.getGuild()))));
				return;
			}
			e.setCancelled(false);
			return;
		}
		e.setCancelled(true);
		Util.sendMessage(p, Messages.EventBlockPlace);
		return;
	}

}