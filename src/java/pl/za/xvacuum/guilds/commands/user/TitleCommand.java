package pl.za.xvacuum.guilds.commands.user;

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

public class TitleCommand extends Executor{
	
	private static final FileConfiguration cfg = Main.getInstance().getConfig();

	public TitleCommand() {
		super(cfg.getString("commands.title.name"), 
				cfg.getString("commands.title.description"), 
				cfg.getString("commands.title.usage"), "title", 
				cfg.getStringList("commands.title.aliases"), true);
		
	}
	

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		Player p = (Player)sender;
		if(args.length != 2) {
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
			Util.sendMessage(p, Messages.PlayerMustBeLeader);
			return;
		}
		String name = args[0];
		if(!UserManager.playedBefore(name)) {
			Util.sendMessage(p, Messages.PlayerNotPlayedBefore);
			return;
		}
		User user = User.get(name);
		String title = args[1];
		user.setTitle(title);
		user.save();
		DataManager.loadUsers(false);
		Util.sendMessage(p, Messages.TitleSuccess);
		return;
	}

}
