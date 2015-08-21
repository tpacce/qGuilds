package pl.za.xvacuum.guilds.misc.files;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import pl.za.xvacuum.guilds.Main;
import pl.za.xvacuum.guilds.utils.Logger;
import pl.za.xvacuum.guilds.utils.Logger.LogType;

public class TablistFile {
	
	private static File dataFolder = Main.getInstance().getDataFolder();
	private static File tablistFile = new File(dataFolder, "tablist.yml");
	
	public static File getFile()
	{
		return tablistFile;
	}
	
	
	public static void setup()
	{
		if(!dataFolder.exists())
		{
			dataFolder.mkdirs();
		}
		else if(!tablistFile.exists())
		{
			try{
				tablistFile.createNewFile();
				
				Logger.log(LogType.INFO, " [Tablist] Tablist file has been created!");
				return;
			}catch(IOException e) {
				Logger.log(LogType.WARNING, " [Tablist] An error occured with loading tablist file!");
				e.printStackTrace();
			}
		}
		
	}
	
	public static FileConfiguration get()
	{
		return YamlConfiguration.loadConfiguration(tablistFile);
	}


}
