package pl.za.xvacuum.guilds.commands.user;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import pl.za.xvacuum.guilds.Main;
import pl.za.xvacuum.guilds.commands.Executor;
import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.objects.Guild;
import pl.za.xvacuum.guilds.objects.User;
import pl.za.xvacuum.guilds.utils.EffectUtils;
import pl.za.xvacuum.guilds.utils.Parser;
import pl.za.xvacuum.guilds.utils.Util;

public class EffectCommand extends Executor{
	
	private static final FileConfiguration cfg = Main.getInstance().getConfig();

	public EffectCommand() {
		super(cfg.getString("commands.effect.name"), 
				cfg.getString("commands.effect.description"), 
				cfg.getString("commands.effect.usage"), "create", 
				cfg.getStringList("commands.effect.aliases"), true);
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		Player p = (Player)sender;
		if(args.length < 0) {
			Util.sendMessage(p, Messages.CommandBadUsage.replace("%usage%", getUsage()));
			return;
		}
		User u = User.get(p);
		if(!u.hasGuild()) {
			Util.sendMessage(p, Messages.PlayerDontHasGuild);
			return;
		}
		Guild g = u.getGuild();
		if(!u.hasPermission(g)){
			Util.sendMessage(p, Messages.PlayerDontHasPerm);
			return;
		}
		List<String> itemsList = new ArrayList<>();
		itemsList = cfg.getStringList("effect-items");
		List<ItemStack> items = new ArrayList<>();
		for(String s : itemsList)
		{
			items.add(Parser.parseItem(s));
		}
		for(ItemStack i : items){
			if(!p.getInventory().containsAtLeast(i, i.getAmount()))
			{
				Util.sendMessage(p, Messages.EffectDontHaveItems.replace("%item%", i.getType().toString().toLowerCase().replace("_", " ")).replace("%amount%", String.valueOf(i.getAmount())));
				return;
			}
		}
		if(EffectUtils.isBlocked(g)) {
			Util.sendMessage(p, Messages.EffectBadTime.replace("%date%", EffectUtils.timeRemain(g)));
			return;
		}
		for(ItemStack i : items)
		{
			p.getInventory().removeItem(i);
		}
		PotionEffect pe = EffectUtils.randomEffect().createEffect(8600, Util.randomInt(1, 4));
		for(User m : g.getMembers()) {
			Player pm = m.getPlayer();
			pm.addPotionEffect(pe, true);
		}
		g.setLastEffectTake(System.currentTimeMillis() + 1000 * Main.getInstance().getConfig().getInt("effect-time"));
		g.save();
		EffectUtils.setLastTakeEffect(g, System.currentTimeMillis() + 1000 * Main.getInstance().getConfig().getInt("effect-time"));
		Util.sendMessage(p, Messages.EffectSuccess);
		return;
		
		
	}
}
