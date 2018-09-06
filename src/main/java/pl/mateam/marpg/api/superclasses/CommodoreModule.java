package pl.mateam.marpg.api.superclasses;

import org.bukkit.plugin.java.JavaPlugin;

import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.api.CommodoreComponent;
import pl.mateam.marpg.api.CommodoreSubmoduleHelper;
import pl.mateam.marpg.api.modules.utils.CommodoreDevelopmentUtils.Extern;

public abstract class CommodoreModule extends JavaPlugin implements CommodoreComponent {
	@Extern(key = "submodule helper")
	private CommodoreSubmoduleHelper submoduleHelper;

	protected final void addSubmodule(CommodoreSubmodule submodule) {
		submoduleHelper.addSubmodule(submodule);
	}
	
	protected final void addReloadableSubmodule(ReloadableCommodoreSubmodule submodule, String reloadKey) {
		submoduleHelper.addReloadableSubmodule(submodule, reloadKey);
	}
	
	protected final void createReloadableGroup(String groupKey, ReloadableCommodoreSubmodule... submodules) {
		submoduleHelper.createReloadableGroup(groupKey, submodules);
	}
	
	@Override
	public final JavaPlugin getPlugin() {
		return this;
	}

	@Override
	public final void onBeingReloaded(String... arguments) {
		submoduleHelper.reload(arguments);
		afterReload();
	}
	
	@Override
	public final void onEnable() {
		Commodore.getComponentsManager().enableComponent(this);
		submoduleHelper.initialize();
	}
	
	@Override
	public final void onDisable() {
		Commodore.getComponentsManager().disableComponent(this);
		submoduleHelper.shutdown();
	}

	protected void afterReload() {}	//Hook
	
	@Override
	public void onBeingTurnedOff() {}	//Just hook, don't have to be implemented in opposite to onBeingTurnedOn
}
