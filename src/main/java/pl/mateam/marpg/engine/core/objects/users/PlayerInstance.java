package pl.mateam.marpg.engine.core.objects.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.Bukkit;

import pl.mateam.marpg.api.events.PlayerObjectBeingSavedCommodoreEvent;
import pl.mateam.marpg.api.events.PlayerObjectBuiltCommodoreEvent;
import pl.mateam.marpg.api.objects.users.CommodorePlayer;
import pl.mateam.marpg.api.objects.users.children.bans.CommodoreBanObject;
import pl.mateam.marpg.api.objects.users.children.bans.CommodoreBanObject.BanScope;
import pl.mateam.marpg.api.objects.users.children.bans.CommodoreBansBuilder;
import pl.mateam.marpg.engine.ControlPanel;
import pl.mateam.marpg.engine.core.internal.CoreUtils;
import pl.mateam.marpg.engine.core.internal.sql.CoreDBField;
import pl.mateam.marpg.engine.core.internal.sql.CoreDBRetrieval;
import pl.mateam.marpg.engine.core.internal.sql.CoreDBUpdate;
import pl.mateam.marpg.engine.core.internal.sql.CoreDBValue;
import pl.mateam.marpg.engine.core.objects.users.children.bans.BanObjectInstance;
import pl.mateam.marpg.engine.core.objects.users.children.bans.BansBuilderInstance;

public class PlayerInstance extends UserInstance implements CommodorePlayer {
	
	/* --------------- */
	/* Load/save stuff */
	/* --------------- */
	
	public PlayerInstance(Connection connection, String nickname) {
		super(connection, nickname);
		if(!SETUP_SUCCESSFULLY)
			return;
		try(PreparedStatement statement = CoreUtils.prepareStatementInline(
				connection, CoreDBRetrieval.PLAYER_GENERAL, nickname);
			ResultSet general = statement.executeQuery()) {
			
			if(!general.next()) {
				SETUP_SUCCESSFULLY = false;
				Bukkit.getPluginManager().callEvent(new PlayerObjectBuiltCommodoreEvent(this, connection, true));
				return;
			}
			this.isWoman = general.getBoolean(CoreDBField.PLAYERS_ISWOMAN);
			try(PreparedStatement statementBans = CoreUtils.prepareStatementInline
					(connection, CoreDBRetrieval.PLAYER_BANINFO, nickname, CoreDBValue.VALUE_BANS_GLOBAL);
				ResultSet bans = statementBans.executeQuery()) {
					
				while(bans.next()) {
					BanScope scope = BanScope.fromSQL(bans.getString(CoreDBField.BANS_SCOPE)); 
					BanObjectInstance banObject = BanObjectInstance.fromDatabase(bans);
					if(banObject != null)
						banInfo.put(scope, banObject);
				}
			}
			Bukkit.getPluginManager().callEvent(new PlayerObjectBuiltCommodoreEvent(this, connection, false));
		} catch (SQLException e) {
			ControlPanel.exceptionThrown(e);
		}
	}

	@Override
	protected void save(Connection connection) {
		super.save(connection);
		saveGeneralInfo(connection);
		saveBanInfo(connection);
		Bukkit.getPluginManager().callEvent(new PlayerObjectBeingSavedCommodoreEvent(this, connection));
	}
	
	private void saveGeneralInfo(Connection connection) {
		CoreUtils.executeStatementInline(connection, CoreDBUpdate.PLAYER,
			/* is_woman = */	isWoman,
			/* where nickname = */	getBukkitPlayer().getName());
	}
	
	private void saveBanInfo(Connection connection) {
		banInfo.values().forEach(banInfo -> banInfo.save(connection, getBukkitPlayer().getName()));
	}
	
	/* ------------------- */
	/* Implementation part */
	/* ------------------- */
	
	private Map<BanScope, BanObjectInstance> banInfo = new EnumMap<>(BanScope.class);
	private boolean isWoman;
	
	@Override
	public CommodoreBanObject getBanInfo(BanScope scope) {
		CommodoreBanObject result = banInfo.get(scope);
		if(result == null)
			return null;
		if(result.getExpirationDate().after(new Date()))
			return result;
		banInfo.remove(scope);
		return null;
	}

	@Override
	public boolean isWoman() {
		return isWoman;
	}

	@Override
	public void setWoman(boolean isWoman) {
		this.isWoman = isWoman;
	}

	@Override
	public void setBanned(BanScope scope, int reasonID) {
		BanObjectInstance banObject = new BanObjectInstance(scope, reasonID);
		setBanned(scope, banObject);
	}

	@Override
	public void setBanned(BanScope scope, String customReason) {
		BanObjectInstance banObject = new BanObjectInstance(customReason);
		setBanned(scope, banObject);
	}

	@Override
	public void setBanned(BanScope scope, int reasonID, Consumer<CommodoreBansBuilder> additionalInfo) {
		BanObjectInstance banObject = new BanObjectInstance(scope, reasonID);
		setBanned(scope, banObject, additionalInfo);
	}

	@Override
	public void setBanned(BanScope scope, String customReason, Consumer<CommodoreBansBuilder> additionalInfo) {
		BanObjectInstance banObject = new BanObjectInstance(customReason);
		setBanned(scope, banObject, additionalInfo);
	}
	
	private void setBanned(BanScope scope, BanObjectInstance banObject) {
		banInfo.put(scope, banObject);
		updateAfterBan(scope, banObject);
	}
	
	private void setBanned(BanScope scope, BanObjectInstance banObject, Consumer<CommodoreBansBuilder> additionalInfo) {
		BansBuilderInstance builder = new BansBuilderInstance(banObject);
		additionalInfo.accept(builder);

		BanObjectInstance previousBanObject = (BanObjectInstance) getBanInfo(scope);
		if(previousBanObject == null) {
			setBanned(scope, banObject);
			return;
		}

		Date newExpirationDate = builder.getBlendMode().applyTo(banObject.getExpirationDate(), previousBanObject.getExpirationDate());
		banObject.setExpirationDate(newExpirationDate);
		setBanned(scope, banObject);
	}
	
	private void updateAfterBan(BanScope scope, CommodoreBanObject banObject) {
		switch(scope) {
			case GLOBAL:
				getBukkitPlayer().kickPlayer(banObject.getFormattedReason());
				break;
			case CHAT_MAIN:
				//TODO
				break;
			case CHAT_TRADE:
				//TODO
				break;
			case CHAT_PRIVATE:
				//TODO
				break;
		}
	}
}
