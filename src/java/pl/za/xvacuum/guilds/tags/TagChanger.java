package pl.za.xvacuum.guilds.tags;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import pl.za.xvacuum.guilds.managers.GuildManager;
import pl.za.xvacuum.guilds.objects.Guild;
import pl.za.xvacuum.guilds.objects.User;
import pl.za.xvacuum.guilds.tags.Relation.RelationType;
import pl.za.xvacuum.guilds.utils.Util;

public class TagChanger {
	
	@SuppressWarnings("deprecation")
	public static void register(Player player) {
		Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
		User u = User.get(player);
		Guild g = u.getGuild();
		for(Guild guild : GuildManager.get()) {
			Team t = sb.getTeam(guild.getTag());
			if(t == null) t = sb.registerNewTeam(guild.getTag());
			RelationType rt = Relation.getRelation(guild, u);
			if(rt.equals(RelationType.MEMBER))  {
				t.setPrefix(Util.setHEX("&a[" + guild.getTag() + "] "));
			}else if (rt.equals(RelationType.ALLY)) {
				t.setPrefix(Util.setHEX("&6[" + guild.getTag() + "] "));
			}else if (rt.equals(RelationType.NEUTRAL)) {
				t.setPrefix(Util.setHEX("&c[" + guild.getTag() + "] "));
			}else {
				t.setPrefix(Util.setHEX("&c[" + guild.getTag() + "] "));
			}
		}
		Team ng = sb.getTeam("ng");
		if(ng == null) {
			ng = sb.registerNewTeam("ng");
			ng.setPrefix(Util.setHEX("&f"));
		}
		player.setScoreboard(sb);
		for(Player online : Bukkit.getOnlinePlayers()) {
			online.getScoreboard().getTeam(g != null ? g.getTag() : "ng").addPlayer(player);
			User user = User.get(online);
			Guild onlineg = user.getGuild();
			player.getScoreboard().getTeam(onlineg != null ? onlineg.getTag() : "ng").addPlayer(online);
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void joinToGuild(Guild g, Player p) {
		for (Player o : Bukkit.getOnlinePlayers()) {
			o.getScoreboard().getTeam(g.getTag()).addPlayer(p);
		}

		p.getScoreboard().getTeam(g.getTag()).setPrefix(Util.setHEX("&a[" + g.getTag() + "] "));

	}

	@SuppressWarnings("deprecation")
	public static void leaveFromGuild(Guild g, Player p) {
		for (Player o : Bukkit.getOnlinePlayers()) {
			o.getScoreboard().getTeam("ng").addPlayer(p);
		}
		if (p.isOnline()) {
			p.getPlayer().getScoreboard().getTeam(g.getTag())
			.setPrefix(Util.setHEX("&c[" + g.getTag() + "] "));
		}
	}

	public static void removeAlliance(Guild g, Guild o) {
		for (User m : o.getMembers()) {
			Team t = m.getPlayer().getScoreboard().getTeam(o.getTag());
			if (t != null) {
				t.setPrefix(Util.setHEX("&c[" + g.getTag() + "] "));
			}
		}
		for (User m : o.getMembers()) {
			Team t = m.getPlayer().getScoreboard().getTeam(g.getTag());
			if (t != null) {
				t.setPrefix(Util.setHEX("&c[" + g.getTag() + "] "));
			}
		}
	}
	
	public static void createAlliance(Guild g, Guild o) {
		for (User m : o.getMembers()) {
			Team t = m.getPlayer().getScoreboard().getTeam(o.getTag());
			if (t != null) {
				t.setPrefix(Util.setHEX("&6[" + g.getTag() + "] "));
			}
		}
		for (User m : o.getMembers()) {
			Team t = m.getPlayer().getScoreboard().getTeam(g.getTag());
			if (t != null) {
				t.setPrefix(Util.setHEX("&6[" + g.getTag() + "] "));
			}
		}
	}
	
	public static void createGuild(Guild g) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			Scoreboard sb = p.getScoreboard();

			Team t = sb.getTeam(g.getTag());
			if(t == null) t = sb.registerNewTeam(g.getTag());
			RelationType rt = Relation.getRelation(g, User.get(p));
			if(rt.equals(RelationType.MEMBER))  {
				t.setPrefix(Util.setHEX("&a[" + g.getTag() + "] "));
			}else if (rt.equals(RelationType.ALLY)) {
				t.setPrefix(Util.setHEX("&6[" + g.getTag() + "] "));
			}else if (rt.equals(RelationType.NEUTRAL)) {
				t.setPrefix(Util.setHEX("&c[" + g.getTag() + "] "));
			}else {
				t.setPrefix(Util.setHEX("&c[" + g.getTag() + "] "));
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static void removeGuild(Guild g) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			Scoreboard sb = p.getScoreboard();

			sb.getTeam(g.getTag()).unregister();

			Team noguild = sb.getTeam("ng");
			for (User m : g.getMembers()) {
				noguild.addPlayer(m.getPlayer());
			}
		}
	}

}
