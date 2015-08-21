package pl.za.xvacuum.guilds.managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import pl.za.xvacuum.guilds.objects.Region;

public class RegionManager {
	
	private static List<Region> regions = new ArrayList<Region>();
	
	public static List<Region> get(){
		return regions;
	}
	
	public static void add(Region r){
		if(!regions.contains(r)) regions.add(r);
	}
	
	public static void remove(Region r){
		if(regions.contains(r)) regions.remove(r);
	}
	
	public static boolean isIn(Location c){
		for(Region r : get()){
			if(r.isIn(c)) return true;
		}
		return false;
	}
	
	public static Region inWhich(Location l){
		for(Region r : get()){
			if(r.isIn(l)) return r;
		}
		return null;
	}
	
	public static boolean isNear(Location c){
		for(Region r : get()){
			return r.isNear(c);
		}
		return false;
	}

}
