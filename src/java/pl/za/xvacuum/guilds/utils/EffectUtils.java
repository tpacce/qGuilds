package pl.za.xvacuum.guilds.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.potion.PotionEffectType;

import pl.za.xvacuum.guilds.objects.Guild;

public class EffectUtils {
	
	private static HashMap<Guild, Long> lastTake = new HashMap<Guild, Long>();
	private static List<PotionEffectType> allowEffects = new ArrayList<PotionEffectType>();

	public static HashMap<Guild, Long> getLastTakeEffect() {
		return lastTake;
	}

	public static void setLastTakeEffect(Guild g, long l) {
		lastTake.put(g, l);
	}
	
	public static boolean isBlocked(Guild g) {
		long timeTake = lastTake.get(g) == null ? 0 : lastTake.get(g);
		if(timeTake == 0) return false; 
		long timeCurrent = System.currentTimeMillis();
		if(timeTake > timeCurrent) {
			return true;
		}
		return false;
	}
	
	public static String timeRemain(Guild g) {
		return Util.parseTime(lastTake.get(g) == null ? 0 : lastTake.get(g));
	}
	
	public static PotionEffectType randomEffect() {
		int i = Util.randomInt(1, allowEffects.size()); 
		return allowEffects.get(i);
	}

	public static void init() {
		allowEffects.add(PotionEffectType.ABSORPTION);
		allowEffects.add(PotionEffectType.DAMAGE_RESISTANCE);
		allowEffects.add(PotionEffectType.FAST_DIGGING);
		allowEffects.add(PotionEffectType.FIRE_RESISTANCE);
		allowEffects.add(PotionEffectType.HEAL);
		allowEffects.add(PotionEffectType.HEALTH_BOOST);
		allowEffects.add(PotionEffectType.INCREASE_DAMAGE);
		allowEffects.add(PotionEffectType.JUMP);
		allowEffects.add(PotionEffectType.REGENERATION);
		allowEffects.add(PotionEffectType.SPEED);
		
	}
}