package pl.za.xvacuum.guilds.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationManager {
	
	public static Location fromString(String s){
		String[] ss = s.split(" ");
		return new Location(Bukkit.getWorld(ss[0]), Integer.parseInt(ss[1]), Integer.parseInt(ss[2]), Integer.parseInt(ss[3]));
	}
	
	public static String fromLocation(Location l){
		return new String(l.getWorld().getName() + " " + l.getBlockX() + " " + l.getBlockY() + " " + l.getBlockZ());
	}

}
