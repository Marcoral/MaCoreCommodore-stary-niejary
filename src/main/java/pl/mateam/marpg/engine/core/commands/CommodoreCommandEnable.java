package pl.mateam.marpg.engine.core.commands;

import java.util.List;
import java.util.Set;

import org.bukkit.command.CommandSender;

import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.api.submodules.modulesmanager.CommodoreModulesManager.CommodoreMassiveEnablingResult;
import pl.mateam.marpg.api.submodules.utils.CommodoreUtilsMessenging;
import pl.mateam.marpg.api.superclasses.CommodoreGenericCommand;
import pl.mateam.marpg.engine.core.commands.shared.CoreCommandsUtils;

public class CommodoreCommandEnable implements CommodoreGenericCommand {
	@Override
	public void invoked(CommandSender executor, String usedLabel, String... args) {
		CommodoreUtilsMessenging messenger = Commodore.getUtils().getMessengingUtils();
		if(args.length == 0) {
			CommodoreMassiveEnablingResult result = Commodore.getModulesManager().enableAllModules();
			List<String> enabledModules = result.getEnabledModulesNames();
			if(enabledModules.size() == 0)
				messenger.commandOutputCasual("No changes. All modules were already enabled.", executor);
			else {
				messenger.commandOutputCasual("Below modules have been enabled:", executor);
				enabledModules.forEach(module -> messenger.commandOutputCasualWithHighlight("- ", module, "", executor));
			}
		} else {
			Set<String> moduleNames = CoreCommandsUtils.createSet(args);
			moduleNames.forEach(moduleName -> {
				if(CoreCommandsUtils.checkModuleUnknownAndHandle(moduleName, executor))
					return;

				boolean result = Commodore.getModulesManager().enableModule(moduleName);
				if(result)
					messenger.commandOutputCasualWithHighlight("Module ", moduleName, " have been enabled!", executor);
				else
					messenger.commandOutputCasualWithHighlight("Module ", moduleName, " was already enabled.", executor);
			});
		}
	}
}
