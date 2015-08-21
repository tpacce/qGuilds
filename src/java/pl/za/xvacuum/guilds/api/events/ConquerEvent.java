package pl.za.xvacuum.guilds.api.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import pl.za.xvacuum.guilds.objects.Guild;
import pl.za.xvacuum.guilds.objects.Treasure;
import pl.za.xvacuum.guilds.objects.User;

public class ConquerEvent extends Event implements Cancellable{

	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;
	private User user;
	private Guild guild;
	private Treasure treasure;
	private int points;
	
	public ConquerEvent(User user, Guild guild, Treasure treasure, int points){
		this.user = user;
		this.guild = guild;
		this.treasure = treasure;
		this.points = points;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public User getUser() {
		return user;
	}

	public Guild getGuild() {
		return guild;
	}

	public Treasure getTreasure() {
		return treasure;
	}

	public int getPoints() {
		return points;
	}

	public void setTreasure(Treasure treasure) {
		this.treasure = treasure;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
		 
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
