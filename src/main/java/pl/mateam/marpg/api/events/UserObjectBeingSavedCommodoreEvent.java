package pl.mateam.marpg.api.events;

import java.sql.Connection;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import pl.mateam.marpg.api.objects.users.CommodoreUser;

public class UserObjectBeingSavedCommodoreEvent extends Event {
	private static final HandlerList handlers = new HandlerList();

	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	private CommodoreUser userObject;
	private Connection connection;
	public UserObjectBeingSavedCommodoreEvent(CommodoreUser userObject, Connection connection) {
		this.userObject = userObject;
		this.connection = connection;
	}
	
	public CommodoreUser getUserObject() {
		return userObject;
	}
	
	public Connection getConnection() {
		return connection;
	}
}
