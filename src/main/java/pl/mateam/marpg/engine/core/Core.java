package pl.mateam.marpg.engine.core;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.api.CommodoreComponent;
import pl.mateam.marpg.api.CommodoreModuleData;
import pl.mateam.marpg.api.submodules.bans.CommodoreBansManager;
import pl.mateam.marpg.api.submodules.database.CommodoreDatabase;
import pl.mateam.marpg.api.submodules.files.CommodoreConfigurationFile;
import pl.mateam.marpg.api.submodules.loggers.CommodoreLoggersManager;
import pl.mateam.marpg.api.submodules.modulesmanager.CommodoreModulesManager.CommodoreModuleReloadResult;
import pl.mateam.marpg.api.submodules.server.CommodoreServer;
import pl.mateam.marpg.api.submodules.text.CommodoreTextManager;
import pl.mateam.marpg.api.submodules.users.CommodoreUsersManager;
import pl.mateam.marpg.api.submodules.utils.CommodoreUtilsMessenging.Logger;
import pl.mateam.marpg.api.superclasses.LogLevel;
import pl.mateam.marpg.api.superclasses.Priority;
import pl.mateam.marpg.engine.Initializer;
import pl.mateam.marpg.engine.apiimpl.ModuleDataInstance;
import pl.mateam.marpg.engine.core.commands.CommodoreCommandBan;
import pl.mateam.marpg.engine.core.commands.CommodoreCommandDisable;
import pl.mateam.marpg.engine.core.commands.CommodoreCommandEnable;
import pl.mateam.marpg.engine.core.commands.CommodoreCommandRegenerate;
import pl.mateam.marpg.engine.core.commands.CommodoreCommandRegenerateCore;
import pl.mateam.marpg.engine.core.commands.CommodoreCommandReload;
import pl.mateam.marpg.engine.core.commands.CommodoreCommandReloadCore;
import pl.mateam.marpg.engine.core.internal.ConfigPath;
import pl.mateam.marpg.engine.core.internal.CoreUtils;
import pl.mateam.marpg.engine.core.listeners.AsyncPlayerPreLoginEventListener;
import pl.mateam.marpg.engine.core.listeners.PlayerJoinEventListener;
import pl.mateam.marpg.engine.core.listeners.PlayerLoginEventListener;
import pl.mateam.marpg.engine.core.listeners.PlayerQuitEventListener;
import pl.mateam.marpg.engine.core.submodules.bans.BansManagerInstance;
import pl.mateam.marpg.engine.core.submodules.database.DatabaseInstance;
import pl.mateam.marpg.engine.core.submodules.logger.LoggersManagerInstance;
import pl.mateam.marpg.engine.core.submodules.server.ServerInstance;
import pl.mateam.marpg.engine.core.submodules.text.TextManagerInstance;
import pl.mateam.marpg.engine.core.submodules.users.UsersManagerInstance;

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
		setupTables();
		registerCommands();
		registerListeners();
	}
	
	private void registerSubmodules() {
		//Dependency for another submodules - should be loaded first.
		moduleData.addReloadableSubmodule(loggers, "logger");
		
		moduleData.addReloadableSubmodule(bans, "bans");
		moduleData.addSubmodule(database);
		moduleData.addReloadableSubmodule(server.getWorldsManager(), "worlds");
		moduleData.addReloadableSubmodule(text, "text");
	}
	
	private void defineReloadableGroups() {
		moduleData.createReloadableGroup("server", server.getWorldsManager());
	}
	
	private void setupTables() {
		CommodoreConfigurationFile config = CoreUtils.getConfigurationFile(ConfigPath.SETUP_DATABASE);
		if(!config.exists()) {
			Commodore.getUtils().getMessengingUtils().logError("Database regeneration script is missing! "
					+ "MaCoreCommodore won't be able to regenerate tables in case that they were missing!", Logger.LOGGER_SETUP, LogLevel.WARNING);
			return;
		}
		
		ConfigurationSection configData = config.getData();
		for(String tableName : configData.getKeys(false)) {
			String creationQuery = configData.getString(tableName);
			moduleData.requestDatabaseTable(tableName, creationQuery);
		}
	}
	
	private void registerCommands() {
		moduleData.registerGenericCommand(CommodoreCommandReload::new, Priority.HIGHEST, "creload", "crel");
		moduleData.registerGenericCommand(CommodoreCommandReloadCore::new, Priority.HIGHEST, "creloadcore", "crelcore");
		moduleData.registerGenericCommand(CommodoreCommandEnable::new, Priority.HIGHEST, "cenable");
		moduleData.registerGenericCommand(CommodoreCommandDisable::new, Priority.HIGHEST, "cdisable");
		moduleData.registerGenericCommand(CommodoreCommandRegenerate::new, Priority.HIGHEST, "cregenerate", "cregen");
		moduleData.registerGenericCommand(CommodoreCommandRegenerateCore::new, Priority.HIGHEST, "cregeneratecore", "cregencore");
		moduleData.registerGenericCommand(CommodoreCommandBan::new, Priority.HIGHEST, "cban");
	}
	
	private void registerListeners() {
		PluginManager manager = Bukkit.getPluginManager();
		manager.registerEvents(new AsyncPlayerPreLoginEventListener(), Initializer.getInstance());
		manager.registerEvents(new PlayerJoinEventListener(), Initializer.getInstance());
		manager.registerEvents(new PlayerLoginEventListener(), Initializer.getInstance());
		manager.registerEvents(new PlayerQuitEventListener(), Initializer.getInstance());
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
	
	private BansManagerInstance bans = new BansManagerInstance();
	private DatabaseInstance database = new DatabaseInstance();
	private LoggersManagerInstance loggers = new LoggersManagerInstance();
	private ServerInstance server = new ServerInstance();
	private TextManagerInstance text = new TextManagerInstance();
	private UsersManagerInstance users = new UsersManagerInstance();
	
	public CommodoreBansManager getBansManager() {
		return bans;
	}
	
	public CommodoreDatabase getDatabase() {
		return database;
	}
	
	public CommodoreLoggersManager getLoggersManager() {
		return loggers;
	}
	
	public CommodoreServer getServer() {
		return server;
	}
	
	public CommodoreTextManager getTextManager() {
		return text;
	}
	
	public CommodoreUsersManager getUsersManager() {
		return users;
	}
}
