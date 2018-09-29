package pl.mateam.marpg.engine.core.internal.sql;

public interface CoreDBField {
	String USERS_NICKNAME = "nickname";
	String USERS_PASSWORD = "password";
	String USERS_LASTSEEN = "last_seen";

	String PLAYERS_ISWOMAN = "is_woman";
	
	String GAMEMASTERS_IP = "last_ip";
	
	String BANS_SCOPE = "scope";
	String BANS_WHOBANNED = "who_banned";
	String BANS_REASON = "reason";
	String BANS_CUSTOMREASON = "custom_reason";
	String BANS_DATE = "date";
	String BANS_EXPIRES = "expires";
}