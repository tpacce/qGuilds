package pl.za.xvacuum.guilds.objects;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import pl.za.xvacuum.guilds.api.enums.DataSaveType;
import pl.za.xvacuum.guilds.api.events.DataSaveEvent;
import pl.za.xvacuum.guilds.managers.RankManager;
import pl.za.xvacuum.guilds.managers.UserManager;
import pl.za.xvacuum.guilds.misc.files.Folders;
import pl.za.xvacuum.guilds.mysql.Query;
import pl.za.xvacuum.guilds.mysql.QueryExecutor;
import pl.za.xvacuum.guilds.mysql.data.Data;
import pl.za.xvacuum.guilds.mysql.data.Data.DataModify;
import pl.za.xvacuum.guilds.mysql.data.Data.DataType;

public class User implements DataModify{
	
	private String name;
	private String title;
	private Guild guild;
	private Rank rank;
	private boolean changed = false;
	
	public User(String name){
		this.name = name;
		UserManager.add(this);
		RankManager.update(this);
	}
	
	public User(Player p){
		this.name = p.getName();
		UserManager.add(this);
		RankManager.update(this);
	}
	
	@SuppressWarnings({ })
	public boolean isOnline()
	{
		return Bukkit.getPlayer(this.name) != null;
	}
	
	@SuppressWarnings({ })
	public Player getPlayer() {
		return Bukkit.getPlayer(this.name);
	}
	
	public boolean isInvited(Guild guild)
	{
		for(User s : guild.getInvited()){
			return s.getName().equals(this.name);
		}
		return false;
	}
	
	public boolean hasPermission(Guild g) {
		if(isLeader(g) || isMaster(g)) {
			return true;
		}
		return false;
	}
	
	public String getName() {
		return name;
	}

	public Guild getGuild() {
		return guild;
	}

	public Rank getRank() {
		if(this.rank !=null) return rank;
		this.rank = new Rank(this);
		this.rank.setKills(0);
		this.rank.setDeaths(0);
		this.rank.setPoints(1000);
		return this.rank;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setGuild(Guild guild) {
		this.guild = guild;
	}

	public void setRank(Rank rank) {
		this.rank = rank;
	}

	public boolean isMaster(Guild guild)
	{
		if(!hasGuild()) return false;
		return this.guild.getMasters().contains(this);
	}
	
	public boolean isLeader(Guild guild)
	{
		if(!hasGuild()) return false;
		return this.guild.getLeader().equals(this);
	}
	
	public boolean isMember(Guild guild)
	{
		if(!hasGuild()) return false;
		return this.guild.getMembers().contains(this);
	}
	
	public boolean hasGuild(){
		return (this.guild != null);
	}
	
	public static User get(String name){
		for(User u : UserManager.get()){
			if(u.getName().equalsIgnoreCase(name)) return u;
		}
		return new User(name);
	}
	
	public static User get(Player p){
		for(User u : UserManager.get()){
			if(u.getName().equalsIgnoreCase(p.getName())) return u;
		}
		return new User(p);
	}
	
	public String toString(){
		return this.name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public boolean wasChanged() {
		return changed;
	}

	@Override
	public void setChanged(boolean changed) {
		this.changed = changed;
		
	}
	
	public static User get(OfflinePlayer offline){
		for(User u : UserManager.get()){
			if(u.getName() == null) continue;
			if(u.getName().equalsIgnoreCase(offline.getName())) return u;
		}
		return new User(offline.getName());
	}

	@Override
	public void save() {
		if(Data.globalType().equals(DataType.MYSQL)) {
			QueryExecutor.execute(new Query(String.format("UPDATE qguilds_users SET `points`='%d' WHERE `name`='%s'", this.rank.getPoints(), this.name)));
			QueryExecutor.execute(new Query(String.format("UPDATE qguilds_users SET `kills`='%d' WHERE `name`='%s'", this.rank.getKills(), this.name)));
			QueryExecutor.execute(new Query(String.format("UPDATE qguilds_users SET `deaths`='%d' WHERE `name`='%s'", this.rank.getDeaths(), this.name)));
			if(this.guild == null)QueryExecutor.execute(new Query(String.format("UPDATE qguilds_users SET `guild`='null' WHERE `name`='%s'",  this.name)));
			else{
				QueryExecutor.execute(new Query(String.format("UPDATE qguilds_users SET `guild`='%s' WHERE `name`='%s'", this.guild.getTag(), this.name)));
			}
			QueryExecutor.execute(new Query(String.format("UPDATE qguilds_users SET `title`='%s' WHERE `name`='%s'", this.title, this.name)));
		} else {
			File f = Folders.getFile(this);
			if(!f.exists()){
				try {
					f.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
			yml.set("name", this.name);
			yml.set("points", this.rank.getPoints());
			yml.set("kills", this.rank.getKills());
			yml.set("deaths", this.rank.getDeaths());
			if(this.guild != null){
				yml.set("guild", this.guild.getTag());
			}
			else{
				yml.set("guild", null);
			}
			if(this.title != null){
				yml.set("title", this.title);
			}
			else{
				yml.set("title", null);
			}
			try {
				yml.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Bukkit.getPluginManager().callEvent(new DataSaveEvent(DataSaveType.USER));
		this.setChanged(false);
	}



	

}
