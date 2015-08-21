package pl.za.xvacuum.guilds.misc.files;

import java.io.File;

import pl.za.xvacuum.guilds.Main;
import pl.za.xvacuum.guilds.objects.Guild;
import pl.za.xvacuum.guilds.objects.Region;
import pl.za.xvacuum.guilds.objects.User;

public class Folders {
	
	private static final File dataFolder = Main.getInstance().getDataFolder();
	private static final File usersFolder = new File(dataFolder, "data" + File.separator + "users");
	private static final File guildsFolder = new File(dataFolder, "data" + File.separator + "guilds");
	private static final File regionsFolder = new File(dataFolder, "data" + File.separator + "regions");
	
	public static void refresh()
	{
		if(!dataFolder.exists()) dataFolder.mkdirs();
		if(!usersFolder.exists()) usersFolder.mkdirs();
		if(!guildsFolder.exists()) guildsFolder.mkdirs();
		if(!regionsFolder.exists()) regionsFolder.mkdirs();
		return;
	}
	
	public static File getDataFolder() 
	{ 
		return dataFolder; 
	}
	
	public static File getUsersFolder() 
	{
		return usersFolder;
	}
	
	public static File getGuildsFolder()
	{
		return guildsFolder;
	}
	
	public static File getRegionsFolder()
	{
		return regionsFolder;
	}
	
	public static boolean exists(User u)
	{
		refresh();
		File f = new File(usersFolder, u.getName() + ".yml");
		return f.exists();
	}
	
	public static boolean exists(Guild g)
	{
		refresh();
		File f = new File(guildsFolder, g.getTag() + ".yml");
		return f.exists();
	}
	
	public static boolean exists(Region r)
	{
		refresh();
		File f = new File(regionsFolder, r.getName() + ".yml");
		return f.exists();
	}
	
	public static File getFile(User u)
	{
		return new File(usersFolder, u.getName() + ".yml");
	}
	
	public static File getFile(Guild g)
	{
		return new File(guildsFolder, g.getTag() + ".yml");
	}
	
	public static File getFile(Region r)
	{
		return new File(regionsFolder, r.getName() + ".yml");
	}
	
}
