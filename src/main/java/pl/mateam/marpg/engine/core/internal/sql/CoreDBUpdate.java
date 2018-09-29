package pl.mateam.marpg.engine.core.internal.sql;

public interface CoreDBUpdate {
	String USER = new StringBuilder("UPDATE ")
			.append(CoreDBTable.USERS)
			.append(" SET ")
			.append(CoreDBField.USERS_LASTSEEN).append(" = ?")
			.append(" WHERE ").append(CoreDBField.USERS_NICKNAME).append(" = ?;").toString();
	
	String PLAYER = new StringBuilder("UPDATE ")
			.append(CoreDBTable.PLAYERS)
			.append(" SET ")
			.append(CoreDBField.PLAYERS_ISWOMAN).append(" = ?")
			.append(" WHERE ").append(CoreDBField.USERS_NICKNAME).append(" = ?;").toString();
}
