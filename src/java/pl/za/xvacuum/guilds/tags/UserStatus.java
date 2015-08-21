package pl.za.xvacuum.guilds.tags;

import pl.za.xvacuum.guilds.objects.User;

public class UserStatus {
	
	public enum UserStatusType {
		LEADER,
		MASTER,
		MEMBER,
		UNKNOWN;
	}
	
	public static UserStatusType getUserStatus(User user) {
		if(user.isLeader(user.getGuild())) return UserStatusType.LEADER;
		else if(user.isMaster(user.getGuild())) return UserStatusType.MASTER;
		else if(user.isMember(user.getGuild())) return UserStatusType.MEMBER;
		return UserStatusType.UNKNOWN;
	}

}
