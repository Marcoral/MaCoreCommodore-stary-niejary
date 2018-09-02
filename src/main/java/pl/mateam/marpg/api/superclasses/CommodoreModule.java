package pl.mateam.marpg.api.superclasses;

import org.bukkit.plugin.java.JavaPlugin;

import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.api.CommodoreComponent;

public abstract class CommodoreModule extends JavaPlugin implements CommodoreComponent {
	@Override
	public final JavaPlugin getPlugin() {
		return this;
	}
	
	@Override
	public final void onEnable() {
		Commodore.getComponentsManager().enableComponent(this);
	}
	
	@Override
	public final void onDisable() {
		Commodore.getComponentsManager().disableComponent(this);
	}

	@Override
	public void onBeingTurnedOff() {}	//Hook
}
