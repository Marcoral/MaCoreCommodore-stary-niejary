package pl.mateam.marpg.engine.core.commands;

import java.text.MessageFormat;

import org.bukkit.command.CommandSender;

import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.api.superclasses.CommodoreGenericCommand;
import pl.mateam.marpg.engine.Initializer;

public class CommodoreCommandRegenerateCore implements CommodoreGenericCommand {
	private static final String VALID_USAGE = "{0} [-f | -fr]";
	
	@Override
	public void invoked(CommandSender executor, String usedLabel, String... args) {
		if(args.length > 1)
			Commodore.getUtils().getCommandsUtils().notifyWrongUsage(
					"Given too many arguments.", MessageFormat.format(VALID_USAGE, usedLabel), executor);
		else {
			String moduleName = Initializer.getInstance().getName();
			String passedParameter = args.length == 1? args[0] : null;
			CommodoreCommandRegenerate.handleCommand(executor, moduleName, passedParameter);
		}
	}
}
