package pl.za.xvacuum.guilds.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import pl.za.xvacuum.guilds.api.enums.DataSaveType;

public class DataSaveEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	private DataSaveType type;
	
	public DataSaveEvent(DataSaveType type) {
		this.type = type;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public DataSaveType getType() {
		return type;
	}

}
