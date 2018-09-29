package pl.mateam.marpg.engine.core.submodules.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.api.objects.users.CommodoreGamemaster;
import pl.mateam.marpg.api.objects.users.CommodorePlayer;
import pl.mateam.marpg.api.objects.users.CommodoreUser;
import pl.mateam.marpg.api.objects.users.children.bans.CommodoreBanObject;
import pl.mateam.marpg.api.objects.users.children.bans.CommodoreBanObject.BanScope;
import pl.mateam.marpg.api.objects.users.children.bans.CommodoreBansBuilder;
import pl.mateam.marpg.api.submodules.text.TextNode;
import pl.mateam.marpg.api.submodules.users.CommodoreUsersManager;
import pl.mateam.marpg.engine.ControlPanel;
import pl.mateam.marpg.engine.core.internal.CoreUtils;
import pl.mateam.marpg.engine.core.internal.sql.CoreDBInsertion;
import pl.mateam.marpg.engine.core.internal.sql.CoreDBRetrieval;
import pl.mateam.marpg.engine.core.objects.users.GamemasterInstance;
import pl.mateam.marpg.engine.core.objects.users.PlayerInstance;
import pl.mateam.marpg.engine.core.objects.users.children.bans.BanObjectInstance;
import pl.mateam.marpg.engine.core.objects.users.children.bans.BansBuilderInstance;

public class UsersManagerInstance implements CommodoreUsersManager {
	
	/* ------------------------------------------------------------
	 * Bridge between AsyncPlayerPreLoginEvent and PlayerLoginEvent
	 -------------------------------------------------------------- */
	
	private Map<String, PlayerInstance> connectingPlayers = new ConcurrentHashMap<>();
	private Map<String, GamemasterInstance> connectingGamemasters = new ConcurrentHashMap<>();
	
	public void gamemasterIsConnecting(String nickname) {
		try(Connection connection = Commodore.getDatabase().getConnection()) {
			connectingGamemasters.put(nickname, new GamemasterInstance(connection, nickname));
		} catch (SQLException e) {
			ControlPanel.exceptionThrown(e);
		}
	}
	
	public void playerIsConnecting(String nickname) {
		try(Connection connection = Commodore.getDatabase().getConnection()) {
			connectingPlayers.put(nickname, new PlayerInstance(connection, nickname));
		} catch (SQLException e) {
			ControlPanel.exceptionThrown(e);
		}
	}
	
	public void handle(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		String nickname = player.getName();
		
		GamemasterInstance gamemasterObject = connectingGamemasters.remove(nickname);
		if(gamemasterObject != null) {
			gamemasterObject.setPlayer(player);
			onlineGamemasters.put(nickname.toLowerCase(), gamemasterObject);
			return;
		}
		PlayerInstance playerObject = connectingPlayers.remove(nickname);
		if(playerObject != null) {
			playerObject.setPlayer(player);
			onlinePlayers.put(nickname.toLowerCase(), playerObject);
			return;
		}
		
		event.disallow(Result.KICK_OTHER, Commodore.getTextManager().getNode(TextNode.LOGIN_DATABASE_NOT_SETUP));
	}
	
	/* ------------------- */
	/* Implementation part */
	/* ------------------- */
	
	// +++++ OBJECT ACCESSORS +++++ \\
	
	private Map<String, CommodoreGamemaster> onlineGamemasters = new HashMap<>();
	private Map<String, CommodorePlayer> onlinePlayers = new HashMap<>();
	
	@Override
	public CommodoreGamemaster getOnlineGamemaster(String nickname) {
		return onlineGamemasters.get(nickname.toLowerCase());
	}
	
	@Override
	public CommodorePlayer getOnlinePlayer(String nickname) {
		return onlinePlayers.get(nickname.toLowerCase());
	}

	@Override
	public CommodoreUser getOnlineUser(String nickname) {
		CommodoreUser result = getOnlineGamemaster(nickname);
		if(result != null)
			return result;
		return getOnlinePlayer(nickname);
	}

	@Override
	public void actionOnAllOnlineGamemasters(Consumer<CommodoreGamemaster> action) {
		onlineGamemasters.values().forEach(action);
	}

	@Override
	public void actionOnAllOnlinePlayers(Consumer<CommodorePlayer> action) {
		onlinePlayers.values().forEach(action);		
	}

	@Override
	public void actionOnAllOnlineUsers(Consumer<CommodoreUser> action) {
		onlineGamemasters.values().forEach(action);
		onlinePlayers.values().forEach(action);
	}
	
	// +++++ BANS +++++ \\
	
	@Override
	public CommodoreBanObject getBanInfo(String nickname, BanScope scope) {
		CommodorePlayer onlinePlayer = getOnlinePlayer(nickname);
		if(onlinePlayer != null)
			return onlinePlayer.getBanInfo(scope);
		try(Connection connection = Commodore.getDatabase().getConnection();
			PreparedStatement statement = CoreUtils.prepareStatementInline(connection, CoreDBRetrieval.PLAYER_BANINFO, nickname, scope.toSQL());
			ResultSet resultSet = statement.executeQuery()) {
			
			if(resultSet.next())
				return BanObjectInstance.fromDatabase(resultSet);
			else
				return null;
		} catch (SQLException e) {
			ControlPanel.exceptionThrown(e);
			return null;
		}
	}
	
	@Override
	public void setBanned(String nickname, BanScope scope, int reasonID) {
		setBannedAndCheck(nickname, scope, reasonID, null);
	}
	
	@Override
	public void setBanned(String nickname, BanScope scope, String customReason) {
		setBannedAndCheck(nickname, scope, customReason, null);
	}

	@Override
	public void setBanned(String nickname, BanScope scope, int reasonID, Consumer<CommodoreBansBuilder> additionalInfo) {
		setBannedAndCheck(nickname, scope, reasonID, additionalInfo, null);
	}
	
	@Override
	public void setBanned(String nickname, BanScope scope, String customReason, Consumer<CommodoreBansBuilder> additionalInfo) {
		setBannedAndCheck(nickname, scope, customReason, additionalInfo, null);
	}
	
	@Override
	public void setBannedAndCheck(String nickname, BanScope scope, int reasonID, Consumer<Boolean> actionOnResult) {
		CommodorePlayer onlinePlayer = getOnlinePlayer(nickname);
		if(onlinePlayer != null) {
			onlinePlayer.setBanned(scope, reasonID);
			if(actionOnResult != null)
				actionOnResult.accept(true);
		} else
			banOfflinePlayer(actionOnResult, nickname, scope.toSQL(), null, reasonID, null, new Date(), null, nickname);
	}
		
	@Override
	public void setBannedAndCheck(String nickname, BanScope scope, String customReason, Consumer<Boolean> actionOnResult) {
		CommodorePlayer onlinePlayer = getOnlinePlayer(nickname);
		if(onlinePlayer != null) {
			onlinePlayer.setBanned(scope, customReason);
			if(actionOnResult != null)
				actionOnResult.accept(true);
		} else
			banOfflinePlayer(actionOnResult, nickname, scope.toSQL(), null, null, customReason, new Date(), null, nickname);
	}
	
	@Override
	public void setBannedAndCheck(String nickname, BanScope scope, int reasonID, Consumer<CommodoreBansBuilder> additionalInfo, Consumer<Boolean> actionOnResult) {
		CommodorePlayer onlinePlayer = getOnlinePlayer(nickname);
		if(onlinePlayer != null) {
			onlinePlayer.setBanned(scope, reasonID, additionalInfo);
			if(actionOnResult != null)
				actionOnResult.accept(true);
		} else {
			BanObjectInstance banObject = new BanObjectInstance(scope, reasonID);
			BansBuilderInstance banBuilder = new BansBuilderInstance(banObject);
			additionalInfo.accept(banBuilder);
			Commodore.getDatabase().performActionOnData(resultSet -> {
				try {
					if(resultSet.next()) {
						BanObjectInstance previousBan = BanObjectInstance.fromDatabase(resultSet);
						Date newExpirationDate = banBuilder.getBlendMode().applyTo(banObject.getExpirationDate(), previousBan.getExpirationDate());
						banObject.setExpirationDate(newExpirationDate);
					}
					banOfflinePlayer(actionOnResult, nickname, scope.toSQL(), banObject.getWhoBanned(), reasonID, null, new Date(), banObject.getExpirationDate(), nickname);
				} catch (SQLException e) {
					ControlPanel.exceptionThrown(e);
				}
			}, CoreDBRetrieval.PLAYER_BANINFO, nickname, scope.toSQL());
		}
	}
	
	@Override
	public void setBannedAndCheck(String nickname, BanScope scope, String customReason, Consumer<CommodoreBansBuilder> additionalInfo, Consumer<Boolean> actionOnResult) {
		CommodorePlayer onlinePlayer = getOnlinePlayer(nickname);
		if(onlinePlayer != null) {
			onlinePlayer.setBanned(scope, customReason, additionalInfo);
			if(actionOnResult != null)
				actionOnResult.accept(true);
		} else {
			BanObjectInstance banObject = new BanObjectInstance(customReason);
			BansBuilderInstance banBuilder = new BansBuilderInstance(banObject);
			additionalInfo.accept(banBuilder);
			Commodore.getDatabase().performActionOnData(resultSet -> {
				try {
					if(resultSet.next()) {
						BanObjectInstance previousBan = BanObjectInstance.fromDatabase(resultSet);
						Date newExpirationDate = banBuilder.getBlendMode().applyTo(banObject.getExpirationDate(), previousBan.getExpirationDate());
						banObject.setExpirationDate(newExpirationDate);
					}
					banOfflinePlayer(actionOnResult, nickname, scope.toSQL(), banObject.getWhoBanned(), null, customReason, new Date(), banObject.getExpirationDate(), nickname);
				} catch (SQLException e) {
					ControlPanel.exceptionThrown(e);
				}
			}, CoreDBRetrieval.PLAYER_BANINFO, nickname, scope.toSQL());
		}
	}
	
	private void banOfflinePlayer(Consumer<Boolean> actionOnResult, Object... args) {
		if(actionOnResult == null)
			Commodore.getDatabase().executeStatement(CoreDBInsertion.PLAYERS_BAN, args);
		else
			Commodore.getDatabase().performActionOnUpdateResult(result -> actionOnResult.accept(result > 0), CoreDBInsertion.PLAYERS_BAN, args);
	}
}
