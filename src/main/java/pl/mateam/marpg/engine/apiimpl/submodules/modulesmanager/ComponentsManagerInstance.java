package pl.mateam.marpg.engine.apiimpl.submodules.modulesmanager;

import java.util.Map;
import java.util.TreeMap;

import org.bukkit.Bukkit;

import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.api.CommodoreComponent;
import pl.mateam.marpg.api.modules.modulesmanager.CommodoreModulesManager;
import pl.mateam.marpg.api.superclasses.CommodoreModule;
import pl.mateam.marpg.engine.Initializer;
import pl.mateam.marpg.engine.apiimpl.ModuleDataInstance;

public class ComponentsManagerInstance implements CommodoreModulesManager {
	private Map<String, CommodoreComponentInfo> knownComponents = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

	public CommodoreComponent getComponentInstanceOrThrow(String componentName) throws ModuleNotLoadedYetException {
		CommodoreComponentInfo info = knownComponents.get(componentName);
		if(info == null)
			throw new ModuleNotLoadedYetException(componentName);
		return info.getPassedInstance();
	}
	
	@Override
	public boolean isModuleLoaded(String componentName) {
		return knownComponents.containsKey(componentName);
	}
	
	@Override
	public boolean enableModule(String componentName) throws ModuleNotLoadedYetException {
		return enableModule(getComponentInstanceOrThrow(componentName));
	}
	
	@Override
	public CommodoreModuleReloadResult reloadModule(String componentName) throws ModuleNotLoadedYetException {
		return reloadModule(componentName, new String[0]);
	}
	
	@Override
	public CommodoreModuleReloadResult reloadModule(String componentName, String... arguments) throws ModuleNotLoadedYetException {
		return reloadModule(getComponentInstanceOrThrow(componentName), arguments);
	}
	
	@Override
	public CommodoreFullReloadResult reloadAllEnabledModules() {
		FullReloadResultInstance result = new FullReloadResultInstance();
		knownComponents.values().forEach(componentInfo -> {
			CommodoreComponent component = componentInfo.getPassedInstance();
			String componentName = component.getPlugin().getName();
			if(componentInfo.isComponentEnabled) {
				int reloadedSubmodulesCount = reloadModule(componentInfo.getPassedInstance()).getReloadedSubmodulesCount();
				result.putReloadedSubmodulesCount(componentName, reloadedSubmodulesCount);
				result.addTotalReloadedSubmodulesCount(reloadedSubmodulesCount);
			} else
				result.addNotReloadedModuleName(componentName);
		});
		return result;
	}
	
	@Override
	public CommodoreMassiveEnablingResult enableAllModules() {
		MassiveEnablingResultInstance result = new MassiveEnablingResultInstance();
		knownComponents.values().forEach(component -> {
			if(!component.isComponentEnabled) {
				enableModule(component.getPassedInstance());
				result.addComponentName(component.getPassedInstance().getPlugin().getName());
			}
		});
		return result;
	}
	
	@Override
	public CommodoreMassiveDisablingResult disableAllModules() {
		MassiveDisablingResultInstance result = new MassiveDisablingResultInstance();
		knownComponents.values().forEach(component -> {
			if(component.isComponentEnabled && component.instance.getPlugin() != Initializer.getInstance()) {
				disableModule(component.getPassedInstance());
				result.addComponentName(component.getPassedInstance().getPlugin().getName());
			}
		});
		return result;
	}

	@Override
	public boolean disableModule(String componentName) throws ModuleNotLoadedYetException {
		return disableModule(getComponentInstanceOrThrow(componentName));
	}
	
	@Override
	public boolean enableModule(CommodoreComponent component) {
		if(!Bukkit.getPluginManager().isPluginEnabled(component.getPlugin())) {
			Bukkit.getPluginManager().enablePlugin(component.getPlugin());
			return true;
		}
		CommodoreComponentInfo componentInfo = knownComponents.get(component.getPlugin().getName());
		if(componentInfo == null) {
			componentInfo = new CommodoreComponentInfo(component);
			knownComponents.put(component.getPlugin().getName(), componentInfo);
			Commodore.getFilesManager().regenerateConfiguration(component);
		} else if(componentInfo.isComponentEnabled)
			return false;
		if(component instanceof CommodoreModule)
			Commodore.getUtils().getDevelopmentUtils().injectExternalField(CommodoreModule.class, (CommodoreModule) component, "module data", new ModuleDataInstance(component.getPlugin().getName()), true);
		component.onBeingTurnedOn();
		componentInfo.isComponentEnabled = true;
		return true;
	}

	@Override
	public boolean disableModule(CommodoreComponent component) {
		if(Bukkit.getPluginManager().isPluginEnabled(component.getPlugin())) {
			Bukkit.getPluginManager().disablePlugin(component.getPlugin());
			return true;
		}
		CommodoreComponentInfo componentInfo = knownComponents.get(component.getPlugin().getName());
		if(!componentInfo.isComponentEnabled)
			return false;
		component.onBeingTurnedOff();
		componentInfo.isComponentEnabled = false;
		return true;
	}

	@Override
	public CommodoreModuleReloadResult reloadModule(CommodoreComponent component) {
		return reloadModule(component, new String[0]);
	}

	@Override
	public CommodoreModuleReloadResult reloadModule(CommodoreComponent component, String... arguments) {
		if(!Bukkit.getServer().getPluginManager().isPluginEnabled(component.getPlugin()))
			return null;
		return component.onBeingReloaded(arguments);
	}

	@Override
	public boolean isModuleEnabled(String componentName) throws ModuleNotLoadedYetException {
		return isModuleEnabled(getComponentInstanceOrThrow(componentName));
	}

	@Override
	public boolean isModuleEnabled(CommodoreComponent component) {
		CommodoreComponentInfo componentInfo = knownComponents.get(component.getPlugin().getName());
		return componentInfo.isComponentEnabled;
	}
	
	private static class CommodoreComponentInfo {
		private final CommodoreComponent instance;
		private boolean isComponentEnabled = false;
		private CommodoreComponentInfo(CommodoreComponent instance) {
			this.instance = instance;
		}
		
		public CommodoreComponent getPassedInstance() {
			return instance;
		}
	}
}
