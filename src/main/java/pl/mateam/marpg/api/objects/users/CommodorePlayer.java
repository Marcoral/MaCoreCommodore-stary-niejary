package pl.mateam.marpg.api.objects.users;

import java.util.function.Consumer;

import pl.mateam.marpg.api.objects.users.children.bans.CommodoreBanObject;
import pl.mateam.marpg.api.objects.users.children.bans.CommodoreBansBuilder;
import pl.mateam.marpg.api.objects.users.children.bans.CommodoreBanObject.BanScope;

public interface CommodorePlayer extends CommodoreUser {
	CommodoreBanObject getBanInfo(BanScope scope);	//Returns null if player is not banned in given scope
	void setBanned(BanScope scope, int reasonID);
	void setBanned(BanScope scope, String customReason);
	void setBanned(BanScope scope, int reasonID, Consumer<CommodoreBansBuilder> additionalInfo);
	void setBanned(BanScope scope, String customReason, Consumer<CommodoreBansBuilder> additionalInfo);
	
	boolean isWoman();
	
	void setWoman(boolean isWoman);
}
