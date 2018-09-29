package pl.mateam.marpg.engine.core.internal.sql;

public interface CoreDBRetrieval {
	String USER_GENERAL = new StringBuilder("SELECT * FROM ")
			.append(CoreDBTable.USERS)
			.append(" WHERE ")
			.append(CoreDBField.USERS_NICKNAME + " = ?;").toString(); 
	
	String GAMEMASTER_GENERAL = new StringBuilder("SELECT * FROM ")
			.append(CoreDBTable.GAMEMASTERS)
			.append(" WHERE ")
			.append(CoreDBField.USERS_NICKNAME + " = ?;").toString();
	
	String PLAYER_GENERAL = new StringBuilder("SELECT * FROM ")
			.append(CoreDBTable.PLAYERS)
			.append(" WHERE ")
			.append(CoreDBField.USERS_NICKNAME + " = ?;").toString();

	String PLAYER_BANINFO = new StringBuilder("SELECT * FROM ")
			.append(CoreDBTable.BANS)
			.append(" WHERE ")
			.append(CoreDBField.USERS_NICKNAME + " = ?")
			.append(" AND ")
			.append(CoreDBField.BANS_SCOPE + " = ?;").toString();
}