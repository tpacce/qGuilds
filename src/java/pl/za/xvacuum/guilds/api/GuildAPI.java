package pl.za.xvacuum.guilds.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import pl.za.xvacuum.guilds.managers.DataManager;
import pl.za.xvacuum.guilds.managers.GuildManager;
import pl.za.xvacuum.guilds.managers.RankManager;
import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.objects.Guild;
import pl.za.xvacuum.guilds.objects.Rank;
import pl.za.xvacuum.guilds.objects.Region;
import pl.za.xvacuum.guilds.objects.Treasure;
import pl.za.xvacuum.guilds.objects.User;
import pl.za.xvacuum.guilds.tags.TagChanger;
import pl.za.xvacuum.guilds.utils.BuildUtils;

public class GuildAPI {
	
	public static void deleteGuild(String tag) {
		Guild g = Guild.get(tag);
		g.getRegion().getCenter().getBlock().setType(Material.AIR);
		for(User m : g.getMembers()) {
			m.setGuild(null);
			m.save();
		}
		g.getRegion().deleteDragonEgg();
		GuildManager.remove(g);
		DataManager.delete(g.getRegion());
		for(User us : g.getMembers()) {
			us.setTitle(null);
			us.setGuild(null);
		}
		g.getRegion().delete();
		for(Guild gu : g.getAllies()) g.removeAlly(gu);
		for(Guild gu : GuildManager.get()){
			if(g.getAllyInvites().contains(gu)) g.removeAllyInvite(g);
		}
		RankManager.remove(g.getRank());
		DataManager.delete(g);
		DataManager.reload(false, false);
		APILogger.log("Deleted guild ,," + tag + "''.");
		return;
	}
	
	public static void createGuild(Player leader, String name, String tag, Location location) {
		Guild g = Guild.get(tag);
		g.setTag(tag.toString());
		g.setName(name.toString());
		g.setBorn(System.currentTimeMillis());
		g.setHome(location);
		g.setLeader(User.get(leader));
		g.setPvp(false);
		List<User> members = new ArrayList<>();
		List<User> masters = new ArrayList<>();
		List<User> invited = new ArrayList<>();
		List<Guild> allies = new ArrayList<>();
		List<Guild> allyInvites = new ArrayList<>();
		members.add(User.get(leader));
		g.setMembers(members);
		g.setMasters(masters);
		g.setAllies(allies);
		g.setAllyInvites(allyInvites);
		g.setInvited(invited);
		g.setRank(new Rank(g));
		g.setRegion(new Region(g, location, 50));
		g.setTreasure(new Treasure(g));
		g.setBroadcast(Messages.BroadcastDefault);
		Block b = location.getBlock();
		
		for(Location l : BuildUtils.sphere(b.getLocation(), 4, 4, false, true, 0)) {
			if(l.getBlock().getType() != Material.BEDROCK) l.getBlock().setType(Material.AIR);
		}
		int cylLocMinus = b.getLocation().getBlockY() - 2;
		Location cylLoc = new Location(b.getWorld(), b.getLocation().getBlockX(), cylLocMinus, b.getLocation().getBlockZ());
		BuildUtils.cylinder(cylLoc, 5, Material.OBSIDIAN);

		
		b.setType(Material.DRAGON_EGG);
		Block bedrockB = cylLoc.getBlock();
		bedrockB.setType(Material.BEDROCK);
		User u = User.get(leader);
		u.setGuild(g);
		u.save();
		g.save();
		g.getRegion().save();
		DataManager.reload(false, false);
		RankManager.update(g);
		TagChanger.createGuild(g);
		APILogger.log("Created guild with leader ,," + u.getName() + "'', name " + name + ", and tag " + tag + ".");
		return;
	}
	
	public static void addPoints(String tag, int points) {
		Guild g = Guild.get(tag);
		g.getRank().setPoints(g.getRank().getPoints() + points);
		APILogger.log("Added " + points + " points to guild " + tag);
		return;
	}
	
	public static void removePoints(String tag, int points) {
		Guild g = Guild.get(tag);
		g.getRank().setPoints(g.getRank().getPoints() - points);
		APILogger.log("Removed " + points + " points to guild " + tag);
		return;
	}
	
	public static void setPoints(String tag, int points) {
		Guild g = Guild.get(tag);
		g.getRank().setPoints(points);
		APILogger.log("Set's " + points + " points of guild " + tag);
		return;
	}
	
	
	public static void addKills(String tag, int kills) {
		Guild g = Guild.get(tag);
		g.getRank().setKills(g.getRank().getKills() + kills);
		APILogger.log("Added " + kills + " kills to guild " + tag);
		return;
	}
	
	public static void removeKills(String tag, int  kills) {
		Guild g = Guild.get(tag);
		g.getRank().setKills(g.getRank().getKills() - kills);
		APILogger.log("Removed " + kills + " kills to guild " + tag);
		return;
	}
	
	public static void setKills(String tag, int kills) {
		Guild g = Guild.get(tag);
		g.getRank().setKills(kills);
		APILogger.log("Set's " + kills + " kills of guild " + tag);
		return;
	}
	
	
	public static void addDeaths(String tag, int deaths) {
		Guild g = Guild.get(tag);
		g.getRank().setDeaths(g.getRank().getDeaths() + deaths);
		APILogger.log("Added " + deaths + " deaths to guild " + tag);
		return;
	}
	
	public static void removeDeaths(String tag, int deaths) {
		Guild g = Guild.get(tag);
		g.getRank().setDeaths(g.getRank().getDeaths() - deaths);
		APILogger.log("Removed " + deaths + " deaths to guild " + tag);
		return;
	}
	
	public static void setDeaths(String tag, int deaths) {
		Guild g = Guild.get(tag);
		g.getRank().setDeaths(deaths);
		APILogger.log("Set's " + deaths + " deaths of guild " + tag);
		return;
	}

}
