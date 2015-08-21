package pl.za.xvacuum.guilds.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import pl.za.xvacuum.guilds.Main;
import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.utils.Util;

public class MainCommand extends Executor{

	private static final FileConfiguration cfg = Main.getInstance().getConfig();

	public MainCommand() {
		super(cfg.getString("commands.main.name"), 
				cfg.getString("commands.main.description"), 
				cfg.getString("commands.main.usage"), "main", 
				cfg.getStringList("commands.main.aliases"), false);
		
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		CommandSender s = sender;
		if(args.length != 0) {
			Util.sendMessage(s, Messages.CommandBadUsage.replace("%usage%", getUsage()));
			return;
		}
		for(String str : Messages.MainContent) {
			Util.sendMessage(s, str
					.replace("%desc:create%", cfg.getString("commands.create.description"))
					.replace("%desc:delete%", cfg.getString("commands.delete.description"))
					.replace("%desc:home%", cfg.getString("commands.home.description"))
					.replace("%desc:info%", cfg.getString("commands.info.description"))
					.replace("%desc:invite%", cfg.getString("commands.invite.description"))
					.replace("%desc:join%", cfg.getString("commands.join.description"))
					.replace("%desc:kick%", cfg.getString("commands.kick.description"))
					.replace("%desc:leader%", cfg.getString("commands.leader.description"))
					.replace("%desc:leave%", cfg.getString("commands.leave.description"))
					.replace("%desc:master%", cfg.getString("commands.master.description"))
					.replace("%desc:pvp%", cfg.getString("commands.pvp.description"))
					.replace("%desc:sethome%", cfg.getString("commands.sethome.description"))
					.replace("%desc:title%", cfg.getString("commands.title.description"))
					.replace("%desc:treasure%", cfg.getString("commands.treasure.description"))
					.replace("%desc:broadcast%", cfg.getString("commands.broadcast.description"))
					.replace("%desc:enlarge%", cfg.getString("commands.enlarge.description"))
					.replace("%desc:ally%", cfg.getString("commands.ally.description"))
					.replace("%desc:effect%", cfg.getString("commands.effect.description"))
					);
		}
	}

}
