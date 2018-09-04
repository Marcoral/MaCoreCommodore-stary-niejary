package pl.mateam.marpg.engine.apiimpl.modules.files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.api.CommodoreComponent;
import pl.mateam.marpg.api.modules.componentsmanager.CommodoreComponentsManager.ComponentNotLoadedYetException;
import pl.mateam.marpg.api.modules.files.CommodoreConfigurationFile;
import pl.mateam.marpg.api.modules.files.CommodoreFilesManager;
import pl.mateam.marpg.engine.ControlPanel;
import pl.mateam.marpg.engine.apiimpl.modules.componentsmanager.ComponentsManagerInstance;

public class FilesManagerInstance implements CommodoreFilesManager {
	@Override
	public void regenerateConfiguration(String componentName) throws ComponentNotLoadedYetException {
		ComponentsManagerInstance componentsManager = (ComponentsManagerInstance) Commodore.getComponentsManager();
		regenerateConfiguration(componentsManager.getComponentInstanceOrThrow(componentName));
	}

	@Override
	public void overwriteConfiguration(String componentName) throws ComponentNotLoadedYetException {
		ComponentsManagerInstance componentsManager = (ComponentsManagerInstance) Commodore.getComponentsManager();
		overwriteConfiguration(componentsManager.getComponentInstanceOrThrow(componentName));
	}

	@Override
	public void regenerateConfiguration(CommodoreComponent component) {
		processConfiguration(component, false);
	}

	@Override
	public void overwriteConfiguration(CommodoreComponent component) {
		processConfiguration(component, true);
	}
	
	private void processConfiguration(CommodoreComponent component, boolean overwrite) {
		File destination = component.getPlugin().getDataFolder();
		destination.mkdirs();
		try {
			String rawPath = component.getPlugin().getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
			String utfPath = URLDecoder.decode(rawPath, "UTF-8");
			JarFile jar = new JarFile(utfPath);
			Enumeration<JarEntry> enu = jar.entries();
			while(enu.hasMoreElements()){
			    JarEntry entry = enu.nextElement();
			    String entryPath = entry.getName();
			    if(!entryPath.startsWith("resources"))
			    	continue;
			    entryPath = entryPath.substring("resources".length());
			
			    File fl = new File(destination, entryPath);
			    if(!fl.exists() || overwrite) {
			    	if(!overwrite)
			    		fl.getParentFile().mkdirs();
			        if(entry.isDirectory())
				        continue;
			        try(InputStream is = jar.getInputStream(entry); FileOutputStream fo = new FileOutputStream(fl)) {
			        	while(is.available() > 0)
					        fo.write(is.read());
			        }
			    }
			}
			jar.close();
		} catch (IOException e) {
			ControlPanel.exceptionThrown(e);
		}
	}

	@Override
	public CommodoreConfigurationFile getConfig(String componentName, String relativePath) throws ComponentNotLoadedYetException {
		ComponentsManagerInstance componentsManager = (ComponentsManagerInstance) Commodore.getComponentsManager();
		return getConfig(componentsManager.getComponentInstanceOrThrow(componentName), relativePath);
	}

	@Override
	public CommodoreConfigurationFile getConfig(CommodoreComponent component, String relativePath) {
		String path = component.getPlugin().getDataFolder() + "/" + relativePath;
		return ConfigurationFileInstance.createFor(path);
	}
}
