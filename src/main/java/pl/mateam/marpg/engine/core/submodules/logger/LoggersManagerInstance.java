package pl.mateam.marpg.engine.core.submodules.logger;

import java.util.EnumMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.api.submodules.files.CommodoreConfigurationFile;
import pl.mateam.marpg.api.submodules.loggers.CommodoreLoggersManager;
import pl.mateam.marpg.api.submodules.utils.CommodoreUtilsMessenging.Logger;
import pl.mateam.marpg.api.superclasses.LogLevel;
import pl.mateam.marpg.api.superclasses.CommodoreReloadableSubmodule;
import pl.mateam.marpg.engine.core.internal.ConfigPath;
import pl.mateam.marpg.engine.core.internal.CoreUtils;
import pl.mateam.marpg.engine.core.submodules.CommodoreCoreSetupException;

public class LoggersManagerInstance implements CommodoreLoggersManager, CommodoreReloadableSubmodule {

	/* ------------------- */
	/* Core submodule part */
	/* ------------------- */

	private static Map<Logger, LogLevel> loggers = new EnumMap<>(Logger.class);
	
	@Override
	public void setup() {
		reload();
	}

	@Override
	public void reload() {
		CommodoreConfigurationFile config = CoreUtils.getConfigurationFile(ConfigPath.ENVIRONMENT);
		if(!config.exists())
			throw new CommodoreCoreSetupException("Logger setup config is missing!");
				
		YamlConfiguration configData = config.getData();
		setupLogger(configData, Logger.LOGGER_SETUP);	//Must be setup to log errors during setup of other loggers
		for(Logger logger : Logger.values())
			if(logger != Logger.LOGGER_SETUP)
				setupLogger(configData, logger);
		Commodore.getUtils().getMessengingUtils().craftLogger(Logger.LOGGER_SETUP, LogLevel.FULL).colorSuccessHighlighted("Successfully set up loggers.").send();
	}
	
	private void setupLogger(ConfigurationSection data, Logger logger) {
		String configPath = "LoggingLevels." + logger.getName();
		if(!data.contains(configPath)) {
			loggers.put(logger, logger.getDefaultLoggingLevel());
			return;
		}
			
		String stringLevel = data.getString(configPath);
		LogLevel level = getLevelUsingString(stringLevel);
		if(level != null) {
			loggers.put(logger, level);
			return;
		}
				
		level = logger.getDefaultLoggingLevel();
		loggers.put(logger, level);
		Commodore.getUtils().getMessengingUtils().craftLogger(Logger.LOGGER_SETUP, LogLevel.ERROR)
			.colorError("Given invalid logger level: ")
			.colorErrorHighlighted(stringLevel)
			.colorError(" for logger ")
			.colorErrorHighlighted(logger.name())
			.colorError(". Using default level, ")
			.colorErrorHighlighted(level.toString())
			.colorError(".").send();
	}
	
	private static LogLevel getLevelUsingString(String key) {
		try {
			LogLevel logLevel = LogLevel.valueOf(key.toUpperCase());
			return logLevel; 
		} catch(Exception e) {
			return null;
		}
	}
	
	/* ------------------- */
	/* Implementation part */
	/* ------------------- */

	@Override
	public void log(String message, Logger logger, LogLevel level) {
		if(loggers.get(logger).getLevel() >= level.getLevel())		
			Bukkit.getConsoleSender().sendMessage(message);
	}
	
	@Override
	public LogLevel getLoggerLevel(Logger logger) {
		return loggers.get(logger);
	}

}
