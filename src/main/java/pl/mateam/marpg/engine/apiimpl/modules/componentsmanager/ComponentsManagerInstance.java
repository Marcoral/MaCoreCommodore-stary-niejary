package pl.mateam.marpg.engine.apiimpl.modules.componentsmanager;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;

import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.api.CommodoreComponent;
import pl.mateam.marpg.api.modules.componentsmanager.CommodoreComponentsManager;

public class ComponentsManagerInstance implements CommodoreComponentsManager {
	private Map<String, CommodoreComponentInfo> knownComponents = new HashMap<>();

	public CommodoreComponent getComponentInstanceOrThrow(String componentName) throws ComponentNotLoadedYetException {
		CommodoreComponentInfo info = knownComponents.get(componentName);
		if(info == null)
			throw new ComponentNotLoadedYetException(componentName);
		return info.getPassedInstance();
	}
	
	@Override
	public boolean enableComponent(String componentName) throws ComponentNotLoadedYetException {
		return enableComponent(getComponentInstanceOrThrow(componentName));
	}
	
	@Override
	public boolean reloadComponent(String componentName) throws ComponentNotLoadedYetException {
		return reloadComponent(componentName, "");	//Using empty String, so use of methods on it (like .equals) is save
	}
	
	@Override
	public boolean reloadComponent(String componentName, String parameter) throws ComponentNotLoadedYetException {
		return reloadComponent(getComponentInstanceOrThrow(componentName), parameter);
	}

	@Override
	public boolean disableComponent(String componentName) throws ComponentNotLoadedYetException {
		return disableComponent(getComponentInstanceOrThrow(componentName));
	}

	@Override
	public boolean enableComponent(CommodoreComponent component) {
		CommodoreComponentInfo componentInfo = knownComponents.get(component.getPlugin().getName());
		if(componentInfo == null) {
			componentInfo = CommodoreComponentInfo.createFor(component);
			knownComponents.put(component.getPlugin().getName(), componentInfo);
			Commodore.getFilesManager().regenerateConfiguration(component);
		} else if(componentInfo.isComponentEnabled())
			return false;
		component.onBeingTurnedOn();
		componentInfo.markEnabled(true);
		return true;
	}

	@Override
	public boolean disableComponent(CommodoreComponent component) {
		if(!Bukkit.getServer().getPluginManager().isPluginEnabled(component.getPlugin()))
			return false;
		component.onBeingTurnedOff();
		knownComponents.get(component.getPlugin().getName()).markEnabled(false);
		return true;
	}

	@Override
	public boolean reloadComponent(CommodoreComponent component) {
		return reloadComponent(component, "");	//Using empty String, so use of methods on it (like .equals) is save
	}

	@Override
	public boolean reloadComponent(CommodoreComponent component, String parameter) {
		if(!Bukkit.getServer().getPluginManager().isPluginEnabled(component.getPlugin()))
			return false;
		return component.onBeingReloaded(parameter);
	}
}
