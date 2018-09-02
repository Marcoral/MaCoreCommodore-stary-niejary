package pl.mateam.marpg.engine.apiimpl.modules.componentsmanager;

import pl.mateam.marpg.api.CommodoreComponent;

public class CommodoreComponentInfo {
	public static CommodoreComponentInfo createFor(CommodoreComponent component) {
		return new CommodoreComponentInfo(component);
	}

	private final CommodoreComponent instance;
	private boolean isComponentEnabled = false;
	private CommodoreComponentInfo(CommodoreComponent instance) {
		this.instance = instance;
	}
	
	public CommodoreComponent getPassedInstance() {
		return instance;
	}

	public boolean isComponentEnabled() {
		return isComponentEnabled;
	}
	
	public void markEnabled(boolean enabled) {
		this.isComponentEnabled = enabled;
	}
}
