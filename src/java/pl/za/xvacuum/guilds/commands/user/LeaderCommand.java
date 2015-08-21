package pl.za.xvacuum.guilds.commands.user;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import pl.za.xvacuum.guilds.Main;
import pl.za.xvacuum.guilds.commands.Executor;
import pl.za.xvacuum.guilds.managers.DataManager;
import pl.za.xvacuum.guilds.managers.UserManager;
import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.objects.Guild;
import pl.za.xvacuum.guilds.objects.User;
import pl.za.xvacuum.guilds.utils.Util;

public class LeaderCommand extends Executor {
	
	private static final FileConfiguration cfg = Main.getInstance().getConfig();
	
	public LeaderCommand() {
		super(cfg.getString("commands.leader.name"), 
				cfg.getString("commands.leader.description"), 
				cfg.getString("commands.leader.usage"), "leader", 
				cfg.getStringList("commands.leader.aliases"), true);
		
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		Player p = (Player)sender;
		if(args.length < 1) {
			Util.sendMessage(p, Messages.CommandBadUsage.replace("%usage%", getUsage()));
			return;
		}
		User u = User.get(p);
		if(!u.hasGuild()) {
			Util.sendMessage(p, Messages.PlayerDontHasGuild);
			return;
		}
		Guild g = u.getGuild();
		if(!u.isLeader(g)) {
			Util.sendMessage(p, Messages.LeaderNoPerm);
			return;
		}
		if(!UserManager.playedBefore(args[0])) {
			Util.sendMessage(p, Messages.PlayerNotPlayedBefore);
			return;
		}
		User nl = User.get(args[0]);
		if(nl.getGuild() != u.getGuild()) {
			Util.sendMessage(p, Messages.PlayerNotInGuild);
			return;
		}
		g.setLeader(nl);
		g.save();
		nl.save();
		u.save();
		DataManager.reload(false, false);
		Bukkit.broadcastMessage(Util.setHEX(Messages.LeaderBroadcast.replace("%player%", nl.getName()).replace("%tag%", g.getTag()).replace("%name%", g.getName())));
		return;
	}

}
