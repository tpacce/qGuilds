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

public class MasterCommand extends Executor{
	
	private static final FileConfiguration cfg = Main.getInstance().getConfig();
	
	public MasterCommand() {
		super(cfg.getString("commands.master.name"), 
				cfg.getString("commands.master.description"), 
				cfg.getString("commands.master.usage"), "leader", 
				cfg.getStringList("commands.master.aliases"), true);
		
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
		User nm = User.get(args[0]);
		if(nm.getGuild() != u.getGuild()) {
			Util.sendMessage(p, Messages.PlayerNotInGuild);
			return;
		}
		if(g.getMasters().contains(nm)) {
			g.removeMaster(nm);
			g.save();
			nm.save();
			DataManager.reload(false, false);
			Bukkit.broadcastMessage(Util.setHEX(Messages.MasterBroadcastDowngrade.replace("%player%", nm.getName()).replace("%tag%", g.getTag()).replace("%name%", g.getName())));
			return;
		}
		if(g.getMasters().size() > cfg.getInt("max-masters")) {
			Util.sendMessage(p, Messages.MasterFullOfMasters);
			return;
		}
		g.addMaster(nm);
		g.save();
		nm.save();
		DataManager.reload(false, false);
		Bukkit.broadcastMessage(Util.setHEX(Messages.MasterBroadcastUpgrade.replace("%player%", nm.getName()).replace("%tag%", g.getTag()).replace("%name%", g.getName())));
		return;
	}

}
