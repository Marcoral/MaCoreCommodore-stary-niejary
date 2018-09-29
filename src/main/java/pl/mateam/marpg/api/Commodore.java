package pl.mateam.marpg.api;

import pl.mateam.marpg.api.submodules.bans.CommodoreBansManager;
import pl.mateam.marpg.api.submodules.database.CommodoreDatabase;
import pl.mateam.marpg.api.submodules.files.CommodoreFilesManager;
import pl.mateam.marpg.api.submodules.loggers.CommodoreLoggersManager;
import pl.mateam.marpg.api.submodules.modulesmanager.CommodoreModulesManager;
import pl.mateam.marpg.api.submodules.text.CommodoreTextManager;
import pl.mateam.marpg.api.submodules.users.CommodoreUsersManager;
import pl.mateam.marpg.api.submodules.utils.CommodoreUtils;
import pl.mateam.marpg.api.submodules.utils.CommodoreUtilsDevelopment.Extern;

public class Commodore {
	@Extern(key = "instance")
	private static final Commodore_Intermediate COMMODORE_SINGLETON = null;
	
	public static CommodoreBansManager getBansManager() {
		return COMMODORE_SINGLETON.getBansManager();
	}
	
	public static CommodoreDatabase getDatabase() {
		return COMMODORE_SINGLETON.getDatabase();
	}

	public static CommodoreFilesManager getFilesManager() {
		return COMMODORE_SINGLETON.getFilesManager();
	}
	
	public static CommodoreLoggersManager getLoggersManager() {
		return COMMODORE_SINGLETON.getLoggersManager();
	}
	
	public static CommodoreModulesManager getModulesManager() {
		return COMMODORE_SINGLETON.getModulesManager();
	}
	
	public static CommodoreTextManager getTextManager() {
		return COMMODORE_SINGLETON.getTextManager();
	}
	
	public static CommodoreUsersManager getUsersManager() {
		return COMMODORE_SINGLETON.getUsersManager();
	}
	
	public static CommodoreUtils getUtils() {
		return COMMODORE_SINGLETON.getUtils();
	}
}