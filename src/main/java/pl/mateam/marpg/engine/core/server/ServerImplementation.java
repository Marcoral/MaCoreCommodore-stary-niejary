package pl.mateam.marpg.engine.core.server;

import pl.mateam.marpg.api.modules.server.CommodoreServer;
import pl.mateam.marpg.api.modules.server.worlds.CommodoreWorldsManager;
import pl.mateam.marpg.api.superclasses.ReloadableCommodoreSubmodule;
import pl.mateam.marpg.engine.core.server.worlds.WorldsManagerImplementation;

public class ServerImplementation implements CommodoreServer, ReloadableCommodoreSubmodule {
	private WorldsManagerImplementation worldsManager = new WorldsManagerImplementation();

	/* ------------------- */
	/* Core submodule part */
	/* ------------------- */

	@Override
	public void setup() {
		worldsManager.setup();
	}
	
	@Override
	public void reload() {
		worldsManager.reload();
	}

	/* ------------------- */
	/* Implementation part */
	/* ------------------- */
	
//	@Override	- uncomment, when getWorldsManager became part of the API
	public CommodoreWorldsManager getWorldsManager() {
		return worldsManager;
	}
}
