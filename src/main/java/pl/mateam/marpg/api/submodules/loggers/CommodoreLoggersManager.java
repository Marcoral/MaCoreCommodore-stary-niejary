package pl.mateam.marpg.api.submodules.loggers;

import pl.mateam.marpg.api.submodules.utils.CommodoreUtilsMessenging.Logger;
import pl.mateam.marpg.api.superclasses.LogLevel;

public interface CommodoreLoggersManager {
	LogLevel getLoggerLevel(Logger logger);
	void log(String message, Logger logger, LogLevel level);
	//TODO: Custom loggers
}
