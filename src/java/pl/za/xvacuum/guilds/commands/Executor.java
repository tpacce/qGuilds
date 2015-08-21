package pl.za.xvacuum.guilds.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.utils.Logger;
import pl.za.xvacuum.guilds.utils.Logger.LogType;
import pl.za.xvacuum.guilds.utils.Util;

public abstract class Executor extends Command{
	
	private String permission;
	private String name;
	private boolean onlyPlayer = false;
	
	public Executor(String name, String description, String usage, String permission, List<String> aliases, boolean onlyPlayer)
	{
		super(name, description, usage, aliases);
		this.permission = "qguilds." + permission;
		this.onlyPlayer = onlyPlayer;
		this.name = name;
	}
	
	public final boolean execute(CommandSender sender, String label, String[] args)
	{
		if(!sender.hasPermission(this.permission))
		{
			sender.sendMessage(Util.setHEX(Messages.CommandNoPerm.replace("%permission%", this.permission.toLowerCase().toString()))); 
			return true;
		}
		if(this.onlyPlayer)
		{
			if(!(sender instanceof Player))
			{
				sender.sendMessage(Util.setHEX(Messages.CommandNoPlayer)); 
				return true;
			}
		}
		try {
			onExecute(sender, args);
		} catch(Exception o) {
			Logger.log(LogType.ERROR, "An error occured with executing command '" + this.name.toLowerCase().toString() + "'!");
			o.printStackTrace();
		}
		return true;
	}

	public abstract void onExecute(CommandSender sender,  String[] args);

}