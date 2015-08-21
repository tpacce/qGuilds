package pl.za.xvacuum.guilds.utils;

import pl.za.xvacuum.guilds.misc.Messages;

public class BooleanUtils {
	
	public static String parseBoolean(boolean b)
	{
		if(b) return Messages.BooleanValueTrue;
		else return Messages.BooleanValueFalse;
	}
	
	public static String parseBooleanMode(boolean b)
	{
		if(b) return Messages.BooleanValueEnabled;
		else return Messages.BooleanValueDisabled;
	}

}
