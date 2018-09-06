package pl.mateam.marpg.engine.core;

import org.bukkit.plugin.java.JavaPlugin;

import pl.mateam.marpg.api.CommodoreComponent;
import pl.mateam.marpg.api.CommodoreSubmoduleHelper;
import pl.mateam.marpg.api.modules.database.CommodoreDatabase;
import pl.mateam.marpg.api.modules.server.CommodoreServer;
import pl.mateam.marpg.api.superclasses.ReloadableCommodoreSubmodule;
import pl.mateam.marpg.engine.Initializer;
import pl.mateam.marpg.engine.apiimpl.SubmoduleHelperInstance;
import pl.mateam.marpg.engine.core.database.DatabaseImplementation;
import pl.mateam.marpg.engine.core.server.ServerImplementation;

public class Core implements CommodoreComponent {

	/*--------------------------*/
	/*	CommodoreComponent part	*/
	/*--------------------------*/

	@Override
	public JavaPlugin getPlugin() {
		return Initializer.getInstance();
	}
	
	private CommodoreSubmoduleHelper submoduleHelper = new SubmoduleHelperInstance();
	
	@Override
	public void onBeingTurnedOn() {
		submoduleHelper.addSubmodule(database);
		submoduleHelper.addReloadableSubmodule(server, "server");
		submoduleHelper.addReloadableSubmodule((ReloadableCommodoreSubmodule) server.getWorldsManager(), "worlds");
		submoduleHelper.initialize();
	}
	
	@Override
	public void onBeingReloaded(String... arguments) {
		submoduleHelper.reload(arguments);
	}

	@Override
	public void onBeingTurnedOff() {
		submoduleHelper.shutdown();
	}

	/*--------------------------------------*/
	/*	Providing access to core modules	*/
	/* 	(guaranteed by Commodore API)		*/
	/*--------------------------------------*/
	
	private DatabaseImplementation database = new DatabaseImplementation();
	private ServerImplementation server = new ServerImplementation();
	
	public CommodoreDatabase getDatabase() {
		return database;
	}
	
	public CommodoreServer getServer() {
		return server;
	}
}
