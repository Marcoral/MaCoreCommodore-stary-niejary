package pl.mateam.marpg.api.modules.componentsmanager;

import pl.mateam.marpg.api.CommodoreComponent;
import pl.mateam.marpg.api.CommodoreRuntimeException;

/*
 * Notice that ComponentsManager uses "CommodoreComponent" interface instead of "CommodoreModule" class.
 * Only making it this way makes possibility of managing of Core.
 */
public interface CommodoreComponentsManager {
	public class ComponentNotLoadedYetException extends CommodoreRuntimeException {
		private static final long serialVersionUID = 7349534588292226736L;

		private String componentName;
		
		public ComponentNotLoadedYetException(String componentName) {
			super("Component must be loaded by bukkit before enabling or disabling it!");
			this.componentName = componentName;
		}
		
		public final String getPassedComponentName() {
			return componentName;
		}
	}

	boolean enableComponent(String componentName) throws ComponentNotLoadedYetException;
	boolean reloadComponent(String componentName) throws ComponentNotLoadedYetException;
	boolean reloadComponent(String componentName, String parameter) throws ComponentNotLoadedYetException;
	boolean disableComponent(String componentName);

	boolean enableComponent(CommodoreComponent component);
	boolean reloadComponent(CommodoreComponent component);
	boolean reloadComponent(CommodoreComponent component, String parameter);
	boolean disableComponent(CommodoreComponent component);
}
