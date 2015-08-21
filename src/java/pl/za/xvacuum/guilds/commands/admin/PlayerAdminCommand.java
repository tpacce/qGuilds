package pl.za.xvacuum.guilds.commands.admin;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pl.za.xvacuum.guilds.commands.Executor;
import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.misc.files.Folders;
import pl.za.xvacuum.guilds.objects.User;
import pl.za.xvacuum.guilds.utils.Util;

public class PlayerAdminCommand extends Executor{

	public PlayerAdminCommand() {
		super("admin-player", "Checks player information", "/admin-player <player>", "admin.player", Arrays.asList("gracz"), false);
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		CommandSender s = sender;
		if(args.length != 1) 
		{
			Util.sendMessage(s, Messages.CommandBadUsage.replace("%usage%", getUsage()));
			return;
		}
		Player player = Bukkit.getPlayer(args[0]);
		if(player == null)
		{
			Util.sendMessage(s, "&cPlayer cannot be null! (is offline)");
			return;
		}
		User u = User.get(player);
		if(!Folders.exists(u)) 
		{
			Util.sendMessage(s, "&cPlayer cannot be null! (unknown user data)");
			return;
		}
		Util.sendMessage(s, "&aPlayer: " + u.getName());
		Util.sendMessage(s, "&aRank: " + u.getRank().getPoints() + " [K:" + u.getRank().getKills() + ", D:" + u.getRank().getDeaths() + "]");
		if(u.hasGuild()) Util.sendMessage(s, "&aGuild: " + u.getGuild().getName() + " [" + u.getGuild().getTag() + "]");
		else{
			Util.sendMessage(s, "&aGuild: " + Messages.UnknownValue);
		}
		
		
		return;
	}

}
