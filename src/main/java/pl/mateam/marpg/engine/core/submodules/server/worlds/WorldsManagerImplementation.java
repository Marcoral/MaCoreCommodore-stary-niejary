package pl.mateam.marpg.engine.core.submodules.server.worlds;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.api.modules.files.CommodoreConfigurationFile;
import pl.mateam.marpg.api.modules.server.worlds.CommodoreWorld;
import pl.mateam.marpg.api.modules.server.worlds.CommodoreWorldsManager;
import pl.mateam.marpg.api.modules.utils.CommodoreMessengingUtils.Logger;
import pl.mateam.marpg.api.superclasses.LogLevel;
import pl.mateam.marpg.api.superclasses.ReloadableCommodoreSubmodule;
import pl.mateam.marpg.engine.core.submodules.CommodoreCoreSetupException;
import pl.mateam.marpg.engine.core.submodules.ConfigPath;
import pl.mateam.marpg.engine.core.submodules.CoreLoadingUtils;

public class WorldsManagerImplementation implements CommodoreWorldsManager, ReloadableCommodoreSubmodule {
	/* ------------------- */
	/* Core submodule part */
	/* ------------------- */
	
	@Override
	public void setup() {
		reload();
	}
	
	@Override
	public void reload() {
		CommodoreConfigurationFile config = CoreLoadingUtils.getConfigurationFile(ConfigPath.ENVIRONMENT);
		if(!config.exists())
			throw new CommodoreCoreSetupException("Worlds setup config is missing!");
		
		Map<String, CommodoreWorld> knownWorlds = new HashMap<>();
		YamlConfiguration configData = config.getData();
		ConfigurationSection defaulsSection = configData.getConfigurationSection("Worlds.Defaults");
		for(CommodoreWorld enumWorld : CommodoreWorld.values()) {
			Commodore.getUtils().getMessengingUtils().logCasualWithHighlight("Preparing world \"", enumWorld.getDirectoryName(), "\"...", Logger.LOGGER_SETUP, LogLevel.DEBUG);
			ConfigurationSection worldSection = configData.getConfigurationSection("Worlds.Specified." + enumWorld.getDirectoryName());
			if(worldSection == null)
				throw new CommodoreCoreSetupException("Configuration path not specified for world \"" + enumWorld.getDirectoryName() + "\"!");
			String worldName = worldSection.getString("Name");
			if(worldName == null)
				throw new CommodoreCoreSetupException("World \"" + enumWorld.getDirectoryName() + "\": directory name not found!");
			CommodoreWorld previousWorldWithSameDirectory = knownWorlds.get(worldName);
			if(previousWorldWithSameDirectory != null)
				throw new CommodoreCoreSetupException("World \"" + enumWorld.getDirectoryName() + "\": Minecraft world name is not unique! "
						+ "It is already defined for world \"" + previousWorldWithSameDirectory.getDirectoryName() + "\".");
			World bukkitWorld = Bukkit.getServer().getWorld(worldName);
			if(bukkitWorld == null)
				throw new CommodoreCoreSetupException("World \"" + enumWorld.getDirectoryName() + "\": given Minecraft world name is invalid! "
						+ "There is no world with such name: \"" + worldName + "\"");
			knownWorlds.put(worldName, enumWorld);
			configure(bukkitWorld, defaulsSection);	//Performing default operations
			configure(bukkitWorld, worldSection);	//And then world-specified
			Commodore.getUtils().getMessengingUtils().logSuccessWithHighlight("", "Worlds are recognized and prepared to play", "", Logger.LOGGER_SETUP, LogLevel.FULL);
		}
	}
	
	private void configure(World world, ConfigurationSection configurationPath) {
		ConfigurationSection gamerulesPath = configurationPath.getConfigurationSection("Gamerules");
		if(gamerulesPath != null) {
			Map<String, Object> gamerules = gamerulesPath.getValues(false);
			if(gamerules != null)
				gamerules.forEach((key, value) -> {
					Commodore.getUtils().getMessengingUtils().craftLogger(Logger.LOGGER_SETUP, LogLevel.DEBUG)
						.colorCasual("World \"")
						.colorCasualHighlighted(world.getName())
						.colorCasual("\": gamerule \"")
						.colorCasualHighlighted(key)
						.colorCasual("\" set to \"")
						.colorCasualHighlighted(value.toString())
						.colorCasual("\".").send();
					world.setGameRuleValue(key, value.toString());
				});
		}
	}
}
