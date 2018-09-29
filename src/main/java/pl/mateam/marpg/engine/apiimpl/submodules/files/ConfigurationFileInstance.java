package pl.mateam.marpg.engine.apiimpl.submodules.files;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

import pl.mateam.marpg.api.submodules.files.CommodoreConfigurationFile;
import pl.mateam.marpg.engine.ControlPanel;

public class ConfigurationFileInstance implements CommodoreConfigurationFile {
	public static CommodoreConfigurationFile createFor(String filePath) {
		return new ConfigurationFileInstance(new File(filePath));
	}

	private File destination;
	private YamlConfiguration config;
	private ConfigurationFileInstance(File destination) {
		this.destination = destination;
	}
	
	@Override
	public boolean exists() {
		return destination.exists();
	}

	@Override
	public YamlConfiguration getData() {
		if(config != null)
			return config;
		if(destination.exists())
			config = YamlConfiguration.loadConfiguration(destination);
		else
			config = new YamlConfiguration();
		return config;
	}

	@Override
	public void saveData() {
		if(config == null)
			return;
		try {
			if(!destination.exists()) {
				destination.getParentFile().mkdirs();
				destination.createNewFile();
			}
			config.save(destination);
		} catch (IOException e) {
			ControlPanel.exceptionThrown(e);
		}
	}
}
