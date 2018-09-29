package pl.mateam.marpg.engine.core.internal.sql;

import pl.mateam.marpg.api.objects.users.children.bans.CommodoreBanObject.BanScope;

public interface CoreDBValue {
	String VALUE_BANS_GLOBAL = BanScope.GLOBAL.toSQL();
}