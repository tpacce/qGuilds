package pl.za.xvacuum.guilds.listeners;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import pl.za.xvacuum.guilds.managers.DataManager;
import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.objects.Guild;
import pl.za.xvacuum.guilds.objects.Treasure;
import pl.za.xvacuum.guilds.objects.User;
import pl.za.xvacuum.guilds.utils.Util;

public class InventoryListener implements Listener{
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e){
        Player p = (Player) e.getPlayer();
        User u = User.get(p);
        Guild g = u.getGuild();
        if (g == null)
            return;
        Inventory inv = e.getInventory();
        if (!Util.setHEX(Messages.TreasureTitle).equalsIgnoreCase(inv.getName()))
            return;
		Treasure t = g.getTreasure();
		t.setItems(inv.getContents()); 
		g.save();
		DataManager.loadGuilds(false);
		u.getPlayer().playSound(u.getPlayer().getLocation(), Sound.CHEST_CLOSE, 1.0F, 1.0F);
	}

}
