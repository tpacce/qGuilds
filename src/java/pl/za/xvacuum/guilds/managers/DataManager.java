package pl.za.xvacuum.guilds.managers;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import pl.za.xvacuum.guilds.api.enums.DataLoadType;
import pl.za.xvacuum.guilds.api.events.DataLoadEvent;
import pl.za.xvacuum.guilds.misc.files.Folders;
import pl.za.xvacuum.guilds.mysql.ConnectionSource;
import pl.za.xvacuum.guilds.mysql.Query;
import pl.za.xvacuum.guilds.mysql.QueryExecutor;
import pl.za.xvacuum.guilds.mysql.data.Data;
import pl.za.xvacuum.guilds.mysql.data.Data.DataType;
import pl.za.xvacuum.guilds.objects.Guild;
import pl.za.xvacuum.guilds.objects.Rank;
import pl.za.xvacuum.guilds.objects.Region;
import pl.za.xvacuum.guilds.objects.Treasure;
import pl.za.xvacuum.guilds.objects.User;
import pl.za.xvacuum.guilds.utils.EffectUtils;
import pl.za.xvacuum.guilds.utils.ItemSerializer;
import pl.za.xvacuum.guilds.utils.ListSerializer;
import pl.za.xvacuum.guilds.utils.Logger;
import pl.za.xvacuum.guilds.utils.Logger.LogType;

public class DataManager {
	
	
	public static void reload(boolean msg, boolean msg2)
	{
		if(msg)Logger.log(LogType.INFO, "  [*] Reloading...");
		loadGuilds(msg2);
		loadUsers(msg2);
		loadRegions(msg2);
	}


	public static void loadGuilds(boolean msg){
		int loaded = 0;
		if(Data.globalType().equals(DataType.FLAT)) {
			for(File f : Folders.getGuildsFolder().listFiles()){
				YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
				Guild g = Guild.get(yml.getString("tag"));
				g.setName(yml.getString("name"));
				g.setBorn(yml.getLong("born"));
				g.setLeader(User.get(yml.getString("leader")));
				g.setMembers(UserManager.fromNames(yml.getStringList("members")));
				g.setInvited(UserManager.fromNames(yml.getStringList("invited")));
				g.setMasters(UserManager.fromNames(yml.getStringList("masters")));
				g.setAllies(GuildManager.fromTags(yml.getStringList("allies")));
				g.setAllyInvites(GuildManager.fromTags(yml.getStringList("allyInvites")));
				g.setHome(LocationManager.fromString(yml.getString("home")));
				g.setBroadcast(yml.getString("broadcast"));
				g.setLastEffectTake(yml.getLong("lastEffectTake"));
				EffectUtils.setLastTakeEffect(g, g.getLastEffectTake());
				if(yml.get("region") !=null){
					g.setRegion(Region.get(yml.getString("region")));
				}
				g.setRank(new Rank(g));
				g.setTreasure(new Treasure(g, yml.getString("treasureContent")));
				loaded++;
			}
		}else {
			try {
				Query q = new Query("SELECT * FROM qguilds_guilds");
				Connection c = ConnectionSource.getInstance().dataSource.getConnection();
				Statement st = c.createStatement();
				ResultSet rs = st.executeQuery(q.getQuery());		
				while(rs.next()) {
					Guild g = Guild.get(rs.getString("tag"));
					g.setName(rs.getString("name"));
					g.setBorn(rs.getLong("born"));
					g.setLeader(User.get(rs.getString("leader")));
					g.setRegion(Region.get(rs.getString("region")));
					g.setHome(LocationManager.fromString(rs.getString("home")));

					g.setMembers(UserManager.fromNames(ListSerializer.deserialize(rs.getString("members"))));
					g.setMasters(UserManager.fromNames(ListSerializer.deserialize(rs.getString("masters"))));
					g.setAllies(GuildManager.fromNames(ListSerializer.deserialize(rs.getString("allies"))));
					g.setAllyInvites(GuildManager.fromNames(ListSerializer.deserialize(rs.getString("allyInvites"))));
					g.setInvited(UserManager.fromNames(ListSerializer.deserialize(rs.getString("invited"))));
				
					g.setBroadcast(rs.getString("broadcast"));
					g.setRank(new Rank(g));
					g.setTreasure(new Treasure(g, rs.getString("treasureContent")));
					g.setLastEffectTake(rs.getLong("lastEffectTake"));
					loaded++;
				}
				rs.close();
				st.close();
				c.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		Bukkit.getPluginManager().callEvent(new DataLoadEvent(DataLoadType.GUILDS, loaded));
		if(msg)Logger.log(LogType.INFO, "     Loaded " + loaded + " guilds.");
	}
	
	public static void loadUsers(boolean msg){
		int loaded = 0;
		if(Data.globalType().equals(DataType.FLAT)) {
			for(File f : Folders.getUsersFolder().listFiles()){
				YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
				User u = User.get(yml.getString("name"));
				u.getRank().setPoints(yml.getInt("points"));
				u.getRank().setKills(yml.getInt("kills"));
				u.getRank().setDeaths(yml.getInt("deaths"));
				if(yml.get("guild") !=null){
					u.setGuild(Guild.get(yml.getString("guild")));
				}
				if(yml.get("title") !=null){
					u.setTitle(yml.getString("title"));
				}
				loaded++;
			}
		}else {
			try {
				Query q = new Query("SELECT * FROM qguilds_users");
				Connection c = ConnectionSource.getInstance().dataSource.getConnection();
				Statement st = c.createStatement();
				ResultSet rs = st.executeQuery(q.getQuery());		
				while(rs.next()) {
					User u = User.get(rs.getString(1));
					u.getRank().setPoints(rs.getInt(2));
					u.getRank().setKills(rs.getInt(3));
					u.getRank().setDeaths(rs.getInt(4));
					if(!rs.getString(5).equals("null")) {
						u.setGuild(Guild.get(rs.getString(5)));
					}
					if(!rs.getString(6).equals("null")) {
						u.setTitle(rs.getString(6));
					}
					loaded++;
				}
				rs.close();
				st.close();
				c.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		Bukkit.getPluginManager().callEvent(new DataLoadEvent(DataLoadType.USERS, loaded));
		if(msg)Logger.log(LogType.INFO, "     Loaded " + loaded + " users.");
	}
	
	public static void loadRegions(boolean msg){
		int loaded = 0;
		if(Data.globalType().equals(DataType.FLAT)) {
			for(File f : Folders.getRegionsFolder().listFiles()){
				YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
				Region r = Region.get(yml.getString("name"));
				r.setGuild(Guild.get(yml.getString("guild")));
				r.setCenter(LocationManager.fromString(yml.getString("center")));
				r.setSize(yml.getInt("size"));
				r.setLowerLoc(LocationManager.fromString(yml.getString("lowerLoc")));
				r.setUpperLoc(LocationManager.fromString(yml.getString("upperLoc")));
				loaded++;
			}
		}else {
			try {
				Query q = new Query("SELECT * FROM qguilds_regions");
				Connection c = ConnectionSource.getInstance().dataSource.getConnection();
				Statement st = c.createStatement();
				ResultSet rs = st.executeQuery(q.getQuery());		
				while(rs.next()) {
					Region r = Region.get(rs.getString(1));
					r.setGuild(Guild.get(rs.getString(2)));
					r.setCenter(LocationManager.fromString(rs.getString(3)));
					r.setSize(rs.getInt(4));
					r.setLowerLoc(LocationManager.fromString(rs.getString(5)));
					r.setUpperLoc(LocationManager.fromString(rs.getString(6)));
					loaded++;
				}
				rs.close();
				st.close();
				c.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		Bukkit.getPluginManager().callEvent(new DataLoadEvent(DataLoadType.REGIONS, loaded));
		if(msg)Logger.log(LogType.INFO, "     Loaded " + loaded + " regions.");
	}
	
	public static void add(Guild g) { 
		if(!Data.globalType().equals(DataType.FLAT)) {
			QueryExecutor.execute(new Query(String.format("INSERT INTO qguilds_guilds VALUES ('%s', '%s', '%d', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%d')", 
					g.getName(), 
					g.getTag(), 
					g.getBorn(),
					g.getLeader().getName(),
					g.getRegion().getName(),
					LocationManager.fromLocation(g.getHome()),
					ListSerializer.serialize(UserManager.toNames(g.getMembers())),
					ListSerializer.serialize(UserManager.toNames(g.getMasters())),
					ListSerializer.serialize(GuildManager.toTags(g.getAllies())),
					ListSerializer.serialize(GuildManager.toTags(g.getAllyInvites())),
					ListSerializer.serialize(UserManager.toNames(g.getInvited())),
					g.getBroadcast(),
					ItemSerializer.serialize(g.getTreasure().getItems()),
					g.getLastEffectTake()
					)));
		}else {
			g.save();
		}
	}
	
	public static void add(User u) { 
		if(!Data.globalType().equals(DataType.FLAT)) {
			if(u.getGuild() == null && u.getTitle() == null) {
				QueryExecutor.execute(new Query(String.format("INSERT INTO qguilds_users VALUES ('%s', '%d', '%d', '%d', NULL, NULL)", 
						u.getName(), 
						u.getRank().getPoints(), 
						u.getRank().getKills(),
						u.getRank().getDeaths())));
			}else if (u.getGuild() == null && u.getTitle() != null){
				QueryExecutor.execute(new Query(String.format("INSERT INTO qguilds_users VALUES ('%s', '%d', '%d', '%d', NULL, '%s')", 
						u.getName(), 
						u.getRank().getPoints(), 
						u.getRank().getKills(),
						u.getRank().getDeaths(),
						u.getTitle())));
			}else {
				QueryExecutor.execute(new Query(String.format("INSERT INTO qguilds_users VALUES ('%s', '%d', '%d', '%d', '%s', '%s')", 
						u.getName(), 
						u.getRank().getPoints(), 
						u.getRank().getKills(),
						u.getRank().getDeaths(),
						u.getGuild().getTag(),
						u.getTitle())));
			}
			
		}else {
			u.save();
		}
	}
	
	public static void add(Region r) { 
		if(!Data.globalType().equals(DataType.FLAT)) {
			QueryExecutor.execute(new Query(String.format("INSERT INTO qguilds_regions VALUES ('%s', '%s', '%s', '%d', '%s', '%s')", 
					r.getName(), 
					r.getGuild().getTag(), 
					LocationManager.fromLocation(r.getCenter()),
					r.getSize(),
					LocationManager.fromLocation(r.getLowerLoc()),
					LocationManager.fromLocation(r.getUpperLoc()))));
		}else {
			r.save();
		}
	}
	
	public static void delete(Guild g)
	{
		if(Data.globalType().equals(DataType.FLAT)) {
			File f = Folders.getFile(g);
			if(!f.exists()) return;
			f.delete();
			return;
		}else {
			QueryExecutor.execute(new Query(String.format("DELETE FROM qguilds_guilds WHERE `tag`='%s'", g.getTag())));
			return;
		}
	}
	
	public static void delete(User u)
	{
		if(Data.globalType().equals(DataType.FLAT)) {
			File f = Folders.getFile(u);
			if(!f.exists()) return;
			f.delete();
			return;
		}else {
			QueryExecutor.execute(new Query(String.format("DELETE FROM qguilds_users WHERE `name`='%s'", u.getName())));
			return;
		}
	}
	
	public static void delete(Region r)
	{
		if(Data.globalType().equals(DataType.FLAT)) {
			File f = Folders.getFile(r);
			if(!f.exists()) return;
			f.delete();
			return;
		}else {
			QueryExecutor.execute(new Query(String.format("DELETE FROM qguilds_regions WHERE `name`='%s'", r.getName())));
			return;
		}
	}
}
