package pl.za.xvacuum.guilds.commands.user;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import pl.za.xvacuum.guilds.Main;
import pl.za.xvacuum.guilds.commands.Executor;
import pl.za.xvacuum.guilds.managers.GuildManager;
import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.objects.Guild;
import pl.za.xvacuum.guilds.objects.User;
import pl.za.xvacuum.guilds.tags.TagChanger;
import pl.za.xvacuum.guilds.utils.Util;

public class AllyCommand extends Executor{
	
	private static final FileConfiguration cfg = Main.getInstance().getConfig();

	public AllyCommand() {
		super(cfg.getString("commands.ally.name"), 
				cfg.getString("commands.ally.description"), 
				cfg.getString("commands.ally.usage"), "ally", 
				cfg.getStringList("commands.ally.aliases"), true);
		
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		if(args.length !=1){
			Util.sendMessage(sender, Messages.CommandBadUsage.replace("%usage%", getUsage()));
			return;
		}
		Player p = (Player) sender;
		User u = User.get(p);
		if(!u.hasGuild()){
			Util.sendMessage(p, Messages.PlayerDontHasGuild);
			return;
		}
		if(!u.isLeader(u.getGuild())){
			Util.sendMessage(p, Messages.PlayerDontHasPerm);
			return;
		}
		if(!GuildManager.tagExists(args[0])){
			Util.sendMessage(p, Messages.GuildNotExist);
			return;
		}
		Guild g = Guild.get(args[0]);
		if(g.equals(u.getGuild())){
			Util.sendMessage(p, Messages.AllySameGuild);
			return;
		}
		if(u.getGuild().getAllies().contains(g)){
			u.getGuild().removeAlly(g);
			g.removeAlly(u.getGuild());
			TagChanger.removeAlliance(g, u.getGuild());
			Bukkit.broadcastMessage(Util.setHEX(Messages.AllyBroadcastBreak.replace("%tag1%", u.getGuild().getTag()).replace("%tag2%", g.getTag())));
			return;
		}
		if(u.getGuild().getAllyInvites().contains(g)){
			u.getGuild().removeAllyInvite(g);
			u.getGuild().addAlly(g);
			g.addAlly(u.getGuild());
			TagChanger.createAlliance(g, u.getGuild());
			Bukkit.broadcastMessage(Util.setHEX(Messages.AllyBroadcast.replace("%tag1%", u.getGuild().getTag()).replace("%tag2%", g.getTag())));
			g.save();
			u.save();
			return;
		}
		if(g.getAllyInvites().contains(u.getGuild())){
			g.removeAllyInvite(u.getGuild());
			Util.sendMessage(p, Messages.AllyRequestCancelled);
			for(User m : g.getMembers()){
				if(m.isOnline()) Util.sendMessage(m.getPlayer(), Messages.AllyRequestCancelledToGuild.replace("%tag%", u.getGuild().getTag()));
			}
			g.save();
			return;
		}
		g.addAllyInvite(u.getGuild());
		Util.sendMessage(p, Messages.AllyRequestSuccess);
		for(User m : g.getMembers()){
			if(m.isOnline()) Util.sendMessage(m.getPlayer(), Messages.AllyRequestToGuild.replace("%tag%", u.getGuild().getTag()));
		}
		g.save();
	}
	
}