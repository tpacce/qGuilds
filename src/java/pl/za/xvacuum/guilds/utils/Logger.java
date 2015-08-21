package pl.za.xvacuum.guilds.utils;

import pl.za.xvacuum.guilds.Main;

public class Logger {
	
	public enum LogType
	{
		INFO,
		WARNING,
		ERROR,
	}
	private static String name = Main.getInstance().getName();
	
	public static void log(LogType l, String s)
	{
		if(l.equals(LogType.INFO)) System.out.println("["+name+"] " + s);
		else if(l.equals(LogType.WARNING)) System.out.println("["+name+" | WARNING] " + s);
		else if(l.equals(LogType.ERROR)) System.out.println("["+name+" | ERROR] " + s);
		else{
			System.out.println("["+name+"] " + s);
			return;
		}
	}
	
	public static void rawLog(String s)
	{
		System.out.println(s);
		return;
	}

}
