package pl.za.xvacuum.guilds.commands.user;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import pl.za.xvacuum.guilds.Main;
import pl.za.xvacuum.guilds.commands.Executor;
import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.objects.Guild;
import pl.za.xvacuum.guilds.objects.User;
import pl.za.xvacuum.guilds.utils.BooleanUtils;
import pl.za.xvacuum.guilds.utils.Util;

public class PvpCommand extends Executor{
	
	private static final FileConfiguration cfg = Main.getInstance().getConfig();
	
	public PvpCommand() {
		super(cfg.getString("commands.pvp.name"), 
				cfg.getString("commands.pvp.description"), 
				cfg.getString("commands.pvp.usage"), "pvp", 
				cfg.getStringList("commands.pvp.aliases"), true);
		
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
		if(!u.hasPermission(g)) {
			Util.sendMessage(p, Messages.PlayerDontHasPerm);
			return;
		}
		boolean pvp = g.isPvp() ? false : true;
		g.setPvp(pvp);
		for(User m : g.getMembers()) {
			Util.sendMessage(m.getPlayer(), Messages.PvpSuccess.replace("%status%", BooleanUtils.parseBooleanMode(pvp)));
		}
		return;
	}

}
