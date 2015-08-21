package pl.za.xvacuum.guilds.commands.admin;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import pl.za.xvacuum.guilds.Main;
import pl.za.xvacuum.guilds.api.events.TablistRefreshEvent;
import pl.za.xvacuum.guilds.commands.Executor;
import pl.za.xvacuum.guilds.managers.DataManager;
import pl.za.xvacuum.guilds.managers.InviteManager;
import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.misc.Tablist;
import pl.za.xvacuum.guilds.misc.files.Folders;
import pl.za.xvacuum.guilds.misc.files.TablistFile;
import pl.za.xvacuum.guilds.mysql.data.Data;
import pl.za.xvacuum.guilds.mysql.data.Data.DataType;
import pl.za.xvacuum.guilds.utils.EffectUtils;
import pl.za.xvacuum.guilds.utils.Util;
import pl.za.xvacuum.guilds.utils.reflections.TabUtils;

public class ReloadAdminCommand extends Executor{
	
	public ReloadAdminCommand() {
		super("admin-reload", "Reloads plugin", "/admin-reload", "admin.reload", Arrays.asList("gareload, garl, gildieprzeladuj, guildreload"), false);
	}
	
	@Override
	public void onExecute(CommandSender sender, String[] args) {
		Util.sendMessage(sender, "&aReloading qGuilds v" + Main.getInstance().getDescription().getVersion().toString() + " ...");
		
		TablistFile.setup();
		Messages.load();
		Messages.save();
		Tablist.load();
		Tablist.save();
		Bukkit.getPluginManager().callEvent(new TablistRefreshEvent(System.currentTimeMillis()));
		TabUtils.broadcastTab();
		EffectUtils.init();
		InviteManager.setup();
		if(Data.globalType().equals(DataType.FLAT)) {
			Folders.refresh();
		}
		DataManager.reload(false, true);
		Main.getInstance().reloadConfig();
		Util.sendMessage(sender, "&aSuccessfuly reloaded.");
		return;
		
	}

}
