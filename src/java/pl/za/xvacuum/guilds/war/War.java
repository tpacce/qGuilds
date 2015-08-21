package pl.za.xvacuum.guilds.war;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import pl.za.xvacuum.guilds.Main;
import pl.za.xvacuum.guilds.api.events.ConquerEvent;
import pl.za.xvacuum.guilds.managers.DataManager;
import pl.za.xvacuum.guilds.managers.GuildManager;
import pl.za.xvacuum.guilds.managers.RankManager;
import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.objects.Guild;
import pl.za.xvacuum.guilds.objects.User;
import pl.za.xvacuum.guilds.utils.TimeUtils;
import pl.za.xvacuum.guilds.utils.Util;
import pl.za.xvacuum.guilds.utils.reflections.ParticleShape;

public class War {
	
	public static void attackGuild(Player p, Guild g, Block b) {
		User u = User.get(p);
		if(!u.hasGuild()) {
			Util.sendMessage(p, Messages.PlayerDontHasGuild);
			return;
		}
		if(u.getGuild() == g) {
			Util.sendMessage(p, Messages.WarCantBeatOwnGuild);
			return;
		}
		if(u.getGuild().getAllies().contains(g)) {
			Util.sendMessage(p, Messages.WarCantBeatOwnAlly);
			return;
		}
		Guild attacker = u.getGuild();
		long canAttackTime = g.getBorn() + 86400 * 1000;
		long currentTime = System.currentTimeMillis();
		if(!(canAttackTime > currentTime)) {
			int members = attacker.getMembers().size();
			int foreachMember = Math.round(1000 / members);
			Bukkit.getPluginManager().callEvent(new ConquerEvent(u, g, g.getTreasure(), foreachMember));
			Location center = g.getRegion().getCenter();
			int centerFixed = center.getBlockY() + 2;
			World w = center.getWorld();
			Location dropPlace = new Location(w, center.getBlockX(), centerFixed, center.getZ());
			ItemStack[] items = g.getTreasure().getItems();
			for(ItemStack i : items) {
				if(i != null) {
					if(Util.chance(Main.getInstance().getConfig().getInt("war-items-chance"))) w.dropItem(dropPlace, i);
				}
			}
			
			for(User m : g.getMembers()) {
				m.setGuild(null);
				m.save();
			}
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
			for(User m : attacker.getMembers()) {
				m.getRank().setPoints(m.getRank().getPoints() + foreachMember);
				m.save();
			}
			b.setType(Material.AIR);
			ParticleShape.send(b.getLocation());
			Bukkit.broadcastMessage(Util.setHEX(Messages.WarBroadcast.replace("%name%", g.getName()).replace("%tag%", g.getTag()).replace("%attacker%", attacker.getTag())));
			return;
		}else {
			Util.sendMessage(p, Messages.WarBadTime.replace("%time%", TimeUtils.parseTimeDiff(canAttackTime - System.currentTimeMillis())));
			return;
		}
	}

}
