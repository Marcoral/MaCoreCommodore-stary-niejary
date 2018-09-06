package pl.mateam.marpg.engine;

import org.bukkit.plugin.java.JavaPlugin;

import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.api.modules.utils.CommodoreDevelopmentUtils;
import pl.mateam.marpg.engine.apiimpl.CommodoreInstance;
import pl.mateam.marpg.engine.core.Core;

public class Initializer extends JavaPlugin {
	private static Initializer singleton;
	public static Initializer getInstance() {
		return singleton;
	}
	
	public void onEnable() {
		singleton = this;
		
		CommodoreInstance commodoreBase = new CommodoreInstance();
		initializeAPI(commodoreBase);
		initializeCore(commodoreBase);
	}
	
	public void onDisable() {
		Commodore.getComponentsManager().disableComponent(this.getName());	//Send shutdown signal to Core
	}
	
	private void initializeAPI(CommodoreInstance commodoreBase) {
		CommodoreDevelopmentUtils devUtils = commodoreBase.getUtils().getDevelopmentUtils();
		devUtils.injectExternalField(Commodore.class, null, "instance", commodoreBase, true);
		//Now use of core-independent methods like Commodore.getComponentsManager() is save.
	}
	
	private void initializeCore(CommodoreInstance commodoreBase) {
		Core core = new Core();
		Commodore.getComponentsManager().enableComponent(core);
		Commodore.getUtils().getDevelopmentUtils().injectExternalField(CommodoreInstance.class, commodoreBase, "core", core, true);
		//Now use of core-dependent methods like Commodore.getDatabase() is save.
	}
}
