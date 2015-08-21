package pl.za.xvacuum.guilds.commands.user;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import pl.za.xvacuum.guilds.Main;
import pl.za.xvacuum.guilds.commands.Executor;
import pl.za.xvacuum.guilds.managers.DataManager;
import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.objects.Guild;
import pl.za.xvacuum.guilds.objects.User;
import pl.za.xvacuum.guilds.utils.Util;

public class BroadcastCommand extends Executor{
	
	private static final FileConfiguration cfg = Main.getInstance().getConfig();

	public BroadcastCommand() {
		super(cfg.getString("commands.broadcast.name"), 
				cfg.getString("commands.broadcast.description"), 
				cfg.getString("commands.broadcast.usage"), "broadcast", 
				cfg.getStringList("commands.broadcast.aliases"), true);
		
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
		if(!u.hasPermission(g)) {
			Util.sendMessage(p, Messages.PlayerDontHasPerm);
			return;
		}
		StringBuilder mb = new StringBuilder();
	    for (String a : args) {
	    	if (mb.length() > 0) {
	        	mb.append(" ");
	        }
	    	mb.append(a);
		}
	    g.setBroadcast(mb.toString());
	    g.save();
	    DataManager.loadGuilds(false);
		for(User m : g.getMembers()) {
			Util.sendMessage(m.getPlayer(), Messages.BroadcastNotify.replace("%player%", p.getName()).replace("%broadcast%", mb.toString()));
		}
		return;
	}


}
