package pl.mateam.marpg.api.submodules.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pl.mateam.marpg.api.superclasses.LogLevel;

public interface CommodoreUtilsMessenging {
	MessageBuilder craftMessageBuilder(Player receiver);
	default void sendCasual(String message, Player receiver) {
		craftMessageBuilder(receiver).colorCasual(message).send();
	}
	default void sendCasualWithHighlight(String part1, String highlightedPart, String part2, Player receiver) {
		craftMessageBuilder(receiver).colorCasual(part1).colorCasualHighlighted(highlightedPart).colorCasual(part2).send();
	}
	default void sendError(String message, Player receiver) {
		craftMessageBuilder(receiver).colorError(message).send();
	}
	default void sendErrorWithHighlight(String part1, String highlightedPart, String part2, Player receiver) {
		craftMessageBuilder(receiver).colorError(part1).colorErrorHighlighted(highlightedPart).colorError(part2).send();
	}
	default void sendSuccess(String message, Player receiver) {
		craftMessageBuilder(receiver).colorSuccess(message).send();
	}
	default void sendSuccessWithHighlight(String part1, String highlightedPart, String part2, Player receiver) {
		craftMessageBuilder(receiver).colorSuccess(part1).colorSuccessHighlighted(highlightedPart).colorSuccess(part2).send();
	}
	
	
	MessageBuilder craftLogger(Logger logger, LogLevel level);
	default void logCasual(String message, Logger logger, LogLevel level) {
		craftLogger(logger, level).colorCasual(message).send();
	}
	default void logCasualWithHighlight(String part1, String highlightedPart, String part2, Logger logger, LogLevel level) {
		craftLogger(logger, level).colorCasual(part1).colorCasualHighlighted(highlightedPart).colorCasual(part2).send();
	}
	default void logError(String message, Logger logger, LogLevel level) {
		craftLogger(logger, level).colorError(message).send();
	}
	default void logErrorWithHighlight(String part1, String highlightedPart, String part2, Logger logger, LogLevel level) {
		craftLogger(logger, level).colorError(part1).colorErrorHighlighted(highlightedPart).colorError(part2).send();
	}
	default void logSuccess(String message, Logger logger, LogLevel level) {
		craftLogger(logger, level).colorSuccess(message).send();
	}
	default void logSuccessWithHighlight(String part1, String highlightedPart, String part2, Logger logger, LogLevel level) {
		craftLogger(logger, level).colorSuccess(part1).colorSuccessHighlighted(highlightedPart).colorSuccess(part2).send();
	}
	
	
	MessageBuilder craftCommandOutputBuilder(CommandSender commandSender);
	default void commandOutputCasual(String message, CommandSender commandSender) {
		craftCommandOutputBuilder(commandSender).colorCasual(message).send();
	}
	default void commandOutputCasualWithHighlight(String part1, String highlightedPart, String part2, CommandSender commandSender) {
		craftCommandOutputBuilder(commandSender).colorCasual(part1).colorCasualHighlighted(highlightedPart).colorCasual(part2).send();
	}
	default void commandOutputError(String message, CommandSender commandSender) {
		craftCommandOutputBuilder(commandSender).colorError(message).send();
	}
	default void commandOutputErrorWithHighlight(String part1, String highlightedPart, String part2, CommandSender commandSender) {
		craftCommandOutputBuilder(commandSender).colorError(part1).colorErrorHighlighted(highlightedPart).colorError(part2).send();
	}
	default void commandOutputSuccess(String message, CommandSender commandSender) {
		craftCommandOutputBuilder(commandSender).colorSuccess(message).send();
	}
	default void commandOutputSuccessWithHighlight(String part1, String highlightedPart, String part2, CommandSender commandSender) {
		craftCommandOutputBuilder(commandSender).colorSuccess(part1).colorSuccessHighlighted(highlightedPart).colorSuccess(part2).send();
	}
	
	
	public interface MessageBuilder {
		@Override
		String toString();
		
		void send();
		
		MessageBuilder append(String text);
		MessageBuilder colorCasual();
		MessageBuilder colorCasual(String text);
		MessageBuilder colorCasualHighlighted();
		MessageBuilder colorCasualHighlighted(String text);
		MessageBuilder colorError();
		MessageBuilder colorError(String text);
		MessageBuilder colorErrorHighlighted();
		MessageBuilder colorErrorHighlighted(String text);
		MessageBuilder colorSuccess();
		MessageBuilder colorSuccess(String text);
		MessageBuilder colorSuccessHighlighted();
		MessageBuilder colorSuccessHighlighted(String text);
		MessageBuilder obfuscated();
		MessageBuilder obfuscated(String text);
		MessageBuilder bold();
		MessageBuilder bold(String text);
		MessageBuilder strikethrough();
		MessageBuilder strikethrough(String text);
		MessageBuilder underline();
		MessageBuilder underline(String text);
		MessageBuilder italic(String text);
		MessageBuilder italic();
		MessageBuilder formatReset();
		MessageBuilder formatReset(String text);
	}

	
	public static enum Logger {
		LOGGER_SETUP(LogLevel.FULL, "LoggerSetup"),
		LOGGER_RUNTIME(LogLevel.FULL, "LoggerRuntime");
		
		private final LogLevel defaultLoggingLevel;
		private final String name;
		private Logger(LogLevel defaultLoggingLevel, String name) {
			this.defaultLoggingLevel = defaultLoggingLevel;
			this.name = name;
		}
		
		public LogLevel getDefaultLoggingLevel() {
			return defaultLoggingLevel;
		}
		
		public String getName() {
			return name;
		}
	}
}
