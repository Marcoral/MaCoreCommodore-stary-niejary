package pl.mateam.marpg.engine.core;

import org.bukkit.plugin.java.JavaPlugin;

import pl.mateam.marpg.api.CommodoreComponent;
import pl.mateam.marpg.api.CommodoreModuleData;
import pl.mateam.marpg.api.modules.database.CommodoreDatabase;
import pl.mateam.marpg.api.modules.loggers.CommodoreLoggersManager;
import pl.mateam.marpg.api.modules.modulesmanager.CommodoreModulesManager.CommodoreModuleReloadResult;
import pl.mateam.marpg.api.modules.server.CommodoreServer;
import pl.mateam.marpg.api.superclasses.Priority;
import pl.mateam.marpg.engine.Initializer;
import pl.mateam.marpg.engine.apiimpl.ModuleDataInstance;
import pl.mateam.marpg.engine.core.commands.CommodoreCommandDisable;
import pl.mateam.marpg.engine.core.commands.CommodoreCommandEnable;
import pl.mateam.marpg.engine.core.commands.CommodoreCommandRegenerate;
import pl.mateam.marpg.engine.core.commands.CommodoreCommandRegenerateCore;
import pl.mateam.marpg.engine.core.commands.CommodoreCommandReload;
import pl.mateam.marpg.engine.core.commands.CommodoreCommandReloadCore;
import pl.mateam.marpg.engine.core.submodules.database.DatabaseImplementation;
import pl.mateam.marpg.engine.core.submodules.logger.LoggersManagerInstance;
import pl.mateam.marpg.engine.core.submodules.server.ServerImplementation;

public class Core implements CommodoreComponent {

	/*--------------------------*/
	/*	CommodoreComponent part	*/
	/*--------------------------*/

	@Override
	public JavaPlugin getPlugin() {
		return Initializer.getInstance();
	}
	
	private CommodoreModuleData moduleData = new ModuleDataInstance(getPlugin().getName());
	
	@Override
	public void onBeingTurnedOn() {
		registerSubmodules();
		defineReloadableGroups();
		moduleData.initialize();
		registerCommands();
	}
	
	private void registerSubmodules() {
		moduleData.addReloadableSubmodule(loggers, "logger");
		moduleData.addSubmodule(database);
		moduleData.addReloadableSubmodule(server.getWorldsManager(), "worlds");
	}
	
	private void defineReloadableGroups() {
		moduleData.createReloadableGroup("server", server.getWorldsManager());
	}
	
	private void registerCommands() {
		moduleData.registerGenericCommand(CommodoreCommandReload::new, Priority.HIGHEST, "creload", "crel");
		moduleData.registerGenericCommand(CommodoreCommandReloadCore::new, Priority.HIGHEST, "creloadcore", "crelcore");
		moduleData.registerGenericCommand(CommodoreCommandEnable::new, Priority.HIGHEST, "cenable");
		moduleData.registerGenericCommand(CommodoreCommandDisable::new, Priority.HIGHEST, "cdisable");
		moduleData.registerGenericCommand(CommodoreCommandRegenerate::new, Priority.HIGHEST, "cregenerate", "cregen");
		moduleData.registerGenericCommand(CommodoreCommandRegenerateCore::new, Priority.HIGHEST, "cregeneratecore", "cregencore");
	}
	
	@Override
	public CommodoreModuleReloadResult onBeingReloaded(String... arguments) {
		return moduleData.reload(arguments);
	}

	@Override
	public void onBeingTurnedOff() {
		moduleData.shutdown();
	}

	/*--------------------------------------*/
	/*	Providing access to core modules	*/
	/* 	(guaranteed by Commodore API)		*/
	/*--------------------------------------*/
	
	private DatabaseImplementation database = new DatabaseImplementation();
	private LoggersManagerInstance loggers = new LoggersManagerInstance();
	private ServerImplementation server = new ServerImplementation();
	
	public CommodoreDatabase getDatabase() {
		return database;
	}
	
	public CommodoreLoggersManager getLoggersManager() {
		return loggers;
	}
	
	public CommodoreServer getServer() {
		return server;
	}
}
