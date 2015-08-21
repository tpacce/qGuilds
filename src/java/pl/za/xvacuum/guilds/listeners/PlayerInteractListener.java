package pl.za.xvacuum.guilds.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import pl.za.xvacuum.guilds.managers.RegionManager;
import pl.za.xvacuum.guilds.objects.Region;
import pl.za.xvacuum.guilds.war.War;

public class PlayerInteractListener implements Listener{
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		Action a = e.getAction();
		if(a.equals(Action.LEFT_CLICK_BLOCK) || (a.equals(Action.RIGHT_CLICK_BLOCK))) {
			if(!e.getClickedBlock().getType().equals(Material.DRAGON_EGG)) {
				return;
			}
			Region r = RegionManager.inWhich(e.getClickedBlock().getLocation());
			//if(!e.getClickedBlock().getLocation().equals(r.getCenter())) {
			//	return;
			//}
			e.setCancelled(true);
			War.attackGuild(p, r.getGuild(), e.getClickedBlock());
			return;
			
		}
		return;
	}

}
