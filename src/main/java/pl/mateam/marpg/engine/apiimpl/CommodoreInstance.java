package pl.mateam.marpg.engine.apiimpl;

import pl.mateam.marpg.api.Commodore_Intermediate;
import pl.mateam.marpg.api.modules.componentsmanager.CommodoreComponentsManager;
import pl.mateam.marpg.api.modules.database.CommodoreDatabase;
import pl.mateam.marpg.api.modules.files.CommodoreFilesManager;
import pl.mateam.marpg.api.modules.utils.CommodoreDevelopmentUtils.Extern;
import pl.mateam.marpg.api.superclasses.CommodoreRuntimeException;
import pl.mateam.marpg.api.modules.utils.CommodoreUtils;
import pl.mateam.marpg.engine.apiimpl.modules.componentsmanager.ComponentsManagerInstance;
import pl.mateam.marpg.engine.apiimpl.modules.files.FilesManagerInstance;
import pl.mateam.marpg.engine.apiimpl.modules.utils.UtilsInstance;
import pl.mateam.marpg.engine.core.Core;

public class CommodoreInstance implements Commodore_Intermediate {
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
	
	/*--------------------------------------------------------------*/
	/*	Core stuff (Core must be set-up before use of below methods	*/
	/*--------------------------------------------------------------*/

	@Extern(key = "core")
	private Core core;
	
	private static class CommodoreCoreNotInitializedException extends CommodoreRuntimeException {
		private static final long serialVersionUID = -2793057693420892672L;

		private CommodoreCoreNotInitializedException() {
			super("Core of Commodore was not set-up before use of its method!");
		}
	}
	
	@Override
	public CommodoreDatabase getDatabase() {
		if(core == null)
			throw new CommodoreCoreNotInitializedException();
		return core.getDatabase();
	}
}
