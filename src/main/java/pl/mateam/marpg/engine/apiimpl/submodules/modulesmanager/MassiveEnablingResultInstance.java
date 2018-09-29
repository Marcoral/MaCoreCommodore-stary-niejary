package pl.mateam.marpg.engine.apiimpl.submodules.modulesmanager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pl.mateam.marpg.api.submodules.modulesmanager.CommodoreModulesManager.CommodoreMassiveEnablingResult;

public class MassiveEnablingResultInstance implements CommodoreMassiveEnablingResult {
	private List<String> enabledComponents = new ArrayList<>();
	
	public void addComponentName(String name) {
		enabledComponents.add(name);
	}
	
	@Override
	public List<String> getEnabledModulesNames() {
		return Collections.unmodifiableList(enabledComponents);
	}
}