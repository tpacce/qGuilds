package pl.za.xvacuum.guilds.commands.user;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import pl.za.xvacuum.guilds.Main;
import pl.za.xvacuum.guilds.commands.Executor;
import pl.za.xvacuum.guilds.managers.DataManager;
import pl.za.xvacuum.guilds.managers.GuildManager;
import pl.za.xvacuum.guilds.managers.RankManager;
import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.objects.Guild;
import pl.za.xvacuum.guilds.objects.User;
import pl.za.xvacuum.guilds.tags.TagChanger;
import pl.za.xvacuum.guilds.utils.Parser;
import pl.za.xvacuum.guilds.utils.Util;

public class JoinCommand extends Executor{

	private static final FileConfiguration cfg = Main.getInstance().getConfig();
	
	public JoinCommand() {
		super(cfg.getString("commands.join.name"), 
				cfg.getString("commands.join.description"), 
				cfg.getString("commands.join.usage"), "join", 
				cfg.getStringList("commands.join.aliases"), true);
		
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		Player p = (Player)sender;
		if(args.length < 1) {
			Util.sendMessage(p, Messages.CommandBadUsage.replace("%usage%", getUsage()));
			return;
		}
		User u = User.get(p);
		if(u.hasGuild()) {
			Util.sendMessage(p, Messages.PlayerHasGuild);
			return;
		}
		if(!GuildManager.tagExists(args[0])) {
			Util.sendMessage(p, Messages.GuildNotExist);
			return;
		}
		Guild g = Guild.get(args[0]);
		if(!u.isInvited(g)) {
			Util.sendMessage(p, Messages.JoinNotInvited);
			return;
		}
		ItemStack i = Parser.parseItem(cfg.getString("join-item"));
		int cost = Util.calculateJoinCost(i.getAmount(), g);
		if(!p.getInventory().containsAtLeast(i, cost))
		{
			Util.sendMessage(p, Messages.JoinDontHaveItems.replace("%item%", i.getType().toString().toLowerCase().replace("_", " ")).replace("%amount%", String.valueOf(i.getAmount())));
			return;
		}
		p.getInventory().removeItem(i);
		g.addMember(u);
		g.removeInvite(u);
		u.setGuild(g);
		g.save();
		u.save();
		TagChanger.joinToGuild(g, p);
		DataManager.reload(false, false);
		Bukkit.broadcastMessage(Util.setHEX(Messages.JoinBroadcast.replace("%player%", p.getName()).replace("%tag%", g.getTag()).replace("%name%", g.getName())));
		RankManager.updateAll();
	}
}
