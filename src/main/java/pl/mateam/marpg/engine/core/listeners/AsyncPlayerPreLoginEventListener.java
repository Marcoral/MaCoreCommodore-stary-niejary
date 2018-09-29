package pl.mateam.marpg.engine.core.listeners;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.api.events.GamemasterAsyncPreLoggedCommodoreEvent;
import pl.mateam.marpg.api.events.GamemasterImpersonateCommodoreEvent;
import pl.mateam.marpg.api.events.PlayerAsyncPreLoggedBannedCommodoreEvent;
import pl.mateam.marpg.api.events.PlayerAsyncPreLoggedCommodoreEvent;
import pl.mateam.marpg.api.objects.users.children.bans.CommodoreBanObject;
import pl.mateam.marpg.api.submodules.text.TextNode;
import pl.mateam.marpg.engine.ControlPanel;
import pl.mateam.marpg.engine.core.internal.sql.CoreDBField;
import pl.mateam.marpg.engine.core.internal.sql.CoreDBRetrieval;
import pl.mateam.marpg.engine.core.internal.sql.CoreDBValue;
import pl.mateam.marpg.engine.core.objects.users.children.bans.BanObjectInstance;
import pl.mateam.marpg.engine.core.submodules.users.UsersManagerInstance;

public class AsyncPlayerPreLoginEventListener implements Listener {
	//Data should be loaded only if player is allowed to join the server - therefore I used MONITOR.
	@EventHandler(priority = EventPriority.MONITOR)
	public void on(AsyncPlayerPreLoginEvent event) {
		if(!event.getLoginResult().equals(Result.ALLOWED))
			return;
			
		Consumer<ResultSet> actionOnGamemasterIP = futureResult -> performActionOnGamemasterIP(futureResult, event);
		Commodore.getDatabase().performActionOnData(actionOnGamemasterIP, CoreDBRetrieval.GAMEMASTER_GENERAL, event.getName());
	}
	
	private void performActionOnGamemasterIP(ResultSet fetchedData, AsyncPlayerPreLoginEvent event) {
		try {
			if(fetchedData.next()) {
				if(!fetchedData.getString(CoreDBField.GAMEMASTERS_IP).equals(event.getAddress().toString())) {
					event.disallow(Result.KICK_OTHER, Commodore.getTextManager().getNode(TextNode.LOGIN_GAMEMASTER_IMPERSONATE));
					Bukkit.getPluginManager().callEvent(new GamemasterImpersonateCommodoreEvent(event));
					return;
				}
				((UsersManagerInstance) Commodore.getUsersManager()).gamemasterIsConnecting(event.getName());
				Bukkit.getPluginManager().callEvent(new GamemasterAsyncPreLoggedCommodoreEvent(event));
			} else {
				Consumer<ResultSet> actionOnPlayerBanInfo = futureResult -> performActionOnPlayerBanInfo(futureResult, event);
				Commodore.getDatabase().performActionOnData(actionOnPlayerBanInfo, CoreDBRetrieval.PLAYER_BANINFO, event.getName(), CoreDBValue.VALUE_BANS_GLOBAL);
			}
		} catch(SQLException e) {
			ControlPanel.exceptionThrown(e);
		}
	}
	
	private void performActionOnPlayerBanInfo(ResultSet fetchedData, AsyncPlayerPreLoginEvent event) {
		try {
			if(fetchedData.next()) {
				CommodoreBanObject banInfo = BanObjectInstance.fromDatabase(fetchedData);
				if(banInfo != null) {
					Bukkit.getPluginManager().callEvent(new PlayerAsyncPreLoggedBannedCommodoreEvent(event));
					event.disallow(Result.KICK_BANNED, banInfo.getFormattedReason());
					return;
				}
			}
			((UsersManagerInstance) Commodore.getUsersManager()).playerIsConnecting(event.getName());
			Bukkit.getPluginManager().callEvent(new PlayerAsyncPreLoggedCommodoreEvent(event));
		} catch(SQLException e) {
			ControlPanel.exceptionThrown(e);
		}
	}
}
