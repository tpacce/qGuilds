package pl.za.xvacuum.guilds.commands.user;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import pl.za.xvacuum.guilds.Main;
import pl.za.xvacuum.guilds.commands.Executor;
import pl.za.xvacuum.guilds.managers.GuildManager;
import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.objects.Guild;
import pl.za.xvacuum.guilds.objects.User;
import pl.za.xvacuum.guilds.utils.BooleanUtils;
import pl.za.xvacuum.guilds.utils.Parser;
import pl.za.xvacuum.guilds.utils.Util;

public class InfoCommand extends Executor{
	
	private static final FileConfiguration cfg = Main.getInstance().getConfig();

	public InfoCommand() {
		super(cfg.getString("commands.info.name"), 
				cfg.getString("commands.info.description"), 
				cfg.getString("commands.info.usage"), "info", 
				cfg.getStringList("commands.info.aliases"), false);
		
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		CommandSender s = sender;
		if(args.length == 0){
			if(s instanceof Player)
			{
				Player p = (Player)sender;
				User u = User.get(p);
				Guild g = u.getGuild();
				if(u.hasGuild())
				{
					for(String str : Messages.InfoContent)
					{
						Util.sendMessage(p, str
								.replace("%tag%", g.getTag())
								.replace("%name%", g.getName())
								.replace("%player%", Parser.parseUser(g.getLeader()))
								.replace("%masters%", g.getMasters() == null ? Messages.UnknownValue : Parser.parseUsers(g.getMasters()))
								.replace("%members%", g.getMembers() == null ? Messages.UnknownValue : Parser.parseUsers(g.getMembers()))
								.replace("%allies%", g.getAllies() == null ? Messages.UnknownValue : g.getAllies().toString().replace("[", "").replace("]", ""))
								.replace("%points%", String.valueOf(g.getRank().getPoints()))
								.replace("%kills%", String.valueOf(g.getRank().getKills()))
								.replace("%deaths%", String.valueOf(g.getRank().getDeaths()))
								.replace("%born%", Util.parseTime(g.getBorn()))
								.replace("%pvp%", BooleanUtils.parseBooleanMode(g.isPvp())));
					}
					return;
				}
				else{
					Util.sendMessage(s, Messages.CommandBadUsage.replace("%usage%", getUsage()));
					return;
				}
			}else{
				Util.sendMessage(s, Messages.CommandBadUsage.replace("%usage%", getUsage()));
				return;
			}
		}
		if(!GuildManager.tagExists(args[0]))
		{
			Util.sendMessage(s, Messages.InfoGuildDoesntExists);
			return;
		}
		Guild g = Guild.get(args[0]);
		for(String str : Messages.InfoContent)
		{
			Util.sendMessage(s, str
					.replace("%tag%", g.getTag())
					.replace("%name%", g.getName())
					.replace("%player%", Parser.parseUser(g.getLeader()))
					.replace("%masters%", g.getMasters() == null ? Messages.UnknownValue : Parser.parseUsers(g.getMasters()))
					.replace("%members%", g.getMembers() == null ? Messages.UnknownValue : Parser.parseUsers(g.getMembers()))
					.replace("%allies%", g.getAllies() == null ? Messages.UnknownValue : g.getAllies().toString().replace("[", "").replace("]", ""))
					.replace("%points%", String.valueOf(g.getRank().getPoints()))
					.replace("%kills%", String.valueOf(g.getRank().getKills()))
					.replace("%deaths%", String.valueOf(g.getRank().getDeaths()))
					.replace("%born%", Util.parseTime(g.getBorn()))
					.replace("%pvp%", BooleanUtils.parseBooleanMode(g.isPvp())));
		}
		return;
		
		
	}
}
