package pl.mateam.marpg.engine.apiimpl.submodules.modulesmanager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.mateam.marpg.api.modules.modulesmanager.CommodoreModulesManager.CommodoreFullReloadResult;

public class FullReloadResultInstance implements CommodoreFullReloadResult {
	private Map<String, Integer> reloadedSubmodulesCount = new HashMap<>();
	private List<String> notReloadedModules = new ArrayList<>();
	private int totalReloadedSubmodulesCount = 0;
	
	public void putReloadedSubmodulesCount(String key, Integer value) {
		reloadedSubmodulesCount.put(key, value);
	}
	
	public void addNotReloadedModuleName(String name) {
		notReloadedModules.add(name);
	}
	
	public void addTotalReloadedSubmodulesCount(int delta) {
		totalReloadedSubmodulesCount += delta;
	}
	
	@Override
	public Map<String, Integer> getReloadedSubmodulesCount() {
		return Collections.unmodifiableMap(reloadedSubmodulesCount);
	}
	
	@Override
	public List<String> getNotReloadedModulesNames() {
		return Collections.unmodifiableList(notReloadedModules);
	}

	@Override
	public int getTotalReloadedSubmodulesCount() {
		return totalReloadedSubmodulesCount;
	}
}