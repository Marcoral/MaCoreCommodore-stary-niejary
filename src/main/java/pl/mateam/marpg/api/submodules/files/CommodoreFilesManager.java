package pl.mateam.marpg.api.submodules.files;

import pl.mateam.marpg.api.CommodoreComponent;
import pl.mateam.marpg.api.submodules.modulesmanager.CommodoreModulesManager.ModuleNotLoadedYetException;

public interface CommodoreFilesManager {
	void regenerateConfiguration(String componentName) throws ModuleNotLoadedYetException;
	void overwriteConfiguration(String componentName) throws ModuleNotLoadedYetException;
	CommodoreConfigurationFile getConfig(String componentName, String relativePath) throws ModuleNotLoadedYetException;

	void regenerateConfiguration(CommodoreComponent component);
	void overwriteConfiguration(CommodoreComponent component);
	CommodoreConfigurationFile getConfig(CommodoreComponent component, String relativePath);
}
