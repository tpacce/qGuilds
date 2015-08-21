package pl.za.xvacuum.guilds.utils;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;

import pl.za.xvacuum.guilds.managers.RankManager;
import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.misc.Tablist;
import pl.za.xvacuum.guilds.objects.Guild;
import pl.za.xvacuum.guilds.objects.User;
import pl.za.xvacuum.guilds.utils.Logger.LogType;

public class ReplaceUtils {
	
	private static int arraysRange = 0;
	
	public static String replaceRank(String string) {
		if(!string.contains("top>")) return null;
		StringBuilder sb = new StringBuilder();
		boolean o = false;
		boolean s = false;
		for(char c : string.toCharArray()){
			boolean e = false;
			switch(c){
				case '[': o = true; break;  // method from FunnyGuilds parser
				case '>': s = true; break;
				case ']': e = true; break;
				default: if(o && s) sb.append(c);
			}
			if(e) break;
		}
		try {
			int i = Integer.valueOf(sb.toString());
			if(string.contains("gtop")){
				
				Guild g = RankManager.byPositionGuild(i);
				
				if(g != null) {
					return StringUtils.replace(string, "[gtop>" + Integer.toString(i) + ']',g.getTag() + " " + StringUtils.replace(Tablist.tablistGuildTop, "%points%", Integer.toString(g.getRank().getPoints())));
				}
				else {
					return StringUtils.replace(string, "[gtop>" + Integer.toString(i) + ']', Messages.UnknownValue);
				}
			}else if(string.contains("ptop")){
				User u = RankManager.byPositionUser(i);
				if(u != null) 
					return StringUtils.replace(string, "[ptop>" + Integer.toString(i) + ']', u.getName());
				else {
					return StringUtils.replace(string, "[ptop>" + Integer.toString(i) + ']', Messages.UnknownValue);
				}
			}
		} catch (NumberFormatException e){
			Logger.log(LogType.ERROR, "Unknown number format: " + sb.toString());
			e.printStackTrace();
		}
		return null;
	}
	
	public static String replaceOnline(User user, String string) {
		if(!string.contains("online>")) return null;
		StringBuilder sb = new StringBuilder();
		boolean o = false;
		boolean s = false;
		for(char c : string.toCharArray()){
			boolean e = false;
			switch(c){
				case '[': o = true; break;  // method from FunnyGuilds parser
				case '>': s = true; break;
				case ']': e = true; break;
				default: if(o && s) sb.append(c);
			}
			if(e) break;
		}
		try {
			int i = Integer.valueOf(sb.toString());
			if(user.hasGuild()) {
				if(arraysRange != user.getGuild().getMembers().size()) {
					User u = user.getGuild().getMembers().get(i);
					arraysRange++;
					if(u.isOnline()) 
						return StringUtils.replace(string, "[online>" + Integer.toString(i) + ']', u.getName());
					else {
						return StringUtils.replace(string, "[online>" + Integer.toString(i) + ']', Messages.UnknownValue);
					}
				}else return StringUtils.replace(string, "[online>" + Integer.toString(i) + ']', Messages.UnknownValue);
			}
			else return StringUtils.replace(string, "[online>" + Integer.toString(i) + ']', Messages.UnknownValue);
		} catch (NumberFormatException e){
			Logger.log(LogType.ERROR, "Unknown number format: " + sb.toString());
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	public static String replace(Player player, String string) {
		User u = User.get(player);
		string = StringUtils.replace(string, "%tag%", u.hasGuild() ? u.getGuild().getTag() : Messages.UnknownValue);
		string = StringUtils.replace(string, "%guild%", u.hasGuild() ? u.getGuild().getName() : Messages.UnknownValue);
		string = StringUtils.replace(string, "%points%", String.valueOf(u.getRank().getPoints()));
		string = StringUtils.replace(string, "%kills%", String.valueOf(u.getRank().getKills()));
		string = StringUtils.replace(string, "%deaths%", String.valueOf(u.getRank().getDeaths()));
		string = StringUtils.replace(string, "%name%", u.getName());
		string = StringUtils.replace(string, "%born%", String.valueOf(u.hasGuild() ? Util.parseTime(u.getGuild().getBorn()) : Messages.UnknownValue));
		string = StringUtils.replace(string, "%pvp%", u.hasGuild() ? BooleanUtils.parseBooleanMode(u.getGuild().isPvp()) : Messages.UnknownValue);
		String r = replaceRank(string);
		String o = replaceOnline(u, string);
		if(o != null) string = o;
		if(r != null) string = r;
		return string;
	}

}
