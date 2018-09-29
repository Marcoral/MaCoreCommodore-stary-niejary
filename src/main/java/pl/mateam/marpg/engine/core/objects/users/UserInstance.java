package pl.mateam.marpg.engine.core.objects.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.api.events.UserObjectBeingSavedCommodoreEvent;
import pl.mateam.marpg.api.events.UserObjectBuiltCommodoreEvent;
import pl.mateam.marpg.api.objects.users.CommodoreUser;
import pl.mateam.marpg.api.superclasses.CommodoreRuntimeException;
import pl.mateam.marpg.engine.ControlPanel;
import pl.mateam.marpg.engine.Initializer;
import pl.mateam.marpg.engine.core.internal.CoreUtils;
import pl.mateam.marpg.engine.core.internal.sql.CoreDBField;
import pl.mateam.marpg.engine.core.internal.sql.CoreDBRetrieval;
import pl.mateam.marpg.engine.core.internal.sql.CoreDBUpdate;

public class UserInstance implements CommodoreUser {	
	
	/* --------------- */
	/* Load/save stuff */
	/* --------------- */
	
	protected boolean SETUP_SUCCESSFULLY = true;
	protected UserInstance(Connection connection, String nickname) { 
		try(PreparedStatement statement = CoreUtils.prepareStatementInline(
				connection, CoreDBRetrieval.USER_GENERAL, nickname);
			ResultSet general = statement.executeQuery()) {
			
			if(!general.next()) {
				SETUP_SUCCESSFULLY = false;
				Bukkit.getPluginManager().callEvent(new UserObjectBuiltCommodoreEvent(this, connection, true));
				return;
			}
			this.password = general.getString(CoreDBField.USERS_PASSWORD);
			Bukkit.getPluginManager().callEvent(new UserObjectBuiltCommodoreEvent(this, connection, false));
		} catch (SQLException e) {
			ControlPanel.exceptionThrown(e);
		}
	}
	
	@Override
	public final void save() {
		Bukkit.getScheduler().runTaskAsynchronously(Initializer.getInstance(), () -> {
			try(Connection connection = Commodore.getDatabase().getConnection()) {
				connection.setAutoCommit(false);
				save(connection);
				Bukkit.getPluginManager().callEvent(new UserObjectBeingSavedCommodoreEvent(this, connection));
				connection.commit();
			} catch (SQLException e) {
				ControlPanel.exceptionThrown(e);
			}
		});
	}
	
	protected void save(Connection connection) {
		saveGeneralInfo(connection);
	}
	
	private void saveGeneralInfo(Connection connection) {
		CoreUtils.executeStatementInline(connection, CoreDBUpdate.USER,
				/* last_seen = */		new Date(),
				/* where nickname = */	player.getName());
	}
	
	/* ------------------- */
	/* Implementation part */
	/* ------------------- */
	
	private Player player;
	private String password;
	
	public void setPlayer(Player player) {
		if(this.player != null)
			throw new CommodoreRuntimeException("Player was already set for UserInstance of " + player.getName() + "!");
		this.player = player;
	}

	@Override
	public boolean passwordMatches(String password) {
		return this.password.equals(password);
	}

	@Override
	public Player getBukkitPlayer() {
		return player;
	}
}
