package pl.mateam.marpg.engine.core.commands;

import org.bukkit.command.CommandSender;

import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.api.modules.utils.CommodoreMessengingUtils;
import pl.mateam.marpg.api.superclasses.CommodoreGenericCommand;
import pl.mateam.marpg.engine.Initializer;

public class CommodoreCommandRegenerateCore implements CommodoreGenericCommand {
	@Override
	public void invoked(CommandSender executor, String usedLabel, String... args) {
		CommodoreMessengingUtils messenger = Commodore.getUtils().getMessengingUtils();			
		if(args.length > 1) {
			messenger.commandOutputError("Error: Given too many arguments.", executor);
			messenger.commandOutputCasualWithHighlight("Valid usage: ", "/" + usedLabel + " [-f | -fr]", "", executor);
		} else {
			String moduleName = Initializer.getInstance().getName();
			String passedParameter = args.length == 1? args[0] : null;
			CommodoreCommandRegenerate.handleCommand(executor, moduleName, passedParameter);
		}
	}
}
