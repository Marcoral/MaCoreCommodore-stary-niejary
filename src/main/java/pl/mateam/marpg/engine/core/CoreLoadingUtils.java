package pl.mateam.marpg.engine.core;

import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.api.modules.files.CommodoreConfigurationFile;
import pl.mateam.marpg.engine.Initializer;

public class CoreLoadingUtils {
	public static CommodoreConfigurationFile getConfigurationFile(ConfigPath configPath) {
		return Commodore.getFilesManager().getConfig(Initializer.getInstance().getName(), configPath.getPath());
	}
}
