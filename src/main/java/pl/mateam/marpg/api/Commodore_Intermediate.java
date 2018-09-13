package pl.mateam.marpg.api;

import pl.mateam.marpg.api.modules.database.CommodoreDatabase;
import pl.mateam.marpg.api.modules.files.CommodoreFilesManager;
import pl.mateam.marpg.api.modules.loggers.CommodoreLoggersManager;
import pl.mateam.marpg.api.modules.modulesmanager.CommodoreModulesManager;
import pl.mateam.marpg.api.modules.utils.CommodoreUtils;

public interface Commodore_Intermediate {
	CommodoreDatabase getDatabase();
	CommodoreFilesManager getFilesManager();
	CommodoreLoggersManager getLoggersManager();
	CommodoreModulesManager getModulesManager();
	CommodoreUtils getUtils();
}
