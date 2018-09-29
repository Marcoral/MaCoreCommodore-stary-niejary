package pl.mateam.marpg.api.submodules.users;

import java.util.function.Consumer;

import pl.mateam.marpg.api.CommodoreInformativeAnnotations.MayRunAsync;
import pl.mateam.marpg.api.CommodoreInformativeAnnotations.UseAsyncOnly;
import pl.mateam.marpg.api.objects.users.CommodoreGamemaster;
import pl.mateam.marpg.api.objects.users.CommodorePlayer;
import pl.mateam.marpg.api.objects.users.CommodoreUser;
import pl.mateam.marpg.api.objects.users.children.bans.CommodoreBanObject;
import pl.mateam.marpg.api.objects.users.children.bans.CommodoreBanObject.BanScope;
import pl.mateam.marpg.api.objects.users.children.bans.CommodoreBansBuilder;

public interface CommodoreUsersManager {
	
	/* ---- */
	/* Bans */
	/* ---- */
	
	@UseAsyncOnly CommodoreBanObject getBanInfo(String nickname, BanScope scope);	//Returns null if player is not banned in given scope
	@MayRunAsync void setBanned(String nickname, BanScope scope, int reasonID);
	@MayRunAsync void setBanned(String nickname, BanScope scope, String customReason);
	@MayRunAsync void setBanned(String nickname, BanScope scope, int reasonID,
					Consumer<CommodoreBansBuilder> additionalInfo);
	@MayRunAsync void setBanned(String nickname, BanScope scope, String customReason,
					Consumer<CommodoreBansBuilder> additionalInfo);
	@MayRunAsync void setBannedAndCheck(String nickname, BanScope scope, int reasonID,
					Consumer<Boolean> actionOnResult);
	@MayRunAsync void setBannedAndCheck(String nickname, BanScope scope, String customReason,
					Consumer<Boolean> actionOnResult);
	@MayRunAsync void setBannedAndCheck(String nickname, BanScope scope, int reasonID,
					Consumer<CommodoreBansBuilder> additionalInfo, Consumer<Boolean> actionOnResult);
	@MayRunAsync void setBannedAndCheck(String nickname, BanScope scope, String customReason,
					Consumer<CommodoreBansBuilder> additionalInfo, Consumer<Boolean> actionOnResult);

	
	void actionOnAllOnlineGamemasters(Consumer<CommodoreGamemaster> action);
	void actionOnAllOnlinePlayers(Consumer<CommodorePlayer> action);
	void actionOnAllOnlineUsers(Consumer<CommodoreUser> action);
	
	/* Do NOT store returned objects anywhere, once you finish work with them!
	 * Instead, fetch them again if necessary. */
	CommodoreGamemaster getOnlineGamemaster(String nickname);
	CommodorePlayer getOnlinePlayer(String nickname);
	CommodoreUser getOnlineUser(String nickname);
}