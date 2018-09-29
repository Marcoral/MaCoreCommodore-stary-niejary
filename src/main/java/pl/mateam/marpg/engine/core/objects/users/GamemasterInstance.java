package pl.mateam.marpg.engine.core.objects.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.api.events.GamemasterObjectBeingSavedCommodoreEvent;
import pl.mateam.marpg.api.events.GamemasterObjectBuiltCommodoreEvent;
import pl.mateam.marpg.api.events.GamemasterVisibilityChangeCommodoreEvent;
import pl.mateam.marpg.api.objects.users.CommodoreGamemaster;
import pl.mateam.marpg.api.objects.users.CommodorePlayer;
import pl.mateam.marpg.api.submodules.users.CommodoreUsersManager;
import pl.mateam.marpg.engine.ControlPanel;
import pl.mateam.marpg.engine.Initializer;
import pl.mateam.marpg.engine.core.internal.CoreUtils;
import pl.mateam.marpg.engine.core.internal.sql.CoreDBRetrieval;

public class GamemasterInstance extends UserInstance implements CommodoreGamemaster {
	
	/* --------------- */
	/* Load/save stuff */
	/* --------------- */
	
	public GamemasterInstance(Connection connection, String nickname) {
		super(connection, nickname);
		if(!SETUP_SUCCESSFULLY)
			return;
		try(PreparedStatement statement = CoreUtils.prepareStatementInline(connection, CoreDBRetrieval.GAMEMASTER_GENERAL, nickname);
			ResultSet general = statement.executeQuery()) {
			
			if(!general.next()) {
				SETUP_SUCCESSFULLY = false;
				Bukkit.getPluginManager().callEvent(new GamemasterObjectBuiltCommodoreEvent(this, connection, true));
				return;
			}
			//TODO
			Bukkit.getPluginManager().callEvent(new GamemasterObjectBuiltCommodoreEvent(this, connection, false));
		} catch (SQLException e) {
			ControlPanel.exceptionThrown(e);
		}
	}
	
	@Override
	protected void save(Connection connection) {
		super.save(connection);
		Bukkit.getPluginManager().callEvent(new GamemasterObjectBeingSavedCommodoreEvent(this, connection));
	}
	
	/* ------------------- */
	/* Implementation part */
	/* ------------------- */
	
	private boolean isVisible = false;

	@Override
	public boolean isVisible() {
		return isVisible;
	}

	@Override
	public void setVisible(boolean isVisible) {
		if(this.isVisible == isVisible)
			return;
		this.isVisible = isVisible;
		
		CommodoreUsersManager manager = Commodore.getUsersManager();
		if(isVisible) {
			manager.actionOnAllOnlinePlayers(this::showToPlayer);
			//TODO: Cool FX
		} else {
			manager.actionOnAllOnlinePlayers(this::hideFromPlayer);
			//TODO: Cool FX
		}
		Bukkit.getServer().getPluginManager().callEvent(new GamemasterVisibilityChangeCommodoreEvent(this));
	}
	
	private void showToPlayer(CommodorePlayer playerObject) {
		Player player = playerObject.getBukkitPlayer();
		Player thisPlayer = getBukkitPlayer();
		player.showPlayer(Initializer.getInstance(), thisPlayer);
	}
	
	private void hideFromPlayer(CommodorePlayer playerObject) {
		Player player = playerObject.getBukkitPlayer();
		Player thisPlayer = getBukkitPlayer();
		player.hidePlayer(Initializer.getInstance(), thisPlayer);
	}
}
