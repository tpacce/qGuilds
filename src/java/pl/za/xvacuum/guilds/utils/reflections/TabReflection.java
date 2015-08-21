package pl.za.xvacuum.guilds.utils.reflections;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.misc.Tablist;
import pl.za.xvacuum.guilds.objects.User;
import pl.za.xvacuum.guilds.utils.ReplaceUtils;
import pl.za.xvacuum.guilds.utils.Util;
import tk.northpl.tab.TabAPI;

public class TabReflection {

	public static void send(Player player) {
		User u = User.get(player);
		PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
		IChatBaseComponent header = ChatSerializer.a("{'text': '" + Util.setHEX(Tablist.tablistHeader.replace("%guild%", u.hasGuild() ? u.getGuild().getName() : Messages.UnknownValue)).replace("%tag%", u.hasGuild() ? u.getGuild().getTag() : Messages.UnknownValue).replace("%points%", String.valueOf(u.getRank().getPoints())).replace("%kills%", String.valueOf(u.getRank().getKills())).replace("%deaths%", String.valueOf(u.getRank().getDeaths())).replace("%name%", u.getName()) + "'}");
		IChatBaseComponent footer = ChatSerializer.a("{'text': '" + Util.setHEX(Tablist.tablistFooter.replace("%guild%", u.hasGuild() ? u.getGuild().getName() : Messages.UnknownValue)).replace("%tag%", u.hasGuild() ? u.getGuild().getTag() : Messages.UnknownValue).replace("%points%", String.valueOf(u.getRank().getPoints())).replace("%kills%", String.valueOf(u.getRank().getKills())).replace("%deaths%", String.valueOf(u.getRank().getDeaths())).replace("%name%", u.getName()) + "'}");
		PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
		try {
	        Field headerField = packet.getClass().getDeclaredField("a");
	        headerField.setAccessible(true);
	        headerField.set(packet, header);
	        headerField.setAccessible(!headerField.isAccessible());
	      
	        Field footerField = packet.getClass().getDeclaredField("b");
	        footerField.setAccessible(true);
	        footerField.set(packet, footer);
	        footerField.setAccessible(!footerField.isAccessible());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		connection.sendPacket(packet);
		
		List<String> strings = new ArrayList<String>();
		for(Field f : Tablist.class.getFields()) {
			String name = f.getName();
			if(name.startsWith("tabSlot_")) {
				try {
					strings.add(f.get(f.getName()).toString());
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		int index = 0;
		while(index != 19) {
			try {
				TabAPI.setTabSlot(0, index, Util.setHEX(ReplaceUtils.replace(player, strings.get(index)).toString()));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			index++;
		}
		index = 19;
		while(index != 39) {
			try {
				TabAPI.setTabSlot(0, index, Util.setHEX(ReplaceUtils.replace(player, strings.get(index)).toString()));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			index++;
		}
		index = 39;
		while(index != 59) {
			try {
				TabAPI.setTabSlot(0, index, Util.setHEX(ReplaceUtils.replace(player, strings.get(index)).toString()));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			index++;
		}
		index = 59;
		while(index != 80) {
			try {
				TabAPI.setTabSlot(0, index, Util.setHEX(ReplaceUtils.replace(player, strings.get(index)).toString()));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			index++;
		}
	}

}
