package pl.mateam.marpg.api.events;

import java.sql.Connection;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import pl.mateam.marpg.api.objects.users.CommodoreUser;

public class UserObjectBuiltCommodoreEvent extends Event {
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
	private boolean brandNew;
	public UserObjectBuiltCommodoreEvent(CommodoreUser userObject, Connection connection, boolean brandNew) {
		this.userObject = userObject;
		this.connection = connection;
		this.brandNew = brandNew;
	}
	
	public CommodoreUser getUserObject() {
		return userObject;
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public boolean isBrandNew() {
		return brandNew;
	}
}
