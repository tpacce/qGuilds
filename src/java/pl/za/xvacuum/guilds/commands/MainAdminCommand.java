package pl.za.xvacuum.guilds.commands;

import java.util.Arrays;

import org.bukkit.command.CommandSender;

import pl.za.xvacuum.guilds.Main;
import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.utils.Util;

public class MainAdminCommand extends Executor{

	public MainAdminCommand() {
		super("ga", "Global admin command", "/ga", "admin", Arrays.asList("guildadmin", "adminguild"), false);
		
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		CommandSender s = sender;
		if(args.length != 0) {
			Util.sendMessage(s, Messages.CommandBadUsage.replace("%usage%", getUsage()));
			return;
		}
		Util.sendMessage(s, "&a/admin-player [player] &8- &7Player information.");
		Util.sendMessage(s, "&a/admin-delete [tag] &8- &7Delete guild.");
		Util.sendMessage(s, "");
		Util.sendMessage(s, "&7&oPlugin version: &l" + Main.getInstance().getDescription().getVersion());
		Util.sendMessage(s, "&7&oMore commands in admin management coming soon!");
		return;
	}

}
