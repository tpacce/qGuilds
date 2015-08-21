package pl.za.xvacuum.guilds.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.entity.Player;

import pl.za.xvacuum.guilds.objects.Guild;
import pl.za.xvacuum.guilds.objects.Rank;
import pl.za.xvacuum.guilds.objects.Rank.RankType;
import pl.za.xvacuum.guilds.objects.User;

public class RankManager {
	

	private static List<Rank> users = new ArrayList<Rank>();
	private static List<Rank> guilds = new ArrayList<Rank>();
	
	public static List<Rank> getRanksUser(){
		return users;
	}
	
	public static List<Rank> getRanksGuild(){
		return guilds;
	}
	
	public static void updateAll(){
		Collections.sort(users);
		Collections.sort(guilds);
	}
	
	public static void update(User u){
		if(!users.contains(u.getRank())) users.add(u.getRank());
		Collections.sort(users);
		if(u.hasGuild()) update(u.getGuild());
	}
	
	public static void update(Guild g){
		if(!guilds.contains(g.getRank())) guilds.add(g.getRank());
		else {
			Collections.sort(guilds);
		}
	}
	
	public static void add(Rank r){
		if(r.getType().equals(RankType.USER)){
			users.add(r);
		}
		else{
			guilds.add(r);
		}
		updateAll();
	}
	
	public static void remove(Rank r){
		if(r.getType().equals(RankType.USER)){
			users.remove(r);
		}
		else{
			guilds.remove(r);
		}
		updateAll();
	}
	
	public static Rank byTag(String tag){
		for(Rank r : guilds){
			if(r.getGuild().getTag().equalsIgnoreCase(tag)) return r;
		}
		return null;
	}
	
	public static int position(User u){
		return users.indexOf(u.getRank()) + 1;
	}
	
	public static int position(Guild g){
		return guilds.indexOf(g.getRank()) + 1;
	}
	
	public static User byPositionUser(int pos){
		if (pos - 1 < UserManager.get().size()) return users.get(pos-1).getUser();
		return null;
	}
	
	public static Guild byPositionGuild(int pos){
		if (pos - 1 < GuildManager.get().size()) return guilds.get(pos-1).getGuild();
		return null;
	}
	
	public static int calc(Player victim, Player damager) {
		User v = User.get(victim);
		User d = User.get(damager);
		int x = v.getRank().getPoints() - d.getRank().getPoints() + 300;
		int damager_reward = 0;
		int victim_reward = 0;
		if (x >= 0) {
			damager_reward = (int)(Math.round(Math.pow(x / 20, 0.95D)) + 30L);
			victim_reward = -(int)(Math.round(Math.pow(x / 20, 0.9D)) + 20L);
		} else {
			damager_reward = (int)-Math.round(Math.pow(Math.abs(x / 34), 0.59D)) + 3;
			victim_reward = (int)-Math.round(Math.pow(Math.abs(x / 22), 0.46D));
		}
		
		int dP = d.getRank().getPoints();
		int vP = v.getRank().getPoints();
		
	    dP += damager_reward;
	    vP += victim_reward;
	    
	    d.getRank().setPoints(dP);
	    v.getRank().setPoints(vP);
	    
	    d.getRank().setKills(d.getRank().getKills() + 1);
	    v.getRank().setDeaths(v.getRank().getDeaths() + 1);
	    
	    RankManager.update(d);
	    RankManager.update(v);
	    
	    v.save();
	    d.save();
	    DataManager.reload(false, false);
	    
	    return damager_reward;
	}
}


