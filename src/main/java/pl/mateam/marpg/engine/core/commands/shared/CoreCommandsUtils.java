package pl.mateam.marpg.engine.core.commands.shared;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.bukkit.command.CommandSender;

import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.api.modules.modulesmanager.CommodoreModulesManager;
import pl.mateam.marpg.api.modules.modulesmanager.CommodoreModulesManager.CommodoreModuleReloadResult;
import pl.mateam.marpg.api.modules.utils.CommodoreMessengingUtils;

public class CoreCommandsUtils {
	public static void reloadComponent(CommandSender executor, String... arguments) {
		CommodoreModulesManager manager = Commodore.getModulesManager();
		CommodoreMessengingUtils messenger = Commodore.getUtils().getMessengingUtils();
		String module = arguments[0];
		if(checkModuleUnknownAndHandle(module, executor))
			return;
		String[] reloadArguments = Arrays.copyOfRange(arguments, 1, arguments.length);
		CommodoreModuleReloadResult result = manager.reloadModule(module, reloadArguments);
		if(result == null) {
			messenger.commandOutputErrorWithHighlight("Module named ", module, " is disabled!", executor);
			return;
		}
		messenger.craftCommandOutputBuilder(executor)
		.colorCasual("Reloaded module ")
		.colorCasualHighlighted(module)
		.colorCasual(" with total of ")
		.colorCasualHighlighted(String.valueOf(result.getReloadedSubmodulesCount()))
		.colorCasual(" submodule(s).").send();
		
		if(result.getUnknownNames().size() > 0) {
			messenger.commandOutputError("Anyway I couldn't handle below arguments:", executor);
			result.getUnknownNames().forEach(unknownName -> messenger.commandOutputErrorWithHighlight("- ", unknownName, "", executor));
		}
	}
	
	public static boolean checkModuleUnknownAndHandle(String moduleName, CommandSender executor) {
		if(Commodore.getModulesManager().isModuleLoaded(moduleName))
			return false;
		Commodore.getUtils().getMessengingUtils().commandOutputErrorWithHighlight("Module named ", moduleName, " is unknown!", executor);
		return true;
	}
	
	public static Set<String> createSet(String... args) {
		Set<String> result = new LinkedHashSet<>();
		for(String arg : args)
			result.add(arg);
		return result;
	}
}
