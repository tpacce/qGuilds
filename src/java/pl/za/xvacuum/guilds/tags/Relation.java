package pl.za.xvacuum.guilds.tags;

import pl.za.xvacuum.guilds.objects.Guild;
import pl.za.xvacuum.guilds.objects.User;

public class Relation {
	
	public enum RelationType {
		NEUTRAL,
		ALLY,
		MEMBER,
		NOGUILD;
	}
	
	public static RelationType getRelation(Guild guild, User user) {
		if(user.getGuild() == null) return RelationType.NEUTRAL;
		else if(user.getGuild().equals(guild)) return RelationType.MEMBER;
		else if(user.getGuild().getAllies().contains(guild)) return RelationType.ALLY;
		return RelationType.NOGUILD;
	}

}
