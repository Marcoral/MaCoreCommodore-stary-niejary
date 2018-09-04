package pl.mateam.marpg.api;

import org.bukkit.plugin.java.JavaPlugin;

//Core of the engine implements CommodoreComponent, but is NOT CommodoreModule.
//Just don't use this interface, everything you need to know is to extends CommodoreModule class.
public interface CommodoreComponent {
	JavaPlugin getPlugin();
	void onBeingTurnedOn();
	void onBeingReloaded(String parameter);
	void onBeingTurnedOff();
}
