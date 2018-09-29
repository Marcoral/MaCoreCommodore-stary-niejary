package pl.mateam.marpg.engine.core.commands;

import org.bukkit.command.CommandSender;

import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.api.submodules.modulesmanager.CommodoreModulesManager.CommodoreFullReloadResult;
import pl.mateam.marpg.api.superclasses.CommodoreGenericCommand;
import pl.mateam.marpg.engine.core.commands.shared.CoreCommandsUtils;

public class CommodoreCommandReload implements CommodoreGenericCommand {
	@Override
	public void invoked(CommandSender executor, String usedLabel, String... args) {
		if(args.length == 0)
			reloadAll(executor);					
		else
			CoreCommandsUtils.reloadComponent(executor, args);
	}
	
	private void reloadAll(CommandSender executor) {
		CommodoreFullReloadResult result = Commodore.getModulesManager().reloadAllEnabledModules();
		String word = result.getReloadedSubmodulesCount().size() > 1? " modules" : " module";
		Commodore.getUtils().getMessengingUtils().craftCommandOutputBuilder(executor)
			.colorCasual("Reloaded ")
			.colorCasualHighlighted(String.valueOf(result.getReloadedSubmodulesCount().size()))
			.colorCasual(word + " with total of ")
			.colorCasualHighlighted(String.valueOf(result.getTotalReloadedSubmodulesCount()))
			.colorCasual(" submodules:").send();
		result.getReloadedSubmodulesCount().forEach((moduleName, submodulesCount) -> {
			String word2 = submodulesCount > 1? " submodules)" : " submodule)";
			Commodore.getUtils().getMessengingUtils().commandOutputCasualWithHighlight("- ", moduleName + " (" + submodulesCount + word2, "", executor);
		});
		Commodore.getUtils().getMessengingUtils().commandOutputCasual("All enabled Commodore modules have been reloaded!", executor);
	}
}