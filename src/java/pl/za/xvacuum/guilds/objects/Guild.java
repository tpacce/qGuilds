package pl.za.xvacuum.guilds.objects;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import pl.za.xvacuum.guilds.api.enums.DataSaveType;
import pl.za.xvacuum.guilds.api.events.DataSaveEvent;
import pl.za.xvacuum.guilds.managers.GuildManager;
import pl.za.xvacuum.guilds.managers.LocationManager;
import pl.za.xvacuum.guilds.managers.UserManager;
import pl.za.xvacuum.guilds.misc.files.Folders;
import pl.za.xvacuum.guilds.mysql.Query;
import pl.za.xvacuum.guilds.mysql.QueryExecutor;
import pl.za.xvacuum.guilds.mysql.data.Data;
import pl.za.xvacuum.guilds.mysql.data.Data.DataModify;
import pl.za.xvacuum.guilds.mysql.data.Data.DataType;
import pl.za.xvacuum.guilds.utils.ItemSerializer;
import pl.za.xvacuum.guilds.utils.ListSerializer;

public class Guild implements DataModify{
	
	private String name;
	private String tag;
	private User leader;
	private List<User> members;
	private List<User> masters;
	private List<User> invited;
	private List<Guild> allies;
	private List<Guild> allyInvites;
	private Location home;
	private long born;
	private long lastEffectTake;
	private boolean pvp;
	private Region region;
	private Rank rank;
	private Treasure treasure;
	private String broadcast;
	private boolean changed;
	
	private Guild(String tag){
		this.tag = tag;
		GuildManager.add(this);
	}

	
	public static Guild get(String tag){
		for(Guild g : GuildManager.get()){
			if(g.getTag().equalsIgnoreCase(tag)) return g;
		}
		return new Guild(tag);
	}
	
	public boolean hasRegion(){
		return (this.region !=null);
	}
	
	public String getName() {
		return name;
	}

	public String getTag() {
		return tag;
	}

	public User getLeader() {
		return leader;
	}

	public List<User> getMembers() {
		return members;
	}

	public List<User> getMasters() {
		return masters;
	}

	public List<User> getInvited() {
		return invited;
	}

	public List<Guild> getAllies() {
		return allies;
	}

	public List<Guild> getAllyInvites() {
		return allyInvites;
	}

	public Location getHome() {
		return home;
	}

	public long getBorn() {
		return born;
	}

	public boolean isPvp() {
		return pvp;
	}

	public Region getRegion() {
		return region;
	}

	public Rank getRank() {
		return rank;
	}

	public Treasure getTreasure() {
		return treasure;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void setLeader(User leader) {
		this.leader = leader;
	}

	public void setMembers(List<User> members) {
		this.members = members;
	}

	public void setMasters(List<User> masters) {
		this.masters = masters;
	}

	public void setInvited(List<User> invited) {
		this.invited = invited;
	}

	public void setAllies(List<Guild> allies) {
		this.allies = allies;
	}

	public void setAllyInvites(List<Guild> allyInvites) {
		this.allyInvites = allyInvites;
	}
	
	public void addMember(User u) {
		this.members.add(u);
	}
	
	public void addMaster(User u) {
		this.masters.add(u);
	}
	
	public void addInvite(User u) {
		this.invited.add(u);
	}
	
	public void addAlly(Guild g) {
		this.allies.add(g);
	}
	
	public void addAllyInvite(Guild g) {
		this.allyInvites.add(g);
	}
	
	public void removeInvite(User u){
		this.invited.remove(u);
	}
	
	public void removeMember(User u){
		this.members.remove(u);
		u.setGuild(null);
	}
	
	public void removeMaster(User u){
		this.masters.remove(u);
	}
	
	public void removeAlly(Guild g){
		this.allies.remove(g);
	}
	
	
	public void removeAllyInvite(Guild g){
		this.allyInvites.remove(g);
	}

	public void setHome(Location home) {
		this.home = home;
	}

	public void setBorn(long born) {
		this.born = born;
	}

	public void setPvp(boolean pvp) {
		this.pvp = pvp;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public void setRank(Rank rank) {
		this.rank = rank;
	}

	public void setTreasure(Treasure treasure) {
		this.treasure = treasure;
	}
	
	public String toString(){
		return this.tag;
	}


	public String getBroadcast() {
		return broadcast;
	}


	public void setBroadcast(String broadcast) {
		this.broadcast = broadcast;
	}


	@Override
	public boolean wasChanged() {
		return changed;
	}


	@Override
	public void setChanged(boolean changed) {
		this.changed = changed;
		
	}


	@Override
	public void save() {
		if(Data.globalType().equals(DataType.MYSQL)) {
			QueryExecutor.execute(new Query(String.format("UPDATE qguilds_guilds SET `name`='%s' WHERE `tag`='%s'", this.name, this.tag)));
			QueryExecutor.execute(new Query(String.format("UPDATE qguilds_guilds SET `born`='%d' WHERE `tag`='%s'", this.born, this.tag)));
			QueryExecutor.execute(new Query(String.format("UPDATE qguilds_guilds SET `leader`='%s' WHERE `tag`='%s'", this.leader.getName(), this.tag)));
			QueryExecutor.execute(new Query(String.format("UPDATE qguilds_guilds SET `region`='%s' WHERE `tag`='%s'", this.region.getName(), this.tag)));
			QueryExecutor.execute(new Query(String.format("UPDATE qguilds_guilds SET `home`='%s' WHERE `tag`='%s'", LocationManager.fromLocation(this.home), this.tag)));
			
			QueryExecutor.execute(new Query(String.format("UPDATE qguilds_guilds SET `members`='%s' WHERE `tag`='%s'", ListSerializer.serialize(UserManager.toNames(this.members)), this.tag)));
			QueryExecutor.execute(new Query(String.format("UPDATE qguilds_guilds SET `masters`='%s' WHERE `tag`='%s'", ListSerializer.serialize(UserManager.toNames(this.masters)), this.tag)));
			QueryExecutor.execute(new Query(String.format("UPDATE qguilds_guilds SET `allies`='%s' WHERE `tag`='%s'", ListSerializer.serialize(GuildManager.toTags(this.allies)), this.tag)));
			QueryExecutor.execute(new Query(String.format("UPDATE qguilds_guilds SET `allyInvites`='%s' WHERE `tag`='%s'", ListSerializer.serialize(GuildManager.toTags(this.allyInvites)), this.tag)));
			QueryExecutor.execute(new Query(String.format("UPDATE qguilds_guilds SET `invited`='%s' WHERE `tag`='%s'", ListSerializer.serialize(UserManager.toNames(this.invited)), this.tag)));
			
			QueryExecutor.execute(new Query(String.format("UPDATE qguilds_guilds SET `broadcast`='%s' WHERE `tag`='%s'", this.broadcast, this.tag)));
			QueryExecutor.execute(new Query(String.format("UPDATE qguilds_guilds SET `treasureContent`='%s' WHERE `tag`='%s'", ItemSerializer.serialize(this.treasure.getItems()), this.tag)));
			QueryExecutor.execute(new Query(String.format("UPDATE qguilds_guilds SET `lastEffectTake`='%d' WHERE `tag`='%s'", this.lastEffectTake, this.tag)));
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
			yml.set("tag", this.tag);
			yml.set("born", this.born);
			yml.set("lastEffectTake", this.lastEffectTake);
			yml.set("leader", this.leader.getName());
			yml.set("region", this.region.getName());
			yml.set("home", LocationManager.fromLocation(this.home));
			yml.set("members", UserManager.toNames(this.members));
			yml.set("masters", UserManager.toNames(this.masters));
			yml.set("allies", GuildManager.toTags(this.allies));
			yml.set("allyInvites", GuildManager.toTags(this.allyInvites));
			yml.set("invited", UserManager.toNames(this.invited));
			yml.set("broadcast", this.broadcast);
			yml.set("treasureContent", ItemSerializer.serialize(this.treasure.getItems()));
			try {
				yml.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Bukkit.getPluginManager().callEvent(new DataSaveEvent(DataSaveType.GUILD));
		this.setChanged(false);
	}


	public long getLastEffectTake() {
		return lastEffectTake;
	}


	public void setLastEffectTake(long lastEffectTake) {
		this.lastEffectTake = lastEffectTake;
	}

}
	
