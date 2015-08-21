package pl.za.xvacuum.guilds.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListSerializer {
	
	public static String serialize(List<String> list) {
		String result = null;
		result = toString(list);
		return result;
	}
	
	public static List<String> deserialize(String serializedList) {
		List<String> result = new ArrayList<>();
	    if ((serializedList == null) || (serializedList.isEmpty())) return result;
	    result = Arrays.asList(serializedList.split(","));
		return result;
	}
	
	private static String toString(List<String> list) {
		StringBuilder sb = new StringBuilder();
		for (String s : list) {
			sb.append(s);
			sb.append(',');
		}
		String s = sb.toString();
		if(s.length() > 1) s = s.substring(0, s.length() - 1);
		return s;
	}

}
