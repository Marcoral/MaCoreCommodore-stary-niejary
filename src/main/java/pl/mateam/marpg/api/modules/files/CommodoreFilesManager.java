package pl.mateam.marpg.api.modules.files;

import pl.mateam.marpg.api.CommodoreComponent;
import pl.mateam.marpg.api.modules.componentsmanager.CommodoreComponentsManager.ComponentNotLoadedYetException;

public interface CommodoreFilesManager {
	void regenerateConfiguration(String componentName) throws ComponentNotLoadedYetException;
	void overwriteConfiguration(String componentName) throws ComponentNotLoadedYetException;
	CommodoreConfigurationFile getConfig(String componentName, String relativePath) throws ComponentNotLoadedYetException;

	void regenerateConfiguration(CommodoreComponent component);
	void overwriteConfiguration(CommodoreComponent component);
	CommodoreConfigurationFile getConfig(CommodoreComponent component, String relativePath);
}
