package pl.za.xvacuum.guilds.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import pl.za.xvacuum.guilds.api.enums.DataLoadType;

public class DataLoadEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	private DataLoadType type;
	private int quantity;
	
	public DataLoadEvent(DataLoadType type, int quantity) {
		this.type = type;
		this.quantity = quantity;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public int getQuantity() {
		return quantity;
	}

	public DataLoadType getType() {
		return type;
	}

}
