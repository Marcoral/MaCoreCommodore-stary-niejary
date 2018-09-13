package pl.mateam.marpg.engine.core.commands;

import org.bukkit.command.CommandSender;

import pl.mateam.marpg.api.superclasses.CommodoreGenericCommand;
import pl.mateam.marpg.engine.Initializer;
import pl.mateam.marpg.engine.core.commands.shared.CoreCommandsUtils;

public class CommodoreCommandReloadCore implements CommodoreGenericCommand {
	@Override
	public void invoked(CommandSender executor, String usedLabel, String... args) {
		String[] newArgs = new String[args.length + 1];
		newArgs[0] = Initializer.getInstance().getName();
		for(int i = 0; i < args.length; i++)
			newArgs[i+1] = args[i];
		CoreCommandsUtils.reloadComponent(executor, newArgs);
	}
}
