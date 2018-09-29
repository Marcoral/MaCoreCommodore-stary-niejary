package pl.mateam.marpg.api.submodules.bans;

import pl.mateam.marpg.api.objects.users.children.bans.CommodoreBanObject.BanScope;

public interface CommodoreBansManager {
	String getReasonFor(BanScope scope, int option);
}
