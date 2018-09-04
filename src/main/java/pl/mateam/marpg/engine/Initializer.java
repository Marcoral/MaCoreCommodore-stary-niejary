package pl.mateam.marpg.engine;

import org.bukkit.plugin.java.JavaPlugin;

import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.api.modules.utils.CommodoreDevelopmentUtils;
import pl.mateam.marpg.engine.apiimpl.CommodoreImplementation;
import pl.mateam.marpg.engine.core.Core;

public class Initializer extends JavaPlugin {
	private static Initializer singleton;
	public static Initializer getInstance() {
		return singleton;
	}
	
	public void onEnable() {
		singleton = this;
		
		CommodoreImplementation commodoreBase = new CommodoreImplementation();
		initializeAPI(commodoreBase);
		initializeCore(commodoreBase);
	}
	
	public void onDisable() {
		Commodore.getComponentsManager().disableComponent(this.getName());	//Send shutdown signal to Core
	}
	
	private void initializeAPI(CommodoreImplementation commodoreBase) {
		CommodoreDevelopmentUtils devUtils = commodoreBase.getUtils().getDevelopmentUtils();
		devUtils.injectExternalField(Commodore.class, null, "instance", commodoreBase);
		//Now use of core-independent methods like Commodore.getComponentsManager() is save.
	}
	
	private void initializeCore(CommodoreImplementation commodoreBase) {
		Core core = new Core();
		Commodore.getComponentsManager().enableComponent(core);
		Commodore.getUtils().getDevelopmentUtils().injectExternalField(CommodoreImplementation.class, commodoreBase, "core", core);
		//Now use of core-dependent methods like Commodore.getDatabase() is save.
	}
}
