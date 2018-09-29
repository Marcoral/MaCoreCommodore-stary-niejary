package pl.mateam.marpg.api;

import pl.mateam.marpg.api.submodules.bans.CommodoreBansManager;
import pl.mateam.marpg.api.submodules.database.CommodoreDatabase;
import pl.mateam.marpg.api.submodules.files.CommodoreFilesManager;
import pl.mateam.marpg.api.submodules.loggers.CommodoreLoggersManager;
import pl.mateam.marpg.api.submodules.modulesmanager.CommodoreModulesManager;
import pl.mateam.marpg.api.submodules.text.CommodoreTextManager;
import pl.mateam.marpg.api.submodules.users.CommodoreUsersManager;
import pl.mateam.marpg.api.submodules.utils.CommodoreUtils;

public interface Commodore_Intermediate {
	CommodoreBansManager getBansManager();
	CommodoreDatabase getDatabase();
	CommodoreFilesManager getFilesManager();
	CommodoreLoggersManager getLoggersManager();
	CommodoreModulesManager getModulesManager();
	CommodoreTextManager getTextManager();
	CommodoreUsersManager getUsersManager();
	CommodoreUtils getUtils();
}
