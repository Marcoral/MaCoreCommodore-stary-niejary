package pl.mateam.marpg.engine.apiimpl.submodules.modulesmanager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pl.mateam.marpg.api.submodules.modulesmanager.CommodoreModulesManager.CommodoreMassiveDisablingResult;

public class MassiveDisablingResultInstance implements CommodoreMassiveDisablingResult {
	private List<String> disabledComponents = new ArrayList<>();
	
	public void addComponentName(String name) {
		disabledComponents.add(name);
	}
	
	@Override
	public List<String> getDisabledModulesNames() {
		return Collections.unmodifiableList(disabledComponents);
	}
}