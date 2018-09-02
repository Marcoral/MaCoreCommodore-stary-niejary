package pl.mateam.marpg.engine;

import org.bukkit.plugin.java.JavaPlugin;

import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.api.Commodore_Intermediate;
import pl.mateam.marpg.api.modules.utils.CommodoreDevelopmentUtils;
import pl.mateam.marpg.engine.apiimpl.CommodoreImplementation;

public class Initializer extends JavaPlugin {
	private static Initializer singleton;
	public static Initializer getInstance() {
		return singleton;
	}
	
	public void onEnable() {
		singleton = this;
		initializeAPI();
		initializeCore();
	}
	
	private void initializeAPI() {
		Commodore_Intermediate commodore = new CommodoreImplementation();
		CommodoreDevelopmentUtils devUtils = commodore.getUtils().getDevelopmentUtils();
		devUtils.injectExternalField(Commodore.class, null, "instance", commodore);
	}
	
	private void initializeCore() {
		Commodore.getComponentsManager().enableComponent(new Core());
	}
}
