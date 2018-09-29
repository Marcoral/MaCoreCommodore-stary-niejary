package pl.mateam.marpg.engine.core.submodules.bans;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.api.objects.users.children.bans.CommodoreBanObject.BanScope;
import pl.mateam.marpg.api.submodules.bans.CommodoreBansManager;
import pl.mateam.marpg.api.submodules.files.CommodoreConfigurationFile;
import pl.mateam.marpg.api.submodules.utils.CommodoreUtilsMessenging;
import pl.mateam.marpg.api.submodules.utils.CommodoreUtilsMessenging.Logger;
import pl.mateam.marpg.api.superclasses.CommodoreReloadableSubmodule;
import pl.mateam.marpg.api.superclasses.LogLevel;
import pl.mateam.marpg.engine.core.internal.ConfigPath;
import pl.mateam.marpg.engine.core.internal.CoreUtils;
import pl.mateam.marpg.engine.core.submodules.CommodoreCoreSetupException;

public class BansManagerInstance implements CommodoreBansManager, CommodoreReloadableSubmodule {
	
	private Map<BanScope, BanTemplateInfo> infos = new EnumMap<>(BanScope.class);
	
	@Override
	public void setup() {
		reload();
	}

	@Override
	public void reload() {
		CommodoreConfigurationFile config = CoreUtils.getConfigurationFile(ConfigPath.BANS);
		if(!config.exists())
			throw new CommodoreCoreSetupException("Bans reasons setup config is missing!");
		
		CommodoreUtilsMessenging messenger = Commodore.getUtils().getMessengingUtils();
		YamlConfiguration configData = config.getData();
		for(BanScope scope : BanScope.values()) {
			ConfigurationSection scopeSection = configData.getConfigurationSection(scope.getConfigurationPath());
			if(scopeSection == null) { 
				messenger.logErrorWithHighlight("Configuration path not specified for ban scope \"", scope.getConfigurationPath(), "\"!", Logger.LOGGER_SETUP, LogLevel.WARNING);
				continue;
			}
			BanTemplateInfo info = new BanTemplateInfo();
			infos.put(scope, info);
			scopeSection.getValues(false).forEach((key, value) -> {
				try {
					Integer intKey = Integer.parseInt(key);
					info.perNumberReasons.put(intKey, (String) value);
				} catch(NumberFormatException e) {
					messenger.craftLogger(Logger.LOGGER_SETUP, LogLevel.ERROR)
					.colorError("Found invalid key while traversing ban scope \"")
					.colorErrorHighlighted(scope.getConfigurationPath())
					.colorError("\": \"")
					.colorErrorHighlighted(key)
					.colorError("\"! Skipping this key.").send();
				}
			});
		}
		Commodore.getUtils().getMessengingUtils().craftLogger(Logger.LOGGER_SETUP, LogLevel.FULL).colorSuccessHighlighted("Successfully set up ban reasons.").send();
	}

	@Override
	public String getReasonFor(BanScope scope, int option) {
		return infos.get(scope).perNumberReasons.get(option);
	}

	private static class BanTemplateInfo {
		private Map<Integer, String> perNumberReasons = new HashMap<>();
	}
}
