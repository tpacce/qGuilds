package pl.za.xvacuum.guilds.objects;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.bukkit.entity.Player;

import pl.za.xvacuum.guilds.Main;
import pl.za.xvacuum.guilds.misc.Messages;
import pl.za.xvacuum.guilds.utils.Logger;
import pl.za.xvacuum.guilds.utils.Logger.LogType;
import pl.za.xvacuum.guilds.utils.Util;

public class Update {
	
	private static URL files;
	private static String version;
	
	public Update(String url)
	{
		try{
			files = new URL(url);
		}catch(MalformedURLException e) {
			Logger.log(LogType.WARNING, " [Updater] An error occured with connecting to update page!");
			e.printStackTrace();
		}
	}
	
	public static String toReadableString(InputStream in, String encoding) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[8192];
		int len = 0;
		while ((len = in.read(buf)) != -1)
			baos.write(buf, 0, len);
		in.close();
		return new String(baos.toByteArray(), encoding);
	}

	public boolean update()
	{
		try{
			URLConnection con = files.openConnection();
			InputStream in = con.getInputStream();
			String encoding = con.getContentEncoding();
			encoding = encoding == null ? "UTF-8" : encoding;
			String body = toReadableString(in, encoding);
			in.close();
			version = body;
			if(!version.equals(Main.VERSION)) {
				if(Messages.DefaultLanguage.contains("pl")) {
					Logger.log(LogType.INFO, " [Updater] Wykryto nowa wersje pluginu qGuilds.");
					Logger.log(LogType.INFO, " [Updater]  Wersja: " + version);
					Logger.log(LogType.INFO, " [Updater]  Obecna: " + Main.getInstance().getDescription().getVersion());
					return true;
				}
				Logger.log(LogType.INFO, " [Updater] Detected new version of qGuilds!");
				Logger.log(LogType.INFO, " [Updater]  Version: " + version);
				Logger.log(LogType.INFO, " [Updater]  Current: " + Main.getInstance().getDescription().getVersion());
				return true;
			}
		}catch(Exception e) {
			Logger.log(LogType.WARNING, " [Updater] An error occured with checking new update!");
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean update(Player p)
	{
		try{
			URLConnection con = files.openConnection();
			InputStream in = con.getInputStream();
			String encoding = con.getContentEncoding();
			encoding = encoding == null ? "UTF-8" : encoding;
			String body = toReadableString(in, encoding);
			in.close();
			version = body;
			if(!version.equals(Main.VERSION)) {
				if(Messages.DefaultLanguage.contains("pl")) {
					Util.sendMessage(p, "&aWykryto nowa wersje pluginu qGuilds.");
					Util.sendMessage(p, "&a  Wersja: " + version);
					Util.sendMessage(p, "&a  Obecna: " + Main.getInstance().getDescription().getVersion());
					return true;
				}
				Util.sendMessage(p, "&aDetected a new version of qGuilds!");
				Util.sendMessage(p, "&a  Version: " + version);
				Util.sendMessage(p, "&a  Current: " + Main.getInstance().getDescription().getVersion());
				return true;
			}
		}catch(Exception e) {
			Logger.log(LogType.WARNING, " [Updater] An error occured with checking new update!");
			e.printStackTrace();
		}
		return false;
	}
	

}
