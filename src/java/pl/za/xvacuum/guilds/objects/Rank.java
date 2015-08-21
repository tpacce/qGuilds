package pl.za.xvacuum.guilds.objects;

import pl.za.xvacuum.guilds.managers.RankManager;

public class Rank implements Comparable<Rank>{
	
	private int points;
	private int kills;
	private int deaths;
	private User user;
	private Guild guild;
	private RankType type;
	
	public Rank(Guild guild){
		type = RankType.GUILD;
		this.guild = guild;
		RankManager.add(this);
	}
	
	public Rank(User user){
		type = RankType.USER;
		this.user = user;
		RankManager.add(this);
	}
	
	public enum RankType
	{
		USER,
		GUILD;
	}

	public int getPoints() {
		if(this.getType().equals(RankType.USER)) return this.points;
		int re = 0;
		for(User u : this.guild.getMembers()){
			re += u.getRank().getPoints();
		}
		return re/this.getGuild().getMembers().size();
	}

	public int getKills() {
		return this.kills;
	}

	public int getDeaths() {
		return this.deaths;
	}

	public User getUser() {
		return this.user;
	}

	public Guild getGuild() {
		return this.guild;
	}

	public RankType getType() {
		return type;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setGuild(Guild guild) {
		this.guild = guild;
	}

	public void setType(RankType type) {
		this.type = type;
	}
	
	public String get(){
		if(this.type.equals(RankType.USER)) return this.user.getName();
		return this.guild.getTag();
	}
	
	
	public String toString(){
		return Integer.toString(this.points);
	}

	@Override
	public int compareTo(Rank rank) {
		int i = Integer.compare(rank.getPoints(), getPoints());
		if(i == 0){
			if(type == null) return -1;
			if(rank.type == null) return 1;
			i = type.toString().compareTo(rank.get());
		} return i;
	}


}


