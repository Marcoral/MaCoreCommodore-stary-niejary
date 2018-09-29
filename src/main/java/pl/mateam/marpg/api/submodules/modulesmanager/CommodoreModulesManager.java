package pl.mateam.marpg.api.submodules.modulesmanager;

import java.util.List;
import java.util.Map;

import pl.mateam.marpg.api.CommodoreComponent;
import pl.mateam.marpg.api.superclasses.CommodoreRuntimeException;

/*
 * Notice that modulesManager uses "CommodoreComponent" interface instead of "CommodoreModule" class.
 * Only making it this way makes possibility of managing of Core.
 */
public interface CommodoreModulesManager {
	public class ModuleNotLoadedYetException extends CommodoreRuntimeException {
		private static final long serialVersionUID = 7349534588292226736L;

		private String moduleName;
		public ModuleNotLoadedYetException(String moduleName) {
			super("Module must be loaded by bukkit before enabling or disabling it!");
			this.moduleName = moduleName;
		}
		
		public final String getPassedModuleName() {
			return moduleName;
		}
	}

	CommodoreMassiveEnablingResult enableAllModules();
	CommodoreMassiveDisablingResult disableAllModules();
	CommodoreFullReloadResult reloadAllEnabledModules();

	boolean isModuleLoaded(String moduleName);
	boolean isModuleEnabled(String moduleName) throws ModuleNotLoadedYetException;
	boolean enableModule(String moduleName) throws ModuleNotLoadedYetException;	//Returns false if module was already enabled
	boolean disableModule(String moduleName) throws ModuleNotLoadedYetException;	//Returns false if module was already disabled
	CommodoreModuleReloadResult reloadModule(String moduleName) throws ModuleNotLoadedYetException;	//Returns null if module was disabled
	CommodoreModuleReloadResult reloadModule(String moduleName, String... parameters) throws ModuleNotLoadedYetException;	//Returns null if module was disabled

	boolean isModuleEnabled(CommodoreComponent module);
	boolean enableModule(CommodoreComponent module);	//Returns false if module was already enabled
	boolean disableModule(CommodoreComponent module);	//Returns false if module was already disabled
	CommodoreModuleReloadResult reloadModule(CommodoreComponent module);	//Returns null if module was disabled
	CommodoreModuleReloadResult reloadModule(CommodoreComponent module, String... parameters);	//Returns null if module was disabled
	
	public interface CommodoreMassiveEnablingResult {
		List<String> getEnabledModulesNames();
	}
	
	public interface CommodoreMassiveDisablingResult {
		List<String> getDisabledModulesNames();
	}
	
	public interface CommodoreModuleReloadResult {
		List<String> getUnknownNames();
		int getReloadedSubmodulesCount();
	}
	
	public interface CommodoreFullReloadResult {		
		Map<String, Integer> getReloadedSubmodulesCount();	//Modulename-submodules count
		List<String> getNotReloadedModulesNames();
		int getTotalReloadedSubmodulesCount();
	}
}
