package pl.za.xvacuum.guilds.api;

public class APILogger {
	
	public static boolean log(String text) {
		if(text != null) {
			System.out.println("[qGuilds API Output] " + text);
			return true;
		}
		return false;
	}

}
