package pl.mateam.marpg.api.modules.files;

import org.bukkit.configuration.file.YamlConfiguration;

public interface CommodoreConfigurationFile {
	boolean exists();
	YamlConfiguration getData();
	void saveData();
}
