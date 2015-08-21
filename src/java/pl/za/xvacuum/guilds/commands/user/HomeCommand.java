package pl.za.xvacuum.guilds.commands.user;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import pl.za.xvacuum.guilds.Main;
import pl.za.xvacuum.guilds.commands.Executor;
import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.objects.Guild;
import pl.za.xvacuum.guilds.objects.User;
import pl.za.xvacuum.guilds.utils.Util;

public class HomeCommand extends Executor{
	
	private static final FileConfiguration cfg = Main.getInstance().getConfig();

	public HomeCommand() {
		super(cfg.getString("commands.home.name"), 
				cfg.getString("commands.home.description"), 
				cfg.getString("commands.home.usage"), "home", 
				cfg.getStringList("commands.home.aliases"), true);
		
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		Player p = (Player)sender;
		User u = User.get(p);
		if(!u.hasGuild()){
			Util.sendMessage(p, Messages.PlayerDontHasGuild);
			return;
		}
		Guild g = u.getGuild();
		Location loc = g.getHome();
		Util.teleportDelay(p, loc);
		return;
	}

}
