package pl.mateam.marpg.api.events;

import java.sql.Connection;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import pl.mateam.marpg.api.objects.users.CommodorePlayer;
import pl.mateam.marpg.api.objects.users.CommodoreUser;

public class PlayerObjectBuiltCommodoreEvent extends Event {
	private static final HandlerList handlers = new HandlerList();

	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	private CommodoreUser playerObject;
	private Connection connection;
	private boolean brandNew;
	public PlayerObjectBuiltCommodoreEvent(CommodorePlayer playerObject, Connection connection, boolean brandNew) {
		this.playerObject = playerObject;
		this.connection = connection;
		this.brandNew = brandNew;
	}
	
	public CommodoreUser getPlayerObject() {
		return playerObject;
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public boolean isBrandNew() {
		return brandNew;
	}
}
