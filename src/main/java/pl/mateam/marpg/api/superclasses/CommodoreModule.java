package pl.mateam.marpg.api.superclasses;

import java.util.function.Supplier;

import org.bukkit.plugin.java.JavaPlugin;

import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.api.CommodoreComponent;
import pl.mateam.marpg.api.CommodoreModuleData;
import pl.mateam.marpg.api.submodules.modulesmanager.CommodoreModulesManager.CommodoreModuleReloadResult;
import pl.mateam.marpg.api.submodules.text.CommodoreTextManager.NodeAlreadyExistsException;
import pl.mateam.marpg.api.submodules.utils.CommodoreUtilsDevelopment.Extern;

public abstract class CommodoreModule extends JavaPlugin implements CommodoreComponent {
	@Extern(key = "module data")
	private CommodoreModuleData moduleData;

	/* ---------- *
	 * Submodules *
	 * ---------- */

	protected final void addSubmodule(CommodoreSubmodule submodule) {
		moduleData.addSubmodule(submodule);
	}
	
	protected final void addReloadableSubmodule(CommodoreReloadableSubmodule submodule, String reloadKey) {
		moduleData.addReloadableSubmodule(submodule, reloadKey);
	}
	
	protected final void createReloadableGroup(String groupKey, CommodoreReloadableSubmodule... submodules) {
		moduleData.createReloadableGroup(groupKey, submodules);
	}
	
	/* -------- *
	 * Commands *
	 * -------- */
	
	protected void registerGenericCommand(Supplier<? extends CommodoreGenericCommand> command, String name, String... aliases) {
		moduleData.registerGenericCommand(command, name, aliases);
	}
	
	protected void registerPlayerCommand(Supplier<? extends CommodorePlayerCommand> command, String name, String... aliases) {
		moduleData.registerPlayerCommand(command, name, aliases);
	}
	
	protected void registerGenericCommand(Supplier<? extends CommodoreGenericCommand> command, Priority aliasesPriority, String name, String... aliases) {
		moduleData.registerGenericCommand(command, aliasesPriority, name, aliases);
	}
	
	protected void registerPlayerCommand(Supplier<? extends CommodorePlayerCommand> command, Priority aliasesPriority, String name, String... aliases) {
		moduleData.registerPlayerCommand(command, aliasesPriority, name, aliases);
	}
	
	/* ---------- *
	 * Database *
	 * ---------- */
	
	protected void requestDatabaseTable(String tableName, String creationQuery) {
		moduleData.requestDatabaseTable(tableName, creationQuery);
	}
	
	/* ---- *
	 * Text *
	 * ---- */
	
	protected void registerTextNodes(String relativePathToFile) throws NodeAlreadyExistsException {
		moduleData.registerTextNodes(relativePathToFile);
	}
	
	/* -------- *
	 * Handling *
	 * -------- */
	
	@Override
	public final JavaPlugin getPlugin() {
		return this;
	}

	@Override
	public final CommodoreModuleReloadResult onBeingReloaded(String... arguments) {
		CommodoreModuleReloadResult result = moduleData.reload(arguments);
		afterReload();
		return result;
	}
	
	@Override
	public final void onEnable() {
		Commodore.getModulesManager().enableModule(this);
		moduleData.initialize();
	}
	
	@Override
	public final void onDisable() {
		Commodore.getModulesManager().disableModule(this);
		moduleData.shutdown();
	}

	protected void afterReload() {}	//Hook
	
	@Override
	public void onBeingTurnedOff() {}	//Just hook, doesn't have to be implemented in opposite to onBeingTurnedOn
}
