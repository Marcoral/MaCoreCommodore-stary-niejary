package pl.mateam.marpg.engine.apiimpl.submodules.utils;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.command.CommandSender;

import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.api.submodules.utils.CommodoreUtilsCommands;
import pl.mateam.marpg.api.submodules.utils.CommodoreUtilsMessenging;

public class UtilsCommandsInstance implements CommodoreUtilsCommands {
	private static final Pattern DURATON_CHECK_PATTERN = Pattern.compile("^(\\d+)\\w$");
	public Duration getDuration(String[] arguments, String indicator) {
		Duration duration = null;
		boolean wasFound = false;
		for(String argument : arguments) {
			if(wasFound) {
				Matcher matcher = DURATON_CHECK_PATTERN.matcher(argument);
				if(!matcher.matches())
					return duration;
				Integer value = Integer.parseInt(matcher.group(1));
				switch(argument.charAt(argument.length()-1)) {
					case 's':
						duration = duration.plusSeconds(value);
						break;
					case 'm':
						duration = duration.plusMinutes(value);
						break;
					case 'h':
						duration = duration.plusHours(value);
						break;
					case 'd':
						duration = duration.plusDays(value);
						break;
					default:
						return duration;
				}
			} else if(argument.equalsIgnoreCase(indicator)) {
				duration = Duration.ZERO;
				wasFound = true;
			}
		}
		return duration;
	}
	
	@Override
	public String getParameterValue(String[] arguments, String indicator) {
		boolean wasFound = false;
		StringBuilder builder = null;
		for(String argument : arguments) {
			if(wasFound) {
				if(builder == null) {
					if(!argument.startsWith("\""))
						return argument;
					builder = new StringBuilder();
					argument = argument.substring(1);
				}
				if(builder != null) {
					builder.append(argument);
					if(argument.endsWith("\"")) {
						builder.setLength(builder.length() - 1);
						return builder.toString();
					}
				}
				builder.append(" ");
			} else if(argument.equalsIgnoreCase(indicator))
				wasFound = true;
		}
		if(builder != null) {
			builder.setLength(builder.length() - 1);
			return builder.toString();
		} else
			return null;
	}
	
	@Override
	public void notifyWrongUsage(String errorMessage, String validUsage, CommandSender executor) {
		CommodoreUtilsMessenging messenger = Commodore.getUtils().getMessengingUtils();
		messenger.commandOutputError("Error: " + errorMessage, executor);
		messenger.craftCommandOutputBuilder(executor)
			.colorCasual("Valid usage: ")
			.colorCasualHighlighted("/" + validUsage).send();
		return;
	}
}
