package pl.za.xvacuum.guilds.commands.admin;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import pl.za.xvacuum.guilds.commands.Executor;
import pl.za.xvacuum.guilds.managers.DataManager;
import pl.za.xvacuum.guilds.managers.GuildManager;
import pl.za.xvacuum.guilds.managers.RankManager;
import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.objects.Guild;
import pl.za.xvacuum.guilds.objects.User;
import pl.za.xvacuum.guilds.utils.Util;

public class DeleteAdminCommand extends Executor{

	public DeleteAdminCommand() {
		super("admin-delete", "Delete command", "/admin-delete [tag]", "admin.delete", Arrays.asList("usun", "rozwiaz"), false);
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		CommandSender s = sender;
		if(args.length != 1) {
			Util.sendMessage(s, Messages.CommandBadUsage.replace("%usage%", getUsage()));
			return;
		}
		if(!GuildManager.tagExists(args[0])) {
			Util.sendMessage(s, "&cGuild cannot be null (tag doesnt exists)");
			return;
		}
		Guild g = Guild.get(args[0]);
		String tag = g.getTag();
		String name = g.getName();
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
		Bukkit.broadcastMessage(Util.setHEX(Messages.DeleteBroadcast.replace("%tag%", tag).replace("%name%", name).replace("%player%", s.getName())));
	}

}
