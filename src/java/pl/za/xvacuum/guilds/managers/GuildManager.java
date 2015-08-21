package pl.za.xvacuum.guilds.managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import pl.za.xvacuum.guilds.objects.Guild;

public class GuildManager {
	
	private static List<Guild> guilds = new ArrayList<Guild>();
	
	public static List<Guild> get(){
		return guilds;
	}
	
	public static void add(Guild u){
		if(!guilds.contains(u)) guilds.add(u);
	}
	
	public static void remove(Guild u){
		if(guilds.contains(u)) guilds.remove(u);
	}
	

	
	public static List<String> toTags(List<Guild> guilds){
		List<String> tags = new ArrayList<String>();
		for(Guild g : guilds) tags.add(g.getTag());
		return tags;
	}
	
	public static List<Guild> fromTags(List<String> tags){
		List<Guild> guilds = new ArrayList<Guild>();
		for(String s : tags) guilds.add(Guild.get(s));
		return guilds;
	}
	
    public static Guild byLocation(Location loc) {
        for (Guild g : guilds) {
            if (g.getRegion().isIn(loc)){
                return g;
            }
        }
        return null;
    }
	
	public static List<String> toNames(List<Guild> guilds){
		List<String> names = new ArrayList<String>();
		for(Guild g : guilds) names.add(g.getName());
		return names;
	}
	
	public static List<Guild> fromNames(List<String> names){
		List<Guild> guilds = new ArrayList<Guild>();
		for(String s : names) guilds.add(fromName(s));
		return guilds;
	}
	
	public static Guild fromName(String name){
		for(Guild g : guilds){
			if(g.getName().equalsIgnoreCase(name)) return g;
		}
		return null;
	}
	
	public static boolean tagExists(String tag){
		for(Guild g : guilds){
			if(g.getTag().equalsIgnoreCase(tag)) return true;
		}
		return false;
	}
	
	public static boolean nameExists(String name){
		for(Guild g : guilds){
			if(g.getName().equalsIgnoreCase(name)) return true;
		}
		return false;
	}
}
