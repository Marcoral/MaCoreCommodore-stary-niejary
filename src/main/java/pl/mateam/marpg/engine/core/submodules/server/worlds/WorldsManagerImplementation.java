package pl.mateam.marpg.engine.core.submodules.server.worlds;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.api.submodules.files.CommodoreConfigurationFile;
import pl.mateam.marpg.api.submodules.server.worlds.CommodoreWorld;
import pl.mateam.marpg.api.submodules.server.worlds.CommodoreWorldsManager;
import pl.mateam.marpg.api.submodules.utils.CommodoreUtilsMessenging.Logger;
import pl.mateam.marpg.api.superclasses.LogLevel;
import pl.mateam.marpg.api.superclasses.CommodoreReloadableSubmodule;
import pl.mateam.marpg.engine.core.internal.ConfigPath;
import pl.mateam.marpg.engine.core.internal.CoreUtils;
import pl.mateam.marpg.engine.core.submodules.CommodoreCoreSetupException;

public class WorldsManagerImplementation implements CommodoreWorldsManager, CommodoreReloadableSubmodule {
	/* ------------------- */
	/* Core submodule part */
	/* ------------------- */
	
	@Override
	public void setup() {
		reload();
	}
	
	@Override
	public void reload() {
		CommodoreConfigurationFile config = CoreUtils.getConfigurationFile(ConfigPath.ENVIRONMENT);
		if(!config.exists())
			throw new CommodoreCoreSetupException("Worlds setup config is missing!");
		
		Map<String, CommodoreWorld> knownWorlds = new HashMap<>();
		YamlConfiguration configData = config.getData();
		ConfigurationSection defaulsSection = configData.getConfigurationSection("Worlds.Defaults");
		for(CommodoreWorld enumWorld : CommodoreWorld.values()) {
			Commodore.getUtils().getMessengingUtils().logCasualWithHighlight("Preparing world \"", enumWorld.getConfigurationPath(), "\"...", Logger.LOGGER_SETUP, LogLevel.DEBUG);
			ConfigurationSection worldSection = configData.getConfigurationSection("Worlds.Specified." + enumWorld.getConfigurationPath());
			if(worldSection == null)
				throw new CommodoreCoreSetupException("Configuration path not specified for world \"" + enumWorld.getConfigurationPath() + "\"!");
			String worldName = worldSection.getString("Name");
			if(worldName == null)
				throw new CommodoreCoreSetupException("World \"" + enumWorld.getConfigurationPath() + "\": directory name not found!");
			CommodoreWorld previousWorldWithSameDirectory = knownWorlds.get(worldName);
			if(previousWorldWithSameDirectory != null)
				throw new CommodoreCoreSetupException("World \"" + enumWorld.getConfigurationPath() + "\": Minecraft world name is not unique! "
						+ "It is already defined for world \"" + previousWorldWithSameDirectory.getConfigurationPath() + "\".");
			World bukkitWorld = Bukkit.getServer().getWorld(worldName);
			if(bukkitWorld == null)
				throw new CommodoreCoreSetupException("World \"" + enumWorld.getConfigurationPath() + "\": given Minecraft world name is invalid! "
						+ "There is no world with such name: \"" + worldName + "\"");
			knownWorlds.put(worldName, enumWorld);
			configure(bukkitWorld, defaulsSection);	//Performing default operations
			configure(bukkitWorld, worldSection);	//And then world-specified
			Commodore.getUtils().getMessengingUtils().logSuccessWithHighlight("", "Worlds are recognized and prepared to play", "", Logger.LOGGER_SETUP, LogLevel.FULL);
		}
	}
	
	@SuppressWarnings("deprecation")
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
