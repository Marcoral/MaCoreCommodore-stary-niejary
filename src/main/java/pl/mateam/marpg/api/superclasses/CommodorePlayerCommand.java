package pl.mateam.marpg.api.superclasses;

import org.bukkit.entity.Player;

import pl.mateam.marpg.api.CommodoreCommand;

public interface CommodorePlayerCommand extends CommodoreCommand {
	//TODO: Replace Player with valid Commodore Player-CommandExecutor type
	void invoked(Player executor, String usedLabel, String... args);
}
