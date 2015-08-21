package pl.za.xvacuum.guilds.commands.user;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import pl.za.xvacuum.guilds.Main;
import pl.za.xvacuum.guilds.commands.Executor;
import pl.za.xvacuum.guilds.managers.RankManager;
import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.objects.Guild;
import pl.za.xvacuum.guilds.objects.User;
import pl.za.xvacuum.guilds.utils.Util;

public class TopCommand extends Executor{
	
	private static final FileConfiguration cfg = Main.getInstance().getConfig();

	public TopCommand() {
		super(cfg.getString("commands.top.name"), 
				cfg.getString("commands.top.description"), 
				cfg.getString("commands.top.usage"), "top", 
				cfg.getStringList("commands.top.aliases"), true);
		
	}
	

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		Player p = (Player)sender;
		User u = User.get(p);
		if(u.hasGuild()) {
			Guild g = u.getGuild();
			Util.sendMessage(p, Messages.TopLine);
			for(int i=1; i<=10; i++){
				if(i <= RankManager.getRanksGuild().size()){
					Guild topg = RankManager.byPositionGuild(i);
					if(g != topg) Util.sendMessage(p, Messages.TopPrefixGuild.replace("%pos%", String.valueOf(i)).replace("%tag%", topg.getTag()).replace("%points%", String.valueOf(topg.getRank().getPoints())).replace("%kills%", String.valueOf(topg.getRank().getKills())).replace("%deaths%", String.valueOf(topg.getRank().getDeaths())));
					else{
						Util.sendMessage(p, Messages.TopPrefixOwnGuild.replace("%pos%", String.valueOf(i)).replace("%tag%", topg.getTag()).replace("%points%", String.valueOf(topg.getRank().getPoints())).replace("%kills%", String.valueOf(topg.getRank().getKills())).replace("%deaths%", String.valueOf(topg.getRank().getDeaths())));
					}
				} else{
					Util.sendMessage(p, Messages.TopPrefixNone.replace("%pos%", String.valueOf(i)));
				}
			}
			Util.sendMessage(p, Messages.TopLine);
			return;
		}
		Util.sendMessage(p, Messages.TopLine);
		for(int i=1; i<=10; i++){
			if(i <= RankManager.getRanksGuild().size()){
				Guild topg = RankManager.byPositionGuild(i);
				Util.sendMessage(p, Messages.TopPrefixGuild.replace("%pos%", String.valueOf(i)).replace("%tag%", topg.getTag()).replace("%points%", String.valueOf(topg.getRank().getPoints())).replace("%kills%", String.valueOf(topg.getRank().getKills())).replace("%deaths%", String.valueOf(topg.getRank().getDeaths())));
			} else{
				Util.sendMessage(p, Messages.TopPrefixNone.replace("%pos%", String.valueOf(i)));
			}
		}
		Util.sendMessage(p, Messages.TopLine);
		return;
		
	}

}
