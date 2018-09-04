package pl.mateam.marpg.engine.core;

import org.bukkit.plugin.java.JavaPlugin;

import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.api.CommodoreComponent;
import pl.mateam.marpg.api.modules.database.CommodoreDatabase;
import pl.mateam.marpg.engine.Initializer;
import pl.mateam.marpg.engine.core.database.DatabaseImplementation;

public class Core implements CommodoreComponent {

	/*--------------------------*/
	/*	CommodoreComponent part	*/
	/*--------------------------*/

	@Override
	public JavaPlugin getPlugin() {
		return Initializer.getInstance();
	}
	
	@Override
	public void onBeingTurnedOn() {
		//TODO: try-catch blocks
		database.setup(Commodore.getFilesManager().getConfig(this, ConfigPath.GENERAL.getPath()));
	}

	@Override
	public void onBeingReloaded(String parameter) {}

	@Override
	public void onBeingTurnedOff() {
		database.shutdown();
	}

	/*--------------------------------------*/
	/*	Providing access to core modules	*/
	/* 	(guaranteed by Commodore API)		*/
	/*--------------------------------------*/
	
	private DatabaseImplementation database = new DatabaseImplementation();
	
	public CommodoreDatabase getDatabase() {
		return database;
	}
}
