package pl.za.xvacuum.guilds.utils.reflections;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import pl.za.xvacuum.guilds.misc.Tablist;

public class TabUtils {
	
	public static void broadcastTab()
	{
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(Tablist.tablistEnabled)TabReflection.send(p);
		}
		return;
	}
	
	public static void sendTab(Player player)
	{
		if(Tablist.tablistEnabled)TabReflection.send(player);
		return;
	}


}
