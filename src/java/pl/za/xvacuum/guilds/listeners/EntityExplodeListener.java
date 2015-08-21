package pl.za.xvacuum.guilds.listeners;

import java.util.Iterator;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import pl.za.xvacuum.guilds.managers.RegionManager;
import pl.za.xvacuum.guilds.objects.Region;
import pl.za.xvacuum.guilds.utils.ExplodeUtils;

public class EntityExplodeListener implements Listener{
	
	@EventHandler
	public void onTNTExplode(EntityExplodeEvent e) {
		Iterator<Block> it = e.blockList().iterator();
		while(it.hasNext()) {
			Block b = it.next();
			if(b.getType().equals(Material.DRAGON_EGG)) {
				it.remove();
			}
			Region in = RegionManager.inWhich(b.getLocation());
			if(in != null) {
				ExplodeUtils.setLastExplode(in.getGuild(), System.currentTimeMillis() + 1000 * 60);
				return;
			}
			
		}
	}

}
