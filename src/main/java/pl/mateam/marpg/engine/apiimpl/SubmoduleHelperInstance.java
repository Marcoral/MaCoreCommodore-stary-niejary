package pl.mateam.marpg.engine.apiimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pl.mateam.marpg.api.CommodoreSubmoduleHelper;
import pl.mateam.marpg.api.superclasses.CommodoreSubmodule;
import pl.mateam.marpg.api.superclasses.ReloadableCommodoreSubmodule;

public class SubmoduleHelperInstance implements CommodoreSubmoduleHelper {
	private List<CommodoreSubmodule> basicSubmodules = new ArrayList<>();
	private Map<String, ReloadableCommodoreSubmodule> reloadableSubmodules = new HashMap<>();
	private Map<String, ReloadableCommodoreSubmodule[]> reloadableGroups = new HashMap<>();
	
	@Override
	public void addSubmodule(CommodoreSubmodule submodule) {
		basicSubmodules.add(submodule);
	}

	@Override
	public void addReloadableSubmodule(ReloadableCommodoreSubmodule submodule, String reloadKey) {
		reloadableSubmodules.put(reloadKey, submodule);
	}

	@Override
	public void createReloadableGroup(String groupKey, ReloadableCommodoreSubmodule... submodules) {
		reloadableGroups.put(groupKey, submodules);
	}

	@Override
	public void initialize() {
		basicSubmodules.forEach(CommodoreSubmodule::setup);
		reloadableSubmodules.values().forEach(CommodoreSubmodule::setup);
	}

	@Override
	public void reload(String... arguments) {
		if(arguments.length == 0) {
			Set<ReloadableCommodoreSubmodule> reloadableSubmodules = new HashSet<>();
			this.reloadableSubmodules.values().forEach(reloadableSubmodules::add);
			for(ReloadableCommodoreSubmodule[] reloadableSubmoduleGroup : reloadableGroups.values())
				for(ReloadableCommodoreSubmodule reloadableSubmodule : reloadableSubmoduleGroup)
					reloadableSubmodules.add(reloadableSubmodule);
			reloadableSubmodules.forEach(ReloadableCommodoreSubmodule::reload);
			return;
		}
		Iterator<String> iterator = Arrays.asList(arguments).iterator();
		while(iterator.hasNext()) {
			String key = iterator.next();
			ReloadableCommodoreSubmodule[] assignedGroup = reloadableGroups.get(key);
			if(assignedGroup != null) {
				for(ReloadableCommodoreSubmodule submodule : assignedGroup)
					submodule.reload();
				iterator.remove();
			} else {
				ReloadableCommodoreSubmodule submodule = reloadableSubmodules.get(key);
				if(submodule != null)
					submodule.reload();
			}
		}
	}

	@Override
	public void shutdown() {
		basicSubmodules.forEach(CommodoreSubmodule::shutdown);
		reloadableSubmodules.values().forEach(CommodoreSubmodule::shutdown);
	}
}
