package pl.za.xvacuum.guilds.objects;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;

import pl.za.xvacuum.guilds.api.enums.DataSaveType;
import pl.za.xvacuum.guilds.api.events.DataSaveEvent;
import pl.za.xvacuum.guilds.managers.DataManager;
import pl.za.xvacuum.guilds.managers.LocationManager;
import pl.za.xvacuum.guilds.managers.RegionManager;
import pl.za.xvacuum.guilds.misc.files.Folders;
import pl.za.xvacuum.guilds.mysql.Query;
import pl.za.xvacuum.guilds.mysql.QueryExecutor;
import pl.za.xvacuum.guilds.mysql.data.Data;
import pl.za.xvacuum.guilds.mysql.data.Data.DataModify;
import pl.za.xvacuum.guilds.mysql.data.Data.DataType;

public class Region implements DataModify{
	
	private String name;
	private Guild guild;
	private Location center;
	private Location lowerLoc;
	private Location upperLoc;
	private int size;
	private boolean changed;
	
	private Region(String name){
		this.name = name;
		RegionManager.add(this);
	}
	
	public Region(Guild guild, Location center, int size){
		this.guild = guild;
		this.name = guild.getTag();
		this.center = center;
		this.size = size;
		RegionManager.add(this);
		calc();
	}
	
	public void calc(){
		Vector low = new Vector(center.getBlockX() - this.size, 0, center.getBlockZ() - this.size);
		Vector up = new Vector(center.getBlockX() + this.size, 256, center.getBlockZ() + this.size);
		this.lowerLoc = low.toLocation(center.getWorld());
		this.upperLoc = up.toLocation(center.getWorld());
	}
	
	public static Region get(String name){
		for(Region r : RegionManager.get()){
			if(r.getName().equalsIgnoreCase(name)) return r;
		}
		return new Region(name);
	}

	public String getName() {
		return this.name;
	}

	public Guild getGuild() {
		return this.guild;
	}

	public Location getCenter() {
		return this.center;
	}

	public int getSize() {
		return this.size;
	}

	public void setName(String name) {
		this.name = name;
	}
	public Location getLowerLoc() {
		return lowerLoc;
	}

	public void setLowerLoc(Location lowerLoc) {
		this.lowerLoc = lowerLoc;
	}

	public Location getUpperLoc() {
		return upperLoc;
	}

	public void setUpperLoc(Location upperLoc) {
		this.upperLoc = upperLoc;
	}

	public void setGuild(Guild guild) {
		this.guild = guild;
	}

	public void setCenter(Location center) {
		this.center = center;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	public void delete(){
		RegionManager.remove(this);
		this.guild.setRegion(null);
		this.guild = null;
		this.center = null;
		this.lowerLoc = null;
		this.upperLoc = null;
		DataManager.delete(this);
	}
	
	public boolean isNear(Location center){
		if(center == null) return false;
		for(Region region : RegionManager.get()){
			if(region.getCenter() == null) return false;
			if(!center.getWorld().equals(region.getCenter().getWorld())) return false;
			double dis = center.distance(region.getCenter());
			int i = region.getSize();
			return dis < (2 * i + 15);
		} 
		return false;	
	}
	
	public boolean isIn(Location l){
		calc();
		if(this.lowerLoc == null || this.upperLoc == null || l == null) return false;
		if((l.getBlockX() > getLowerLoc().getBlockX()) && (l.getBlockX() < getUpperLoc().getBlockX()) &&
			(l.getBlockY() > getLowerLoc().getBlockY()) && (l.getBlockY() < getUpperLoc().getBlockY()) &&
			(l.getBlockZ() > getLowerLoc().getBlockZ()) && (l.getBlockZ() < getUpperLoc().getBlockZ())
		){
			return true;
		}
		return false;
	}
	
	public Region inWhich(Location l){
		for(Region r : RegionManager.get()){
			if(r.isIn(l)) return r;
		}
		return null;
	}
	
	public void deleteDragonEgg() {
		int loc = this.center.getBlockY() - 1;
		Location dragonEggLoc = new Location(this.center.getWorld(), this.center.getBlockX(), loc, this.center.getBlockZ());
		dragonEggLoc.getBlock().setType(Material.AIR);
		return;
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
			QueryExecutor.execute(new Query(String.format("UPDATE qguilds_regions SET `guild`='%s' WHERE `name`='%s'", this.guild.getTag(), this.name)));
			QueryExecutor.execute(new Query(String.format("UPDATE qguilds_regions SET `center`='%s' WHERE `name`='%s'", LocationManager.fromLocation(this.center), this.name)));
			QueryExecutor.execute(new Query(String.format("UPDATE qguilds_regions SET `size`='%d' WHERE `name`='%s'", this.size, this.name)));
			QueryExecutor.execute(new Query(String.format("UPDATE qguilds_regions SET `lowerLoc`='%s' WHERE `name`='%s'", LocationManager.fromLocation(this.lowerLoc), this.name)));
			QueryExecutor.execute(new Query(String.format("UPDATE qguilds_regions SET `upperLoc`='%s' WHERE `name`='%s'", LocationManager.fromLocation(this.upperLoc), this.name)));
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
			yml.set("guild", this.guild.getTag());
			yml.set("center", LocationManager.fromLocation(this.center));
			yml.set("size", this.size);
			yml.set("lowerLoc", LocationManager.fromLocation(this.lowerLoc));
			yml.set("upperLoc", LocationManager.fromLocation(this.upperLoc));
			try {
				yml.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Bukkit.getPluginManager().callEvent(new DataSaveEvent(DataSaveType.REGION));
		this.setChanged(false);
		
	}
	
}


