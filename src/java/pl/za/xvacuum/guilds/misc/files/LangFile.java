package pl.za.xvacuum.guilds.misc.files;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import pl.za.xvacuum.guilds.Main;
import pl.za.xvacuum.guilds.utils.Logger;
import pl.za.xvacuum.guilds.utils.Logger.LogType;

public class LangFile {
	
	private static File dataFolder = Main.getInstance().getDataFolder();
	private static File langFile = new File(dataFolder, "messages.yml");
	
	public static File getFile()
	{
		return langFile;
	}
	
	
	public static void setup()
	{
		if(!dataFolder.exists())
		{
			dataFolder.mkdirs();
		}
		else if(!langFile.exists())
		{
			try{
				langFile.createNewFile();
				Logger.log(LogType.INFO, " [Lang] Lang file has been created!");
				return;
			}catch(IOException e) {
				Logger.log(LogType.WARNING, " [Lang] An error occured with loading lang file!");
				Logger.log(LogType.WARNING, " [Lang] Messages has been changed to default (english)");
				e.printStackTrace();
			}
		}
		
	}
	
	public static FileConfiguration get()
	{
		return YamlConfiguration.loadConfiguration(langFile);
	}

}
