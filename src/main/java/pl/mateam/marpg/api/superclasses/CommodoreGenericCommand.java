package pl.mateam.marpg.api.superclasses;

import org.bukkit.command.CommandSender;

import pl.mateam.marpg.api.CommodoreCommand;

public interface CommodoreGenericCommand extends CommodoreCommand {
	void invoked(CommandSender executor, String usedLabel, String... args);
}
