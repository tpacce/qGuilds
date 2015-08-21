package pl.za.xvacuum.guilds.commands.user;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import pl.za.xvacuum.guilds.Main;
import pl.za.xvacuum.guilds.commands.Executor;
import pl.za.xvacuum.guilds.managers.DataManager;
import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.objects.Guild;
import pl.za.xvacuum.guilds.objects.Region;
import pl.za.xvacuum.guilds.objects.User;
import pl.za.xvacuum.guilds.utils.Util;

public class EnlargeCommand extends Executor{
	
	private static final FileConfiguration cfg = Main.getInstance().getConfig();

	public EnlargeCommand() {
		super(cfg.getString("commands.enlarge.name"), 
				cfg.getString("commands.enlarge.description"), 
				cfg.getString("commands.enlarge.usage"), "enlarge", 
				cfg.getStringList("commands.enlarge.aliases"), true);
		
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		Player p = (Player)sender;
		User u = User.get(p);
		if(!u.hasGuild()) {
			Util.sendMessage(p, Messages.PlayerDontHasGuild);
			return;
		}
		Guild g = u.getGuild();
		Region r = g.getRegion();
		if(!u.hasPermission(g)) {
			Util.sendMessage(p, Messages.PlayerDontHasPerm);
			return;
		}
		if(r.getSize() >= cfg.getInt("enlarge-max-radius")) {
			Util.sendMessage(p, Messages.EnlargeMaxSize);
			return;
		}
		int cost = Util.calculateEnlargeCost(g.getRegion());
		ItemStack item = new ItemStack(Material.getMaterial(cfg.getString("enlarge-item")), cost);
		if(!p.getInventory().containsAtLeast(item, cost))
		{
			Util.sendMessage(p, Messages.EnlargeNoItems.replace("%amount%", String.valueOf(cost)).replace("%item%", cfg.getString("enlarge-item").toLowerCase().replace("_", "")));
			return;
		}
		r.setSize(r.getSize() + cfg.getInt("enlarge-radius"));
		p.getInventory().removeItem(new ItemStack(item.getType(), cost));
		r.save();
		DataManager.loadRegions(false);
		Util.sendMessage(p, Messages.EnlargeSuccess.replace("%actualSize%", String.valueOf(r.getSize())));
		return;
	}

}
