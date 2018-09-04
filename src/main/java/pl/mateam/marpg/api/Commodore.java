package pl.mateam.marpg.api;

import pl.mateam.marpg.api.modules.componentsmanager.CommodoreComponentsManager;
import pl.mateam.marpg.api.modules.database.CommodoreDatabase;
import pl.mateam.marpg.api.modules.files.CommodoreFilesManager;
import pl.mateam.marpg.api.modules.utils.CommodoreDevelopmentUtils.Extern;
import pl.mateam.marpg.api.modules.utils.CommodoreUtils;

public class Commodore {
	@Extern(key = "instance")
	private static final Commodore_Intermediate COMMODORE_SINGLETON = null;
	
	public static CommodoreComponentsManager getComponentsManager() {
		return COMMODORE_SINGLETON.getComponentsManager();
	}
	
	public static CommodoreDatabase getDatabase() {
		return COMMODORE_SINGLETON.getDatabase();
	}

	public static CommodoreFilesManager getFilesManager() {
		return COMMODORE_SINGLETON.getFilesManager();
	}
	
	public static CommodoreUtils getUtils() {
		return COMMODORE_SINGLETON.getUtils();
	}
}