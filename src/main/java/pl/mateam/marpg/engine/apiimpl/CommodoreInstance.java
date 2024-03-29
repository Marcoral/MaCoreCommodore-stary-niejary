package pl.mateam.marpg.engine.apiimpl;

import pl.mateam.marpg.api.Commodore_Intermediate;
import pl.mateam.marpg.api.submodules.bans.CommodoreBansManager;
import pl.mateam.marpg.api.submodules.database.CommodoreDatabase;
import pl.mateam.marpg.api.submodules.files.CommodoreFilesManager;
import pl.mateam.marpg.api.submodules.loggers.CommodoreLoggersManager;
import pl.mateam.marpg.api.submodules.modulesmanager.CommodoreModulesManager;
import pl.mateam.marpg.api.submodules.text.CommodoreTextManager;
import pl.mateam.marpg.api.submodules.users.CommodoreUsersManager;
import pl.mateam.marpg.api.submodules.utils.CommodoreUtils;
import pl.mateam.marpg.api.submodules.utils.CommodoreUtilsDevelopment.Extern;
import pl.mateam.marpg.api.superclasses.CommodoreRuntimeException;
import pl.mateam.marpg.engine.apiimpl.submodules.files.FilesManagerInstance;
import pl.mateam.marpg.engine.apiimpl.submodules.modulesmanager.ComponentsManagerInstance;
import pl.mateam.marpg.engine.apiimpl.submodules.utils.UtilsInstance;
import pl.mateam.marpg.engine.core.Core;

public class CommodoreInstance implements Commodore_Intermediate {
	private CommodoreModulesManager modulesManager = new ComponentsManagerInstance();
	private CommodoreFilesManager filesManager = new FilesManagerInstance();
	private CommodoreUtils utils = new UtilsInstance();
	
	@Override
	public CommodoreFilesManager getFilesManager() {
		return filesManager;
	}
	
	@Override
	public CommodoreModulesManager getModulesManager() {
		return modulesManager;
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
	
	private void validateCommodoreNotNull() {
		if(core == null)
			throw new CommodoreCoreNotInitializedException();
	}
	
	@Override
	public CommodoreBansManager getBansManager() {
		return core.getBansManager();
	}
	
	@Override
	public CommodoreDatabase getDatabase() {
		validateCommodoreNotNull();
		return core.getDatabase();
	}
	
	@Override
	public CommodoreLoggersManager getLoggersManager() {
		validateCommodoreNotNull();
		return core.getLoggersManager();
	}
	
	@Override
	public CommodoreTextManager getTextManager() {
		validateCommodoreNotNull();
		return core.getTextManager();
	}
	
	@Override
	public CommodoreUsersManager getUsersManager() {
		validateCommodoreNotNull();
		return core.getUsersManager();
	}
}
