package pl.mateam.marpg.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import pl.mateam.marpg.api.objects.users.CommodoreGamemaster;

public class GamemasterVisibilityChangeCommodoreEvent extends Event {
	private static final HandlerList handlers = new HandlerList();

	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	private CommodoreGamemaster gamemasterObject;
	public GamemasterVisibilityChangeCommodoreEvent(CommodoreGamemaster gamemasterObject) {
		this.gamemasterObject = gamemasterObject;
	}
	
	public CommodoreGamemaster getGamemasterObject() {
		return gamemasterObject;
	}
}
