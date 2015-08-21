package pl.za.xvacuum.guilds.utils;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.SimplePluginManager;

import pl.za.xvacuum.guilds.Main;
import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.objects.Guild;
import pl.za.xvacuum.guilds.objects.Region;
import pl.za.xvacuum.guilds.utils.Logger.LogType;

public class Util {
	
	private static CommandMap cmdMap;
	public static List<Player> list = new ArrayList<>();
	private static Random rand = new Random();

	public static String setHEX(String s) { return ChatColor.translateAlternateColorCodes('&', s); }
	
	public static void sendMessage(CommandSender s, String t) {
		if(t == null) Logger.log(LogType.WARNING, "message cannot be null"); 
		else{
			s.sendMessage(setHEX(t));
			return;
		}
		return;
	}
	
	
	public static List<String> setHEXList(List<String> text) {
		List<String> result = new ArrayList<String>();
		
		for(String s : text) {
			result.add(setHEX(s));
		}
		return result;
	}

	public static int randomInt(int min, int max) {
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
	
	public static boolean chance(int chance) {
		int c = new Random().nextInt(100) + 1;
		if(c > chance) {
			return true;
		}
		return false;
	}
	
	public static int calculateJoinCost(int amount, Guild g) {
		int mem = g.getMembers().size();
		int cost = amount + (mem * 2);
		return Math.round(cost);
	}
	
	public static int calculateEnlargeCost(Region r) {
		int ns = r.getSize() + Main.getInstance().getConfig().getInt("enlarge-radius");
		double nc = 4 * ns + r.getSize() % 6 - ns;
		Math.round((double)nc);
		return (int) nc / 2;
	}
	
	public static void teleportDelay(final Player player, final Location loc)
	{
		
		if(player.hasPermission("qguilds.teleport.bypass")){
			player.teleport(loc);
			Util.sendMessage(player, Messages.TeleportSuccess);
			return;
		}
		
		list.add(player);
		Util.sendMessage(player, Messages.TeleportProcess.replace("%time%", String.valueOf(Main.getInstance().getConfig().getLong("teleport-delay")).replace("L", "")));
		Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
		    @Override
			public void run() {
		    	if(list.contains(player)){
		    		if(!player.isOnline()) {
		    			list.remove(player);
		    			return;
		    		}
		    		
			       player.teleport(loc);
			       list.remove(player);
			       Util.sendMessage(player, Messages.TeleportSuccess);
			       Main.getInstance().getServer().getScheduler().cancelAllTasks();
		    	}
		    }
		}, Main.getInstance().getConfig().getLong("teleport-delay") * 20); 
	}
	
	public static boolean registerCommand(Command command)
	{
		if (cmdMap == null) {
			try {
				Field field = SimplePluginManager.class.getDeclaredField("commandMap");
				field.setAccessible(true);
				cmdMap = (CommandMap)field.get(Bukkit.getServer().getPluginManager());
			}
			catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		cmdMap.register("qguilds", command);
		return true;
	}
	
	public static String parseTime(long time) {
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.GERMANY);
		Date d = new Date(time);
		return format.format(d);
	}
	
	

}
