package pl.za.xvacuum.guilds.mysql.data;

import org.bukkit.configuration.file.FileConfiguration;

import pl.za.xvacuum.guilds.Main;
import pl.za.xvacuum.guilds.managers.GuildManager;
import pl.za.xvacuum.guilds.managers.RegionManager;
import pl.za.xvacuum.guilds.managers.UserManager;
import pl.za.xvacuum.guilds.mysql.Query;
import pl.za.xvacuum.guilds.mysql.QueryExecutor;
import pl.za.xvacuum.guilds.objects.Guild;
import pl.za.xvacuum.guilds.objects.Region;
import pl.za.xvacuum.guilds.objects.User;

public class Data {

	public enum DataType {
		FLAT,
		MYSQL;
	}

	public abstract interface DataModify { 
		public boolean changed = false;
		
		public boolean wasChanged();
		public void setChanged(boolean changed);
		public void save();
	}
	
	public static DataType globalType() {
		FileConfiguration cfg = Main.getInstance().getConfig();
		String type = cfg.getString("data-type");
		if(type.equalsIgnoreCase("flatfile") 
				|| type.equalsIgnoreCase("flat") 
				|| type.equalsIgnoreCase("file")){
			return DataType.FLAT;
		} else if (type.equalsIgnoreCase("mysql") 
				|| type.equalsIgnoreCase("sql")){
			return DataType.MYSQL;
		}
		return null;
	}
	
	public static void saveAll() {
		for(User u : UserManager.get()) {
			u.save();
		}
		for(Guild g : GuildManager.get()) {
			g.save();
		}
		for(Region r : RegionManager.get()) {
			r.save();
		}
	}
	
	public static void createTables() {
		QueryExecutor.execute(new Query("CREATE TABLE IF NOT EXISTS qguilds_users (name VARCHAR(20), points int, kills int, deaths int, guild VARCHAR(50), title VARCHAR(50))"));
		QueryExecutor.execute(new Query("CREATE TABLE IF NOT EXISTS qguilds_regions (name VARCHAR(20), guild VARCHAR(50), center VARCHAR(100), size int, upperLoc VARCHAR(100), lowerLoc VARCHAR(100))"));
		QueryExecutor.execute(new Query("CREATE TABLE IF NOT EXISTS qguilds_guilds (name VARCHAR(20), tag VARCHAR(50), born bigint, leader VARCHAR(30), region VARCHAR(30), home VARCHAR(100), members VARCHAR(250), masters VARCHAR(250), allies VARCHAR(250), allyInvites VARCHAR(250), invited VARCHAR(250), broadcast VARCHAR(100), treasureContent VARCHAR(5000), lastEffectTake bigint)"));
	}
}
