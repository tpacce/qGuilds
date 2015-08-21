package pl.za.xvacuum.guilds.commands.user;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import pl.za.xvacuum.guilds.Main;
import pl.za.xvacuum.guilds.commands.Executor;
import pl.za.xvacuum.guilds.managers.DataManager;
import pl.za.xvacuum.guilds.managers.RankManager;
import pl.za.xvacuum.guilds.managers.UserManager;
import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.objects.Guild;
import pl.za.xvacuum.guilds.objects.User;
import pl.za.xvacuum.guilds.tags.TagChanger;
import pl.za.xvacuum.guilds.utils.Util;

public class KickCommand extends Executor{

	private static final FileConfiguration cfg = Main.getInstance().getConfig();
	
	public KickCommand() {
		super(cfg.getString("commands.kick.name"), 
				cfg.getString("commands.kick.description"), 
				cfg.getString("commands.kick.usage"), "kick", 
				cfg.getStringList("commands.kick.aliases"), true);
		
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		Player p = (Player)sender;
		if(args.length < 1) {
			Util.sendMessage(p, Messages.CommandBadUsage.replace("%usage%", getUsage()));
			return;
		}
		if(!UserManager.playedBefore(args[0])) {
			Util.sendMessage(p, Messages.PlayerNotPlayedBefore);
			return;
		}
		User u = User.get(p);
		User k = User.get(Bukkit.getPlayer(args[0]));
		if(!u.hasGuild()) {
			Util.sendMessage(p, Messages.PlayerDontHasGuild);
			return;
		}
		Guild g = u.getGuild();
		if(g != k.getGuild()) {
			Util.sendMessage(p, Messages.KickNotInGuild);
			return;
		}
		if(!u.hasPermission(g)) {
			Util.sendMessage(p, Messages.PlayerDontHasPerm);
			return;
		}
		if(k.isLeader(g)) {
			Util.sendMessage(p, Messages.KickCantKick);
			return;
		}
		if(k.isMaster(g)) g.removeMaster(k);
		g.removeMember(k);
		k.setGuild(null);
		g.save();
		k.save();
		TagChanger.leaveFromGuild(g, k.getPlayer());
		DataManager.reload(false, false);
		Bukkit.broadcastMessage(Util.setHEX(Messages.KickBroadcast.replace("%player%", k.getName()).replace("%tag%", g.getTag()).replace("%name%", g.getName())));
		RankManager.updateAll();
	}
}
