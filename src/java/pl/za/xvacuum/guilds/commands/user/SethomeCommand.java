package pl.za.xvacuum.guilds.commands.user;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import pl.za.xvacuum.guilds.Main;
import pl.za.xvacuum.guilds.commands.Executor;
import pl.za.xvacuum.guilds.managers.DataManager;
import pl.za.xvacuum.guilds.managers.RegionManager;
import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.objects.Guild;
import pl.za.xvacuum.guilds.objects.Region;
import pl.za.xvacuum.guilds.objects.User;
import pl.za.xvacuum.guilds.utils.Util;

public class SethomeCommand extends Executor{
	
	private static final FileConfiguration cfg = Main.getInstance().getConfig();

	public SethomeCommand() {
		super(cfg.getString("commands.sethome.name"), 
				cfg.getString("commands.sethome.description"), 
				cfg.getString("commands.sethome.usage"), "sethome", 
				cfg.getStringList("commands.sethome.aliases"), true);
		
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
		Region r = RegionManager.inWhich(p.getLocation());
		if(!(r == g.getRegion())) {
			Util.sendMessage(p, Messages.PlayerMustBeInGuild);
			return;
		}
		Location loc = p.getLocation();
		g.setHome(loc);
		g.save();
		DataManager.reload(false, false);
		Util.sendMessage(p, Messages.SethomeSuccess);
		return;
	}

}
