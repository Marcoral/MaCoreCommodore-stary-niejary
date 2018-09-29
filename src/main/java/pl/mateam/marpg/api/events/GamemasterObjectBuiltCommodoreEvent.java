package pl.mateam.marpg.api.events;

import java.sql.Connection;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import pl.mateam.marpg.api.objects.users.CommodoreGamemaster;

public class GamemasterObjectBuiltCommodoreEvent extends Event {
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
	private boolean brandNew;
	public GamemasterObjectBuiltCommodoreEvent(CommodoreGamemaster gamemasterObject, Connection connection, boolean brandNew) {
		this.gamemasterObject = gamemasterObject;
		this.connection = connection;
		this.brandNew = brandNew;

	}
	
	public CommodoreGamemaster getGamemasterObject() {
		return gamemasterObject;
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public boolean isBrandNew() {
		return brandNew;
	}
}
