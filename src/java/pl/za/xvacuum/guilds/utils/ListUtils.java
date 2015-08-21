package pl.za.xvacuum.guilds.utils;

import java.lang.reflect.Field;
import java.util.List;

public class ListUtils {
	
    public static boolean isList(Field f) {
    	return List.class.isAssignableFrom(f.getType());
    }

}
