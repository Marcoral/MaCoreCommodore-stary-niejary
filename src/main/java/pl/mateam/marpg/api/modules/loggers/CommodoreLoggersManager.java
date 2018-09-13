package pl.mateam.marpg.api.modules.loggers;

import pl.mateam.marpg.api.superclasses.LogLevel;

import pl.mateam.marpg.api.modules.utils.CommodoreMessengingUtils.Logger;

public interface CommodoreLoggersManager {
	LogLevel getLoggerLevel(Logger logger);
	void log(String message, Logger logger, LogLevel level);
	//TODO: Custom loggers
}
