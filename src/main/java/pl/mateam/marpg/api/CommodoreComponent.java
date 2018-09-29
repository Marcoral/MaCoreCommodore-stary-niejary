package pl.mateam.marpg.api;

import org.bukkit.plugin.java.JavaPlugin;

import pl.mateam.marpg.api.submodules.modulesmanager.CommodoreModulesManager.CommodoreModuleReloadResult;

//Core of the engine implements CommodoreComponent, but is NOT CommodoreModule.
//Just don't use this interface, everything you need to know is to extends CommodoreModule class.
public interface CommodoreComponent {
	JavaPlugin getPlugin();
	void onBeingTurnedOn();
	void onBeingTurnedOff();
	CommodoreModuleReloadResult onBeingReloaded(String... arguments);
}
