package pl.mateam.marpg.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class PlayerAsyncPreLoggedBannedCommodoreEvent extends Event {
	private static final HandlerList handlers = new HandlerList();

	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	private final AsyncPlayerPreLoginEvent parentEvent;
	public PlayerAsyncPreLoggedBannedCommodoreEvent(AsyncPlayerPreLoginEvent precedingEvent) {
		this.parentEvent = precedingEvent;
	}
	
	public AsyncPlayerPreLoginEvent getParentEvent() {
		return parentEvent;
	}
}
