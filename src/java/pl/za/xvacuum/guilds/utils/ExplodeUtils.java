package pl.za.xvacuum.guilds.utils;

import java.util.HashMap;
import pl.za.xvacuum.guilds.objects.Guild;

public class ExplodeUtils {
	
	private static HashMap<Guild, Long> lastExplode = new HashMap<Guild, Long>();

	public static HashMap<Guild, Long> getLastExplode() {
		return lastExplode;
	}

	public static void setLastExplode(Guild g, long l) {
		lastExplode.put(g, l);
	}
	
	public static boolean isBlocked(Guild g) {
		long timeExplode = lastExplode.get(g) == null ? 0 : lastExplode.get(g);
		if(timeExplode == 0) return false; 
		long timeCurrent = System.currentTimeMillis();
		if(timeExplode > timeCurrent) {
			return true;
		}
		return false;
	}
	
	public static String timeRemain(Guild g) {
		return Long.toString((lastExplode.get(g) == null ? 0 : lastExplode.get(g) - System.currentTimeMillis()) / 1000) + "s";
	}
	


}
