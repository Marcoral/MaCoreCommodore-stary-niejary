package pl.mateam.marpg.engine.core.commands;

import java.util.List;
import java.util.Set;

import org.bukkit.command.CommandSender;

import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.api.modules.modulesmanager.CommodoreModulesManager.CommodoreMassiveDisablingResult;
import pl.mateam.marpg.api.modules.utils.CommodoreMessengingUtils;
import pl.mateam.marpg.api.superclasses.CommodoreGenericCommand;
import pl.mateam.marpg.engine.Initializer;
import pl.mateam.marpg.engine.core.commands.shared.CoreCommandsUtils;


public class CommodoreCommandDisable implements CommodoreGenericCommand {
	@Override
	public void invoked(CommandSender executor, String usedLabel, String... args) {
		CommodoreMessengingUtils messenger = Commodore.getUtils().getMessengingUtils();
		if(args.length == 0) {
			CommodoreMassiveDisablingResult result = Commodore.getModulesManager().disableAllModules();
			List<String> disabledModules = result.getDisabledModulesNames();
			if(disabledModules.size() == 0)
				messenger.commandOutputCasual("No changes. Core was only not-disabled module.", executor);
			else {
				messenger.commandOutputCasual("Below modules have been disabled:", executor);
				disabledModules.forEach(module -> messenger.commandOutputCasualWithHighlight("- ", module, "", executor));
			}
		} else {
			Set<String> moduleNames = CoreCommandsUtils.createSet(args);
			moduleNames.forEach(moduleName -> {
				if(CoreCommandsUtils.checkModuleUnknownAndHandle(moduleName, executor))
					return;
				
				if(moduleName.equalsIgnoreCase(Initializer.getInstance().getName())) {
					messenger.commandOutputErrorWithHighlight("", Initializer.getInstance().getName(), " can not be disabled!", executor);
					return;
				}

				boolean result = Commodore.getModulesManager().disableModule(moduleName);
				if(result)
					messenger.commandOutputCasualWithHighlight("Module ", moduleName, " have been disabled!", executor);
				else
					messenger.commandOutputCasualWithHighlight("Module ", moduleName, " was already disabled.", executor);
			});
		}
	}
}
