package pl.mateam.marpg.api;

import java.util.function.Supplier;

import pl.mateam.marpg.api.submodules.modulesmanager.CommodoreModulesManager.CommodoreModuleReloadResult;
import pl.mateam.marpg.api.submodules.text.CommodoreTextManager.NodeAlreadyExistsException;
import pl.mateam.marpg.api.superclasses.CommodoreGenericCommand;
import pl.mateam.marpg.api.superclasses.CommodorePlayerCommand;
import pl.mateam.marpg.api.superclasses.CommodoreSubmodule;
import pl.mateam.marpg.api.superclasses.Priority;
import pl.mateam.marpg.api.superclasses.CommodoreReloadableSubmodule;

public interface CommodoreModuleData {
	void addSubmodule(CommodoreSubmodule submodule);
	void addReloadableSubmodule(CommodoreReloadableSubmodule submodule, String reloadKey);
	void createReloadableGroup(String groupKey, CommodoreReloadableSubmodule... submodules);
	
	void registerGenericCommand(Supplier<? extends CommodoreGenericCommand> command, String name, String... aliases);
	void registerPlayerCommand(Supplier<? extends CommodorePlayerCommand> command, String name, String... aliases);
	void registerGenericCommand(Supplier<? extends CommodoreGenericCommand> command, Priority aliasesPriority, String name, String... aliases);
	void registerPlayerCommand(Supplier<? extends CommodorePlayerCommand> command, Priority aliasesPriority, String name, String... aliases);
	
	void requestDatabaseTable(String tableName, String creationQuery);
	
	
	void registerTextNodes(String relativePathToFile) throws NodeAlreadyExistsException;


	void initialize();
	void shutdown();
	CommodoreModuleReloadResult reload(String... arguments);
}