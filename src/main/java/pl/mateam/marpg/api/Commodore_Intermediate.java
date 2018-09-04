package pl.mateam.marpg.api;

import pl.mateam.marpg.api.modules.componentsmanager.CommodoreComponentsManager;
import pl.mateam.marpg.api.modules.database.CommodoreDatabase;
import pl.mateam.marpg.api.modules.files.CommodoreFilesManager;
import pl.mateam.marpg.api.modules.utils.CommodoreUtils;

public interface Commodore_Intermediate {
	CommodoreComponentsManager getComponentsManager();
	CommodoreDatabase getDatabase();
	CommodoreFilesManager getFilesManager();
	CommodoreUtils getUtils();
}
