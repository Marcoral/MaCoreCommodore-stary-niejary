package pl.mateam.marpg.engine;

import org.bukkit.plugin.java.JavaPlugin;

import pl.mateam.marpg.api.CommodoreComponent;

public class Core implements CommodoreComponent {
	@Override
	public JavaPlugin getPlugin() {
		return Initializer.getInstance();
	}
	
	@Override
	public void onBeingTurnedOn() {
		
	}

	@Override
	public boolean onBeingReloaded(String parameter) {
		return true;
	}

	@Override
	public void onBeingTurnedOff() {}
}
