package pl.mateam.marpg.engine.core.internal.sql;

import static pl.mateam.marpg.engine.core.internal.CoreUtils.buildTransaction;

public interface CoreDBInsertion {
	String USER = "INSERT INTO " + CoreDBTable.USERS + " VALUES (, ?, ?);";
	String PLAYER = 
			buildTransaction(
					USER,
					"INSERT INTO " + CoreDBTable.PLAYERS + " VALUES (?, 0);"
				);	//0 stands for "male"
	
	String GAMEMASTER = 
			buildTransaction(
					USER,
					"INSERT INTO " + CoreDBTable.GAMEMASTERS + " VALUES (?);"
				);
	
	String PLAYERS_BAN = 
			"REPLACE INTO " + CoreDBTable.BANS + " SELECT ?, ?, ?, ?, ?, ?, ? WHERE "
		+	"EXISTS (SELECT * FROM " + CoreDBTable.PLAYERS + " WHERE " + CoreDBField.USERS_NICKNAME + " = ?);";
}