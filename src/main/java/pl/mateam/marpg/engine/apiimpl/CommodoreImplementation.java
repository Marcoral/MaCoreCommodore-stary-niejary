package pl.mateam.marpg.engine.apiimpl;

import pl.mateam.marpg.api.Commodore_Intermediate;
import pl.mateam.marpg.api.modules.componentsmanager.CommodoreComponentsManager;
import pl.mateam.marpg.api.modules.files.CommodoreFilesManager;
import pl.mateam.marpg.api.modules.utils.CommodoreUtils;
import pl.mateam.marpg.engine.apiimpl.modules.componentsmanager.ComponentsManagerInstance;
import pl.mateam.marpg.engine.apiimpl.modules.files.FilesManagerInstance;
import pl.mateam.marpg.engine.apiimpl.modules.utils.UtilsInstance;

public class CommodoreImplementation implements Commodore_Intermediate {
	private CommodoreComponentsManager componentsManager = new ComponentsManagerInstance();
	private CommodoreFilesManager filesManager = new FilesManagerInstance();
	private CommodoreUtils utils = new UtilsInstance();
	
	@Override
	public CommodoreComponentsManager getComponentsManager() {
		return componentsManager;
	}
	
	@Override
	public CommodoreFilesManager getFilesManager() {
		return filesManager;
	}
	
	@Override
	public CommodoreUtils getUtils() {
		return utils;
	}
}
