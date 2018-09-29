package pl.mateam.marpg.engine.core.submodules.text;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

import net.md_5.bungee.api.ChatColor;
import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.api.submodules.files.CommodoreConfigurationFile;
import pl.mateam.marpg.api.submodules.text.CommodoreTextManager;
import pl.mateam.marpg.api.submodules.text.TextNode;
import pl.mateam.marpg.api.submodules.utils.CommodoreUtilsMessenging;
import pl.mateam.marpg.api.submodules.utils.CommodoreUtilsMessenging.Logger;
import pl.mateam.marpg.api.superclasses.LogLevel;
import pl.mateam.marpg.api.superclasses.CommodoreReloadableSubmodule;
import pl.mateam.marpg.engine.core.internal.ConfigPath;
import pl.mateam.marpg.engine.core.internal.CoreUtils;
import pl.mateam.marpg.engine.core.submodules.CommodoreCoreSetupException;

public class TextManagerInstance implements CommodoreReloadableSubmodule, CommodoreTextManager {
	private Map<TextNode, String> textByEnum = new EnumMap<>(TextNode.class);
	private Map<String, String> textByString = new HashMap<>();
	
	@Override
	public void setup() {
		reload();
	}

	@Override
	public void reload() {
		CommodoreUtilsMessenging messenger = Commodore.getUtils().getMessengingUtils();
		CommodoreConfigurationFile config = CoreUtils.getConfigurationFile(ConfigPath.TEXT);
		if(!config.exists())
			throw new CommodoreCoreSetupException("Text config is missing!");
		
		ConfigurationSection configData = config.getData();
		for(TextNode node : TextNode.values()) {
			String value = configData.getString(node.getPath(), null);
			if(value == null)
				throw new CommodoreCoreSetupException("No defined value for build-in text node \"" + node + "\"!");
			textByEnum.put(node, ChatColor.translateAlternateColorCodes('&', value));
		}
		
		messenger.craftLogger(Logger.LOGGER_SETUP, LogLevel.FULL)
		.colorSuccessHighlighted("Text nodes have been set.").send();
	}

	@Override
	public String getNode(TextNode node) {
		return textByEnum.get(node);
	}

	@Override
	public String getNode(String node) {
		return textByString.get(node);
	}

	@Override
	public void registerNode(String nodeId, String text) throws NodeAlreadyExistsException {
		if(textByString.containsKey(nodeId))
			throw new NodeAlreadyExistsException(nodeId);
		textByString.put(nodeId, ChatColor.translateAlternateColorCodes('&', text));
	}
}
