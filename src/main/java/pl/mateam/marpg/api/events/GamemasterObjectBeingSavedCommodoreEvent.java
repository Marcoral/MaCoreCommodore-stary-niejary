package pl.mateam.marpg.api.events;

import java.sql.Connection;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import pl.mateam.marpg.api.objects.users.CommodoreGamemaster;

public class GamemasterObjectBeingSavedCommodoreEvent extends Event {
	private static final HandlerList handlers = new HandlerList();

	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	private CommodoreGamemaster gamemasterObject;
	private Connection connection;
	public GamemasterObjectBeingSavedCommodoreEvent(CommodoreGamemaster gamemasterObject, Connection connection) {
		this.gamemasterObject = gamemasterObject;
		this.connection = connection;
	}
	
	public CommodoreGamemaster getGamemasterObject() {
		return gamemasterObject;
	}
	
	public Connection getConnection() {
		return connection;
	}
}
