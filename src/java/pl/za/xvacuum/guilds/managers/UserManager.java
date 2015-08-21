package pl.za.xvacuum.guilds.managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import pl.za.xvacuum.guilds.objects.Guild;
import pl.za.xvacuum.guilds.objects.User;

public class UserManager {
	
	private static List<User> users = new ArrayList<User>();
	
	public static List<User> get(){
		return users;
	}
	
	public static void add(User u){
		if(!users.contains(u)) users.add(u);
	}
	
	public static void remove(User u){
		if(users.contains(u)) users.remove(u);
	}
	
	public static Guild playerGuild(String name){
		for(Guild g : GuildManager.get()){
			for(User u : g.getMembers()){
				if(u.getName().equalsIgnoreCase(name)) return g;
			}
		}
		return null;
	}
	
	public static boolean hasGuild(User u) {
		return u.hasGuild();
	}
	
	public static boolean playedBefore(String name){
		for(User u : users){
			if(u.getName().equalsIgnoreCase(name)) return true;
		}
		return false;
	}
	
	public static boolean playedBefore(Player player){
		for(User u : users){
			if(u.getName().equalsIgnoreCase(player.getName())) return true;
		}
		return false;
	}
	
	public static List<String> toNames(List<User> users){
		List<String> names = new ArrayList<String>();
		for(User u : users) names.add(u.getName());
		return names;
	}
	
	public static List<User> fromNames(List<String> names){
		List<User> users = new ArrayList<User>();
		for(String s : names) users.add(User.get(s));
		return users;
	}
	
}
