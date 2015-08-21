package pl.za.xvacuum.guilds.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import pl.za.xvacuum.guilds.objects.User;
import pl.za.xvacuum.guilds.utils.Logger.LogType;

public class Parser {
	
	/**
	 * @author Dzikoysk
	 * @param string
	 * @return
	 */
	
	public static ItemStack parseItem(String string){
		String item = string;
		Integer amount = Integer.parseInt(item.substring(0, item.indexOf(' ')));
		String type = item.substring(item.indexOf(' ')+1);
		if(type == null){
			type = item;
			amount = 1;
		}
		type = type.toUpperCase();
		type = type.replace(" ", "_");
		Material material = Material.getMaterial(type);
		ItemStack itemstack = null;
		if(material == null){
			if(type.equalsIgnoreCase("enchanted_golden_apple")) itemstack = new ItemStack(Material.GOLDEN_APPLE, 1, (short)1);
			else {
				Logger.log(LogType.WARNING, " Unknown item: " + string);
				return new ItemStack(Material.AIR);
			}
		} else itemstack = new ItemStack(material);
		itemstack.setAmount(amount);
		return itemstack;
	}

	public static String parseUsers(List<User> users) {
		List<String> strings = new ArrayList<String>();
		for(User u : users) {
			if(u.isOnline()) {
				strings.add("&a" + u.getName());
			}else{
				strings.add("&c" + u.getName());
			}
		}
		String parsed = strings.toString().replace("]", "").replace("[", "").replace(",", "&7,");
		return Util.setHEX(parsed);
	}
	
	public static String parseUser(User user) {
		return user.isOnline() ? Util.setHEX("&a" + user.getName()) : Util.setHEX("&c" + user.getName());
	}
}
