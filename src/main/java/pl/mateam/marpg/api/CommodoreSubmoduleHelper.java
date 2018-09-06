package pl.mateam.marpg.api;

import pl.mateam.marpg.api.superclasses.CommodoreSubmodule;
import pl.mateam.marpg.api.superclasses.ReloadableCommodoreSubmodule;

public interface CommodoreSubmoduleHelper {
	void addSubmodule(CommodoreSubmodule submodule);
	void addReloadableSubmodule(ReloadableCommodoreSubmodule submodule, String reloadKey);
	void createReloadableGroup(String groupKey, ReloadableCommodoreSubmodule... submodules);
	
	void initialize();
	void reload(String... arguments);
	void shutdown();
}
