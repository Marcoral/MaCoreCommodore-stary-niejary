package pl.mateam.marpg.api.events;

import java.sql.Connection;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import pl.mateam.marpg.api.objects.users.CommodorePlayer;

public class PlayerObjectBeingSavedCommodoreEvent extends Event {
	private static final HandlerList handlers = new HandlerList();

	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	private CommodorePlayer playerObject;
	private Connection connection;
	public PlayerObjectBeingSavedCommodoreEvent(CommodorePlayer playerObject, Connection connection) {
		this.playerObject = playerObject;
		this.connection = connection;
	}
	
	public CommodorePlayer getPlayerObject() {
		return playerObject;
	}
	
	public Connection getConnection() {
		return connection;
	}
}
