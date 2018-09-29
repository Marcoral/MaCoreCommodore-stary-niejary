package pl.mateam.marpg.api.submodules.utils;

import java.time.Duration;

import org.bukkit.command.CommandSender;

public interface CommodoreUtilsCommands {
	Duration getDuration(String[] arguments, String indicator);
	String getParameterValue(String[] arguments, String indicator);
	void notifyWrongUsage(String errorMessage, String validUsage, CommandSender executor);
}
