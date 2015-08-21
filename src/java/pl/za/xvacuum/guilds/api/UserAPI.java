package pl.za.xvacuum.guilds.api;

import org.bukkit.entity.Player;

import pl.za.xvacuum.guilds.objects.User;

public class UserAPI {
	
	public static void addPoints(Player player, int points) {
		User u = User.get(player);
		u.getRank().setPoints(u.getRank().getPoints() + points);
		APILogger.log("Added " + points + " points to player " + player.getName());
		return;
	}
	
	public static void removePoints(Player player, int points) {
		User u = User.get(player);
		u.getRank().setPoints(u.getRank().getPoints() - points);
		APILogger.log("Removed " + points + " points to player " + player.getName());
		return;
	}
	
	public static void setPoints(Player player, int points) {
		User u = User.get(player);
		u.getRank().setPoints(points);
		APILogger.log("Set's " + points + " points of player " + player.getName());
		return;
	}
	
	public static void addKills(Player player, int kills) {
		User u = User.get(player);
		u.getRank().setKills(u.getRank().getPoints() + kills);
		APILogger.log("Added " + kills + " kills to player " + player.getName());
		return;
	}
	
	public static void removeKills(Player player, int kills) {
		User u = User.get(player);
		u.getRank().setKills(u.getRank().getPoints() - kills);
		APILogger.log("Removed " + kills + " kills to player " + player.getName());
		return;
	}
	
	public static void setKills(Player player, int kills) {
		User u = User.get(player);
		u.getRank().setKills(kills);
		APILogger.log("Set's " + kills + " kills of player " + player.getName());
		return;
	}

	
	public static void addDeaths(Player player, int deaths) {
		User u = User.get(player);
		u.getRank().setKills(u.getRank().getPoints() + deaths);
		APILogger.log("Added " + deaths + " deaths to player " + player.getName());
		return;
	}
	
	public static void removeDeaths(Player player, int deaths) {
		User u = User.get(player);
		u.getRank().setKills(u.getRank().getPoints() - deaths);
		APILogger.log("Removed " + deaths + " deaths to player " + player.getName());
		return;
	}
	
	public static void setDeaths(Player player, int deaths) {
		User u = User.get(player);
		u.getRank().setKills(deaths);
		APILogger.log("Set's " + deaths + " deaths of player " + player.getName());
		return;
	}
}
