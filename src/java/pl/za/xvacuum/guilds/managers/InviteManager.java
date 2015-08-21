package pl.za.xvacuum.guilds.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import pl.za.xvacuum.guilds.Main;
import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.objects.Guild;
import pl.za.xvacuum.guilds.objects.User;
import pl.za.xvacuum.guilds.utils.Util;

public class InviteManager {
	
	public static void setup() {
		for(Guild g : GuildManager.get()) {
			g.getInvited().clear();
		}
	}
	
	public static void add(Guild g, User u) {
		g.addInvite(u);
	}
	
	public static void remove(Guild g, User u) {
		g.removeInvite(u);
	}
	
	public static boolean isInvited(Guild g, User u) {
		return g.getInvited().contains(u);
	}
	
	public static void invite(final User from, final User invited, final Guild to) {
		Player fr = from.getPlayer();
		if(invited.hasGuild()) { 
			Util.sendMessage(fr, Messages.InviteUserHasGuild);
			return;
		}
		Player inv = invited.getPlayer();
		add(to, invited);
		Util.sendMessage(fr, Messages.InviteSuccessPlayer.replace("%player%", inv.getName()));
		Util.sendMessage(inv, Messages.InviteSuccessInvited.replace("%tag%", to.getTag()).replace("%name%", to.getName()).replace("%player%", fr.getName()));
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable(){

			@Override
			public void run() {
				if(!invited.isInvited(to)) return;
				Util.sendMessage(invited.getPlayer(), Messages.InviteCooldownStops.replace("%tag%", to.getTag()));
				remove(to, invited);
			}
			
		}, 60L * 20);
		return;
	}

}
