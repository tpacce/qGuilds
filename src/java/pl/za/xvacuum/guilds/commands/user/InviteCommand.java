package pl.za.xvacuum.guilds.commands.user;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import pl.za.xvacuum.guilds.Main;
import pl.za.xvacuum.guilds.commands.Executor;
import pl.za.xvacuum.guilds.managers.InviteManager;
import pl.za.xvacuum.guilds.managers.UserManager;
import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.objects.User;
import pl.za.xvacuum.guilds.utils.Util;

public class InviteCommand extends Executor {
	
	private static final FileConfiguration cfg = Main.getInstance().getConfig();
	
	public InviteCommand() {
		super(cfg.getString("commands.invite.name"), 
				cfg.getString("commands.invite.description"), 
				cfg.getString("commands.invite.usage"), "invite", 
				cfg.getStringList("commands.invite.aliases"), true);
		
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
		if(!u.hasGuild()) {
			Util.sendMessage(p, Messages.PlayerDontHasGuild);
			return;
		}
		Player invited = Bukkit.getPlayer(args[0]);
		if(invited == null) {
			Util.sendMessage(p, Messages.PlayerIsntOnline);
			return;
		}
		User inv = User.get(invited);
		if(inv.hasGuild()) {
			Util.sendMessage(p, Messages.PlayerHasGuildOther);
			return;
		}
		if(inv.isInvited(u.getGuild())) {
			u.getGuild().removeInvite(inv);
			Bukkit.getScheduler().cancelAllTasks();
			Util.sendMessage(p, Messages.InviteCancelledPlayer.replace("%player%", inv.getName()));
			Util.sendMessage(inv.getPlayer(), Messages.InviteCancelledInvited.replace("%tag%", u.getGuild().getTag()));
			u.getGuild().save();
			u.save();
			return;
		}
		InviteManager.invite(u, inv, u.getGuild());
		u.getGuild().save();
		u.save();
		return;
	}

}
