package pl.za.xvacuum.guilds.commands.user;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import pl.za.xvacuum.guilds.Main;
import pl.za.xvacuum.guilds.commands.Executor;
import pl.za.xvacuum.guilds.managers.RegionManager;
import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.objects.Guild;
import pl.za.xvacuum.guilds.objects.Region;
import pl.za.xvacuum.guilds.objects.Treasure;
import pl.za.xvacuum.guilds.objects.User;
import pl.za.xvacuum.guilds.utils.Util;

public class TreasureCommand extends Executor{
	
	private static final FileConfiguration cfg = Main.getInstance().getConfig();

	public TreasureCommand() {
		super(cfg.getString("commands.treasure.name"), 
				cfg.getString("commands.treasure.description"), 
				cfg.getString("commands.treasure.usage"), "treasure", 
				cfg.getStringList("commands.treasure.aliases"), true);
		
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		Player p = (Player)sender;
		User u = User.get(p);
		if(!u.hasGuild()) {
			Util.sendMessage(p, Messages.PlayerDontHasGuild);
			return;
		}
		Region r = RegionManager.inWhich(p.getLocation());
		if(!(r == u.getGuild().getRegion())) {
			Util.sendMessage(p, Messages.PlayerMustBeInGuild);
			return;
		}
		Guild g = u.getGuild();
		Treasure t = g.getTreasure();
		Inventory i = t.getInventory(); i.clear(); i.setContents(t.getItems());
		u.getPlayer().openInventory(i);
		u.getPlayer().playSound(u.getPlayer().getLocation(), Sound.CHEST_OPEN, 1.0F, 1.0F);
		return;
	}

}
