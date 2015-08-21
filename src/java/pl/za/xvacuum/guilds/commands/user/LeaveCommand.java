package pl.za.xvacuum.guilds.commands.user;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import pl.za.xvacuum.guilds.Main;
import pl.za.xvacuum.guilds.commands.Executor;
import pl.za.xvacuum.guilds.managers.DataManager;
import pl.za.xvacuum.guilds.managers.RankManager;
import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.objects.Guild;
import pl.za.xvacuum.guilds.objects.User;
import pl.za.xvacuum.guilds.tags.TagChanger;
import pl.za.xvacuum.guilds.utils.Util;

public class LeaveCommand extends Executor{

	private static final FileConfiguration cfg = Main.getInstance().getConfig();
	
	public LeaveCommand() {
		super(cfg.getString("commands.leave.name"), 
				cfg.getString("commands.leave.description"), 
				cfg.getString("commands.leave.usage"), "leave", 
				cfg.getStringList("commands.leave.aliases"), true);
		
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		Player p = (Player)sender;
		User u = User.get(p);
		if(!u.hasGuild()) {
			Util.sendMessage(p, Messages.PlayerDontHasGuild);
			return;
		}
		Guild g = u.getGuild();
		if(u.isLeader(g)) {
			Util.sendMessage(p, Messages.LeaveIsLeader);
			return;
		}
		if(u.isMaster(g)) g.removeMaster(u);
		g.removeMember(u);
		u.setGuild(null);
		u.save();
		TagChanger.leaveFromGuild(g, p);
		g.save();
		DataManager.reload(false, false);
		Bukkit.broadcastMessage(Util.setHEX(Messages.LeaveBroadcast.replace("%player%", p.getName()).replace("%tag%", g.getTag()).replace("%name%", g.getName())));
		RankManager.updateAll();
	}
}

