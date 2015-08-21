package pl.za.xvacuum.guilds.commands.user;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import pl.za.xvacuum.guilds.Main;
import pl.za.xvacuum.guilds.commands.Executor;
import pl.za.xvacuum.guilds.managers.DataManager;
import pl.za.xvacuum.guilds.managers.GuildManager;
import pl.za.xvacuum.guilds.managers.RankManager;
import pl.za.xvacuum.guilds.managers.RegionManager;
import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.objects.Guild;
import pl.za.xvacuum.guilds.objects.Rank;
import pl.za.xvacuum.guilds.objects.Region;
import pl.za.xvacuum.guilds.objects.Treasure;
import pl.za.xvacuum.guilds.objects.User;
import pl.za.xvacuum.guilds.tags.TagChanger;
import pl.za.xvacuum.guilds.utils.BuildUtils;
import pl.za.xvacuum.guilds.utils.Parser;
import pl.za.xvacuum.guilds.utils.Util;
public class CreateCommand extends Executor{
	
	private static final FileConfiguration cfg = Main.getInstance().getConfig();

	public CreateCommand() {
		super(cfg.getString("commands.create.name"), 
				cfg.getString("commands.create.description"), 
				cfg.getString("commands.create.usage"), "create", 
				cfg.getStringList("commands.create.aliases"), true);
		
	}

	@Override
	public void onExecute(CommandSender sender, String[] args) {
		Player p = (Player)sender;
		if(args.length != 2)
		{
			Util.sendMessage(p, Messages.CommandBadUsage.replace("%usage%", getUsage()));
			return;
		}
		if(p.getWorld().equals(cfg.getString("world"))) {
			Util.sendMessage(p, Messages.CreateBadWorld);
			return;
		}
		User u = User.get(p);
		if(u.hasGuild())
		{
			Util.sendMessage(p, Messages.PlayerHasGuild);
			return;
		}
		if(!p.getWorld().getName().contains(cfg.getString("world"))) {
			
			return;
		}
		String tag = args[0];
		String name = args[1];
		if(tag.length() > cfg.getInt("tag-length-max"))
		{
			Util.sendMessage(p, Messages.CreateBadLengthTagMax.replace("%length%", String.valueOf(cfg.getInt("tag-length-max"))));
			return;
		}
		if(tag.length() < cfg.getInt("tag-length-min"))
		{
			Util.sendMessage(p, Messages.CreateBadLengthTagMin.replace("%length%", String.valueOf(cfg.getInt("tag-length-min"))));
			return;
		}
		if(name.length() > cfg.getInt("name-length-max"))
		{
			Util.sendMessage(p, Messages.CreateBadLengthNameMax.replace("%length%", String.valueOf(cfg.getInt("name-length-max"))));
			return;
		}
		if(name.length() < cfg.getInt("name-length-min"))
		{
			Util.sendMessage(p, Messages.CreateBadLengthNameMin.replace("%length%", String.valueOf(cfg.getInt("name-length-min"))));
			return;
		}
		
		if(GuildManager.nameExists(name) || GuildManager.tagExists(tag))
		{
			Util.sendMessage(p, Messages.CreateGuildExist);
			return;
		}
		Location c = p.getLocation().getBlock().getLocation();
		if (cfg.getInt("region-size") + cfg.getInt("spawn-radius") > p.getWorld().getSpawnLocation().distance(c)) {
			Util.sendMessage(p, Messages.CreateGuildSpawnProtect);
			return;
		}
		List<String> itemsList = new ArrayList<>();
		if(p.hasPermission("qguilds.vip")) itemsList = cfg.getStringList("create-items-vip");
		else{
			itemsList = cfg.getStringList("create-items");
		}
		List<ItemStack> items = new ArrayList<>();
		for(String s : itemsList)
		{
			items.add(Parser.parseItem(s));
		}
		for(ItemStack i : items){
			if(!p.getInventory().containsAtLeast(i, i.getAmount()))
			{
				Util.sendMessage(p, Messages.CreateNoItems);
				return;
			}
		}
		if(RegionManager.isNear(c))
		{
			Util.sendMessage(p, Messages.CreateGuildNear);
			return;
		}
		for(ItemStack i : items)
		{
			p.getInventory().removeItem(i);
		}
		Guild g = Guild.get(tag);
		g.setTag(tag.toString());
		g.setName(name.toString());
		g.setBorn(System.currentTimeMillis());
		g.setHome(c);
		g.setLeader(u);
		g.setPvp(false);
		List<User> members = new ArrayList<>();
		List<User> masters = new ArrayList<>();
		List<User> invited = new ArrayList<>();
		List<Guild> allies = new ArrayList<>();
		List<Guild> allyInvites = new ArrayList<>();
		members.add(u);
		g.setMembers(members);
		g.setMasters(masters);
		g.setAllies(allies);
		g.setAllyInvites(allyInvites);
		g.setInvited(invited);
		g.setRank(new Rank(g));
		g.setRegion(new Region(g, c, 50));
		g.setTreasure(new Treasure(g));
		g.setBroadcast(Messages.BroadcastDefault);
		Block b = c.getBlock();
		
		for(Location l : BuildUtils.sphere(b.getLocation(), 4, 4, false, true, 0)) {
			if(l.getBlock().getType() != Material.BEDROCK) l.getBlock().setType(Material.AIR);
		}
		int cylLocMinus = b.getLocation().getBlockY() - 2;
		Location cylLoc = new Location(b.getWorld(), b.getLocation().getBlockX(), cylLocMinus, b.getLocation().getBlockZ());
		BuildUtils.cylinder(cylLoc, 5, Material.OBSIDIAN);

		
		b.setType(Material.DRAGON_EGG);
		Block bedrockB = cylLoc.getBlock();
		bedrockB.setType(Material.BEDROCK);
		DataManager.add(g);
		DataManager.add(g.getRegion());
		u.setGuild(g);
		u.save();
		g.save();
		g.getRegion().save();
		DataManager.reload(false, false);
		RankManager.update(g);
		Bukkit.broadcastMessage(Util.setHEX(Messages.CreateBroadcast.replace("%tag%", tag).replace("%name%", name).replace("%player%", p.getName())));
		TagChanger.createGuild(g);
		TagChanger.joinToGuild(g, p);
		return;
	}

}
