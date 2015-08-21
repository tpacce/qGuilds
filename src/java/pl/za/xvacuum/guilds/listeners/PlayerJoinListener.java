package pl.za.xvacuum.guilds.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import pl.za.xvacuum.guilds.Main;
import pl.za.xvacuum.guilds.managers.DataManager;
import pl.za.xvacuum.guilds.managers.RankManager;
import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.objects.Guild;
import pl.za.xvacuum.guilds.objects.Update;
import pl.za.xvacuum.guilds.objects.User;
import pl.za.xvacuum.guilds.tags.TagChanger;
import pl.za.xvacuum.guilds.utils.Util;
import pl.za.xvacuum.guilds.utils.reflections.TabUtils;
public class PlayerJoinListener implements Listener{
	
	protected Update update;
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		Player p = e.getPlayer();
		if(!p.hasPlayedBefore()) {
			User u = new User(p);
			DataManager.add(u);
		}
		User u = User.get(p);
		u.save();
		RankManager.update(u);
		DataManager.loadUsers(false);
		if(p.hasPermission("qguilds.update"))
		{
			this.update = new Update(Main.LINK_DEF);
			this.update.update(p);
		}
		if(u.hasGuild()) {
			Guild g = u.getGuild();
			for(User m : g.getMembers())  {
				Util.sendMessage(m.getPlayer(), Messages.EventPlayerJoinNotify.replace("%player%", p.getName()));
			}
			Util.sendMessage(p, Messages.BroadcastJoinNotify.replace("%broadcast%", g.getBroadcast()));
 		}
		u.save();
		TagChanger.register(p);
		TabUtils.sendTab(p);
		return;
	}

}
