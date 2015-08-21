package pl.za.xvacuum.guilds.listeners;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import pl.za.xvacuum.guilds.Main;
import pl.za.xvacuum.guilds.objects.Guild;
import pl.za.xvacuum.guilds.objects.User;
import pl.za.xvacuum.guilds.utils.Util;

public class PlayerChatListener implements Listener{
	
	private static final FileConfiguration cfg = Main.getInstance().getConfig();
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e)
	{
		Player p = e.getPlayer();
		String format = e.getFormat();
		User u = User.get(p);
		if(u.hasGuild()) {
			if(e.getMessage().startsWith("!!")) {
				e.setCancelled(true);
				String aformat = new String(cfg.getString("ally-message-format"));
				for(User m : u.getGuild().getMembers()) {
					Util.sendMessage(m.getPlayer(), aformat.replace("%player%", u.getName()).replace("%tag%", u.getGuild().getTag()).replace("%message%", e.getMessage().replace("!!", "")).replace("%title%", u.getTitle() == null ? "" : u.getTitle()));
				}
				for(Guild ally : u.getGuild().getAllies()) {
					for(User allyM : ally.getMembers()){
						Util.sendMessage(allyM.getPlayer(), aformat.replace("%player%", u.getName()).replace("%tag%", u.getGuild().getTag()).replace("%message%", e.getMessage().replace("!", "")));
					}
				}
				return;
			}else if(e.getMessage().startsWith("!")) {
				e.setCancelled(true);
				String gformat = new String(cfg.getString("guild-message-format"));
				for(User m : u.getGuild().getMembers()) {
					Util.sendMessage(m.getPlayer(), gformat.replace("%player%", u.getName()).replace("%tag%", u.getGuild().getTag()).replace("%message%", e.getMessage().replace("!", "")).replace("%title%", u.getTitle() == null ? "" : u.getTitle()));
				}
				return;
			}
		}
		format = StringUtils.replace(format, "{TAG}", u.hasGuild() ? cfg.getString("chat-tag-format").replace("%tag%", u.getGuild().getTag()) : "");
		format = StringUtils.replace(format, "{POINTS}", cfg.getString("chat-points-format").replace("%points%", String.valueOf(u.getRank().getPoints())));
		e.setFormat(Util.setHEX(format));
		return;
	}

}
