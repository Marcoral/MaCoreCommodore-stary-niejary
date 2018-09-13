package pl.mateam.marpg.engine.core.commands;

import org.bukkit.command.CommandSender;

import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.api.modules.utils.CommodoreMessengingUtils;
import pl.mateam.marpg.api.superclasses.CommodoreGenericCommand;
import pl.mateam.marpg.engine.core.commands.shared.CoreCommandsUtils;

public class CommodoreCommandRegenerate implements CommodoreGenericCommand {
	@Override
	public void invoked(CommandSender executor, String usedLabel, String... args) {
		CommodoreMessengingUtils messenger = Commodore.getUtils().getMessengingUtils();
		if(args.length == 0)
			messenger.commandOutputError("Error: You must specify name of module to regenerate its configuration!", executor);
		else if(args.length > 2) {
			messenger.commandOutputError("Error: Given too many arguments.", executor);
			messenger.commandOutputCasualWithHighlight("Valid usage: ", "/" + usedLabel + " <module name> [-f | -fr]", "", executor);
		} else {
			String moduleName = args[0];
			if(CoreCommandsUtils.checkModuleUnknownAndHandle(moduleName, executor))
				return;
			String passedParameter = args.length == 2? args[1] : null;
			handleCommand(executor, moduleName, passedParameter);
		}
	}
	
	static void handleCommand(CommandSender executor, String moduleName, String passedParameter) {
		if(passedParameter != null) {
			if(passedParameter.equalsIgnoreCase("-f")) {
				overwriteConfiguration(executor, moduleName);
				return;
			} else if(passedParameter.equalsIgnoreCase("-fr")) {
				overwriteConfiguration(executor, moduleName);
				CoreCommandsUtils.reloadComponent(executor, moduleName);
				return;
			} else
				sendMessageInvalidFlag(executor, passedParameter);
		}
		regenerateConfiguration(executor, moduleName);
	}
	
	private static void sendMessageInvalidFlag(CommandSender executor, String passedParameter) {
		CommodoreMessengingUtils messenger = Commodore.getUtils().getMessengingUtils();
		messenger.craftCommandOutputBuilder(executor)
		.colorError("Warning: passed parameter ")
		.colorErrorHighlighted(passedParameter)
		.colorError(" is unknown. Use ")
		.colorErrorHighlighted("-f")
		.colorError(" to override module's configuration (using ")
		.colorErrorHighlighted("-fr")
		.colorError(" will additionally reload module afterwards)").send();
		messenger.commandOutputCasualWithHighlight("", "USE WITH CARE!", "", executor);
		messenger.commandOutputCasual("Regenerating configuration instead...", executor);
	}
	
	private static void regenerateConfiguration(CommandSender executor, String moduleName) {
		Commodore.getFilesManager().regenerateConfiguration(moduleName);
		CommodoreMessengingUtils messenger = Commodore.getUtils().getMessengingUtils();
		messenger.commandOutputSuccessWithHighlight("Configuration of module ", moduleName, " have been regenerated.", executor);
	}
	
	private static void overwriteConfiguration(CommandSender executor, String moduleName) {
		Commodore.getFilesManager().overwriteConfiguration(moduleName);
		CommodoreMessengingUtils messenger = Commodore.getUtils().getMessengingUtils();
		messenger.commandOutputSuccessWithHighlight("Configuration of module ", moduleName, " have been overwritten.", executor);
	}
}
