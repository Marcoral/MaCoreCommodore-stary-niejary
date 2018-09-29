package pl.mateam.marpg.engine.apiimpl;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_13_R2.CraftServer;
import org.bukkit.entity.Player;

import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.api.CommodoreModuleData;
import pl.mateam.marpg.api.submodules.files.CommodoreConfigurationFile;
import pl.mateam.marpg.api.submodules.modulesmanager.CommodoreModulesManager.CommodoreModuleReloadResult;
import pl.mateam.marpg.api.submodules.text.CommodoreTextManager.NodeAlreadyExistsException;
import pl.mateam.marpg.api.submodules.utils.CommodoreUtilsMessenging;
import pl.mateam.marpg.api.submodules.utils.CommodoreUtilsMessenging.Logger;
import pl.mateam.marpg.api.superclasses.CommodoreGenericCommand;
import pl.mateam.marpg.api.superclasses.CommodorePlayerCommand;
import pl.mateam.marpg.api.superclasses.CommodoreSubmodule;
import pl.mateam.marpg.api.superclasses.LogLevel;
import pl.mateam.marpg.api.superclasses.Priority;
import pl.mateam.marpg.api.superclasses.CommodoreReloadableSubmodule;
import pl.mateam.marpg.engine.ControlPanel;
import pl.mateam.marpg.engine.core.submodules.CommodoreCoreSetupException;

public class ModuleDataInstance implements CommodoreModuleData {
	
	private final String moduleName;
	public ModuleDataInstance(String moduleName) {
		this.moduleName = moduleName;
	}
	
	/* --------------------- *
	 * Submodules management *
	 * -------------------- */
	
	private List<CommodoreSubmodule> setupOrder = new ArrayList<>();
	private Map<String, CommodoreReloadableSubmodule> reloadableSubmodules = new HashMap<>();
	private Map<String, CommodoreReloadableSubmodule[]> reloadableGroups = new HashMap<>();

	@Override
	public void addSubmodule(CommodoreSubmodule submodule) {
		setupOrder.add(submodule);
	}

	@Override
	public void addReloadableSubmodule(CommodoreReloadableSubmodule submodule, String reloadKey) {
		setupOrder.add(submodule);
		reloadableSubmodules.put(reloadKey, submodule);
	}

	@Override
	public void createReloadableGroup(String groupKey, CommodoreReloadableSubmodule... submodules) {
		reloadableGroups.put(groupKey, submodules);
	}

	/* ------------------- *
	 * Commands management *
	 * ------------------- */
	
	private static Map<String, List<CommodoreCommandMetainfo>> commandsQueue = new HashMap<>();
	private static Map<String, CommodoreCommandMetainfo> activeCommands = new HashMap<>();
	private List<String> registeredCommands = new ArrayList<>();
		
	@Override
	public void registerGenericCommand(Supplier<? extends CommodoreGenericCommand> command, String name, String... aliases) {
		registerGenericCommand(command, Priority.NORMAL, name, aliases);
	}

	@Override
	public void registerPlayerCommand(Supplier<? extends CommodorePlayerCommand> command, String name, String... aliases) {
		registerPlayerCommand(command, Priority.NORMAL, name, aliases);
	}

	@Override
	public void registerGenericCommand(Supplier<? extends CommodoreGenericCommand> command, Priority aliasesPriority, String name, String... aliases) {
		registerCommands(aliasesPriority, name, aliases, alias -> {
			return new BukkitCommand(alias) {
				@Override
				public boolean execute(CommandSender executor, String label, String[] args) {
					command.get().invoked(executor, label, args);
					return false;
				}
			};
		});
	}
	
	@Override
	public void registerPlayerCommand(Supplier<? extends CommodorePlayerCommand> command, Priority aliasesPriority, String name, String... aliases) {
		registerCommands(aliasesPriority, name, aliases, alias -> {
			return new BukkitCommand(alias) {
				@Override
				public boolean execute(CommandSender executor, String label, String[] args) {
					if(executor instanceof Player)
						command.get().invoked((Player) executor, label, args);
					else
						Commodore.getUtils().getMessengingUtils().commandOutputError("This command can not be used from the console!", Bukkit.getConsoleSender());
					return true;
				}
			};
		});
	}
	
	@SuppressWarnings("unchecked")
	private void registerCommands(Priority aliasesPriority, String name, String[] aliases, Function<String, BukkitCommand> commandHandler) {
		Map<String, Command> registeredCommands = null;
		SimpleCommandMap commandMap = ((CraftServer) Bukkit.getServer()).getCommandMap();
		String[] names = new String[aliases.length+1];
		names[0] = name;
		for(int i = 0; i < aliases.length; ++i)
			names[i+1] = aliases[i];
		for(String alias : names) {
			this.registeredCommands.add(alias);
			CommodoreCommandMetainfo newCommandInfo = new CommodoreCommandMetainfo(aliasesPriority, this, commandHandler);
			CommodoreCommandMetainfo oldCommandInfo = activeCommands.get(alias);
			if(oldCommandInfo != null) {
				Priority priorityOfOldCommand = oldCommandInfo.priority;
				if(aliasesPriority.getLevel() <= priorityOfOldCommand.getLevel()) {
					Commodore.getUtils().getMessengingUtils().craftLogger(Logger.LOGGER_SETUP, LogLevel.WARNING)
					.colorError("Command ")
					.colorErrorHighlighted(alias)
					.colorError(" with priority ")
					.colorErrorHighlighted(aliasesPriority.toString())
					.colorError(" was not registered, as there already exists such command with priority ")
					.colorErrorHighlighted(priorityOfOldCommand.toString())
					.colorError(" (registerer: ")
					.colorErrorHighlighted(oldCommandInfo.registerer.moduleName)
					.colorError(")").send();
					addCommandInfoToQueue(alias, newCommandInfo);
					continue;
				} else {
					if(registeredCommands == null)
						registeredCommands = (Map<String, Command>) Commodore.getUtils().getReflectionUtils().getFieldValue(commandMap, "knownCommands");
					registeredCommands.remove(alias);
					Commodore.getUtils().getMessengingUtils().craftLogger(Logger.LOGGER_SETUP, LogLevel.FULL)
					.colorCasual("Command ")
					.colorCasualHighlighted(alias)
					.colorCasual(" with priority ")
					.colorCasualHighlighted(aliasesPriority.toString())
					.colorCasual(" just overwritted another command with such name with priority ")
					.colorCasualHighlighted(priorityOfOldCommand.toString())
					.colorCasual(". Registerer of the latter: ")
					.colorCasualHighlighted(oldCommandInfo.registerer.moduleName)
					.colorCasual(".").send();
					addCommandInfoToQueue(alias, oldCommandInfo);
				}
			}
			activeCommands.put(alias, newCommandInfo);
			commandMap.register(alias, commandHandler.apply(alias));
			Commodore.getUtils().getMessengingUtils().craftLogger(Logger.LOGGER_SETUP, LogLevel.FULL)
			.colorSuccess("Command ")
			.colorSuccessHighlighted(alias)
			.colorSuccess(" with priority ")
			.colorSuccessHighlighted(aliasesPriority.toString())
			.colorSuccess(" have been successfully registered.").send();
		}
	}
	
	private void addCommandInfoToQueue(String commandName, CommodoreCommandMetainfo info) {
		List<CommodoreCommandMetainfo> queue = commandsQueue.get(commandName);
		if(queue == null) {
			queue = new ArrayList<>();
			commandsQueue.put(commandName, queue);
		}
		queue.add(info);
	}
	
	@SuppressWarnings("unchecked")
	private void unregisterCommands() {
		Map<String, Command> registeredCommands = null;
		SimpleCommandMap commandMap = ((CraftServer) Bukkit.getServer()).getCommandMap();
		for(String command : this.registeredCommands) {
			CommodoreCommandMetainfo activeCommandInfo = activeCommands.get(command);
			if(activeCommandInfo != null && activeCommandInfo.registerer == this) {
				if(registeredCommands == null)
					registeredCommands = (Map<String, Command>) Commodore.getUtils().getReflectionUtils().getFieldValue(commandMap, "knownCommands");
				registeredCommands.remove(command);
				registerNextCommandFromQueue(command);
			}
		}
	}
	
	private void registerNextCommandFromQueue(String commandName) {
		List<CommodoreCommandMetainfo> queue = commandsQueue.get(commandName);
		if(queue == null) {
			activeCommands.remove(commandName);
			Commodore.getUtils().getMessengingUtils().logCasualWithHighlight("Command ", commandName, " have been unregistered!", Logger.LOGGER_RUNTIME, LogLevel.DEBUG);
		} else {
			int newMetaIndex = 0;
			CommodoreCommandMetainfo newMeta = queue.get(newMetaIndex);
			for(int i = 1; i < queue.size(); ++i) {
				CommodoreCommandMetainfo tempMeta = queue.get(i);
				if(tempMeta.priority.getLevel() > newMeta.priority.getLevel()) {
					newMetaIndex = i;
					newMeta = tempMeta;
				}
			}
			queue.remove(newMetaIndex);
			if(queue.size() == 0)
				commandsQueue.remove(commandName);
			activeCommands.put(commandName, newMeta);
			((CraftServer) Bukkit.getServer()).getCommandMap().register(commandName, newMeta.commandHandler.apply(commandName));
			Commodore.getUtils().getMessengingUtils().craftLogger(Logger.LOGGER_RUNTIME, LogLevel.FULL)
			.colorCasual("Command ")
			.colorCasualHighlighted(commandName)
			.colorCasual(" is about to be unregistered, but there is more commands using such name.").send();
			Commodore.getUtils().getMessengingUtils().craftLogger(Logger.LOGGER_RUNTIME, LogLevel.FULL)
			.colorCasual("New executor is: ")
			.colorCasualHighlighted(newMeta.registerer.moduleName)
			.colorCasual(" with priority ")
			.colorCasualHighlighted(newMeta.priority.toString())
			.colorCasual(".").send();
		}
	}
	
	private static class CommodoreCommandMetainfo {
		private final Priority priority;
		private final ModuleDataInstance registerer;
		private final Function<String, BukkitCommand> commandHandler;
		
		private CommodoreCommandMetainfo(Priority priority, ModuleDataInstance registerer, Function<String, BukkitCommand> commandHandler) {
			this.priority = priority;
			this.registerer = registerer;
			this.commandHandler = commandHandler;
		}
	}
	
	
	/* ---------------- *
	 * Database support *
	 * ---------------- */
	
	@Override
	public void requestDatabaseTable(String tableName, String creationQuery) {
		Commodore.getDatabase().performAction(connection -> {
			try {
				DatabaseMetaData meta = connection.getMetaData();
				try(ResultSet resultSet = meta.getTables(null, null, tableName, new String[] {"TABLE"})) {
					if(!resultSet.next()) {
						connection.createStatement().execute(creationQuery);
						Commodore.getUtils().getMessengingUtils().logCasualWithHighlight("Database table ", tableName, " doesn't exist and will be created.", Logger.LOGGER_SETUP, LogLevel.FULL);
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}
	
	/* ---------------- *
	 * Text registering *
	 * ---------------- */
	
	@Override
	public void registerTextNodes(String relativePathToFile) throws NodeAlreadyExistsException {
		CommodoreUtilsMessenging messenger = Commodore.getUtils().getMessengingUtils();
		CommodoreConfigurationFile config = Commodore.getFilesManager().getConfig(moduleName, relativePathToFile);
		if(!config.exists())
			throw new CommodoreCoreSetupException("Text config at specified path is missing!");
		
		ConfigurationSection configData = config.getData();
		for(String node : configData.getKeys(false)) {
			String value = configData.getString(node);
			Commodore.getTextManager().registerNode(node, value);
		}
		
		messenger.craftLogger(Logger.LOGGER_SETUP, LogLevel.FULL)
		.colorSuccessHighlighted("Text nodes have been set.").send();	
	}
			
	/* -------------- *
	 * State handling *
	 * -------------- */
	
	@Override
	public void initialize() {	//TODO: Return amount of initialized modules, track initialization time
		try {
			setupOrder.forEach(CommodoreSubmodule::setup);
		} catch(Exception e) {
			Commodore.getLoggersManager().log("Error catched during module enabling!", Logger.LOGGER_SETUP, LogLevel.ERROR);
			ControlPanel.exceptionThrown(e);
		}
	}

	@Override
	public CommodoreModuleReloadResult reload(String... arguments) {
		ModuleReloadResultInstance result = new ModuleReloadResultInstance();
		//Reload all if no arguments given
		if(arguments.length == 0) {
			reloadableSubmodules.values().forEach(CommodoreReloadableSubmodule::reload);
			result.reloadedSubmodulesCount = reloadableSubmodules.size();
		} else {
			Iterator<String> iterator = Arrays.asList(arguments).iterator();
			Set<CommodoreReloadableSubmodule> submodules = new LinkedHashSet<>();
			while(iterator.hasNext()) {
				String key = iterator.next();
				CommodoreReloadableSubmodule[] assignedGroup = reloadableGroups.get(key);
				if(assignedGroup != null)
					for(CommodoreReloadableSubmodule submodule : assignedGroup)
						submodules.add(submodule);
				else {
					CommodoreReloadableSubmodule submodule = reloadableSubmodules.get(key);
					if(submodule != null) 
						submodules.add(submodule);
					else
						result.unknownNames.add(key);
				}
			}
			submodules.forEach(submodule -> {
				submodule.reload();
				result.reloadedSubmodulesCount++;
			});
		}
		return result;
	}

	@Override
	public void shutdown() {
		try {
			//Shutdown submodules in order reverse to initialization
			ListIterator<CommodoreSubmodule> submodulesIterator = setupOrder.listIterator(setupOrder.size());
			while(submodulesIterator.hasPrevious())
				submodulesIterator.previous().shutdown();
			unregisterCommands();
		} catch(Exception e) {
			Commodore.getLoggersManager().log("Error catched during module disabling!", Logger.LOGGER_SETUP, LogLevel.ERROR);
			ControlPanel.exceptionThrown(e);
		}
	}
	
	
	private static class ModuleReloadResultInstance implements CommodoreModuleReloadResult {
		private List<String> unknownNames = new ArrayList<>();
		private int reloadedSubmodulesCount = 0;

		@Override
		public List<String> getUnknownNames() {
			return unknownNames;
		}
		
		@Override
		public int getReloadedSubmodulesCount() {
			return reloadedSubmodulesCount;
		}
	}
}
