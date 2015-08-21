package pl.za.xvacuum.guilds.objects;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.utils.ItemSerializer;
import pl.za.xvacuum.guilds.utils.Util;

public class Treasure {
	
	private Guild guild;
	private Inventory inventory = Bukkit.createInventory(null, 54, Util.setHEX(Messages.TreasureTitle));
	private ItemStack[] items = new ItemStack[54];
	
	public Treasure(Guild guild) {
		this.setGuild(guild);
	}
	
	public Treasure(Guild guild, String content) {
		ItemStack[] items = ItemSerializer.deserialize(content);
		if(items.length > this.items.length) this.items = Arrays.copyOfRange(items, 0, this.items.length);
		else{
			this.items = items;
		}
	}
	
	public ItemStack[] getItems() {
		return this.items;
	}
	
	public void setItems(ItemStack[] items) {
		this.items = items;
	}
	
	public Inventory getInventory() {
		return this.inventory;
	}
	
	public String getContents() {
		return ItemSerializer.serialize(this.items);
	}

	public Guild getGuild() {
		return guild;
	}

	public void setGuild(Guild guild) {
		this.guild = guild;
	}
	

}
