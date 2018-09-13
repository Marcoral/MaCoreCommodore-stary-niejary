package pl.mateam.marpg.engine.core.submodules.server;

import pl.mateam.marpg.api.modules.server.CommodoreServer;
import pl.mateam.marpg.engine.core.submodules.server.worlds.WorldsManagerImplementation;

public class ServerImplementation implements CommodoreServer {
	private WorldsManagerImplementation worldsManager = new WorldsManagerImplementation();

	/* ------------------- */
	/* Implementation part */
	/* ------------------- */
	
//	@Override	- uncomment, when getWorldsManager becomes part of the API
	public WorldsManagerImplementation getWorldsManager() {
		return worldsManager;
	}
}
