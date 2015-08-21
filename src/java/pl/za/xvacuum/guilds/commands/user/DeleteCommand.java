package pl.za.xvacuum.guilds.commands.user;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import pl.za.xvacuum.guilds.Main;
import pl.za.xvacuum.guilds.commands.Executor;
import pl.za.xvacuum.guilds.managers.DataManager;
import pl.za.xvacuum.guilds.managers.GuildManager;
import pl.za.xvacuum.guilds.managers.RankManager;
import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.objects.Guild;
import pl.za.xvacuum.guilds.objects.User;
import pl.za.xvacuum.guilds.tags.TagChanger;
import pl.za.xvacuum.guilds.utils.Util;

public class DeleteCommand extends Executor{
	
	private static final FileConfiguration cfg = Main.getInstance().getConfig();

	public DeleteCommand() {
		super(cfg.getString("commands.delete.name"), 
				cfg.getString("commands.delete.description"), 
				cfg.getString("commands.delete.usage"), "create", 
				cfg.getStringList("commands.delete.aliases"), true);
		
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		Player p = (Player)sender;
		if(args.length > 0){
			Util.sendMessage(p, Messages.CommandBadUsage.replace("%usage%", getUsage()));
			return;
		}
		User u = User.get(p);
		if(!u.hasGuild())
		{
			Util.sendMessage(p, Messages.PlayerDontHasGuild);
			return;
		}
		Guild g = u.getGuild();
		String tag = g.getTag();
		String name = g.getName();
		u.setGuild(null);
		u.save();
		TagChanger.removeGuild(g);
		
		g.getRegion().deleteDragonEgg();
		GuildManager.remove(g);
		DataManager.delete(g.getRegion());
		for(User us : g.getMembers()) {
			us.setTitle(null);
			us.setGuild(null);
		}
		g.getRegion().delete();
		for(Guild gu : g.getAllies()) g.removeAlly(gu);
		for(Guild gu : GuildManager.get()){
			if(g.getAllyInvites().contains(gu)) g.removeAllyInvite(g);
		}
		RankManager.remove(g.getRank());
		DataManager.delete(g);
		
		DataManager.reload(false, false);
		Bukkit.broadcastMessage(Util.setHEX(Messages.DeleteBroadcast.replace("%tag%", tag).replace("%name%", name).replace("%player%", p.getName())));
		return;
		
	}
}