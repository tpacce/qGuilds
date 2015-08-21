package pl.za.xvacuum.guilds.antylogout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import pl.za.xvacuum.guilds.Main;
import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.objects.User;
import pl.za.xvacuum.guilds.utils.Util;

public class LogoutSystem {
	
	private static final List<Player> times = new ArrayList<Player>();
	private static final HashMap<Player, Player> lastAttacker = new HashMap<Player, Player>();
	
	public static List<Player> getTimes() {
		return times;
	}
	
	public static HashMap<Player, Player> getLastAttackerMap() {
		return lastAttacker;
	}
	
	public static Player getLastAttacker(Player player) {
		return lastAttacker.containsKey(player) ? lastAttacker.get(player) : null;
	}
	
	public static void start(final Player player) {
		Util.sendMessage(player, Messages.AntyLogoutInCombat);
		times.add(player);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable(){

			@Override
			public void run() {
				times.remove(player);
				Util.sendMessage(player, Messages.AntyLogoutNotInCombat);
				return;
			}
			
		}, 15L * 20);
	}
	
	public static void notOnline(Player player) {
		times.remove(player);
		User u = User.get(player);
		u.getRank().setPoints(u.getRank().getPoints() - 50);
		if(lastAttacker.containsKey(player)) {
			Player lastatt = lastAttacker.get(player);
			User user = User.get(lastatt);
			user.getRank().setPoints(user.getRank().getPoints() + 25);
		}
		return;
	}


}
