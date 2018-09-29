package pl.mateam.marpg.api.submodules.server.worlds;

public enum CommodoreWorld {
	KARHAN("Karhan");
	
	private String configurationPath;
	private CommodoreWorld(String configurationPath) {
		this.configurationPath = configurationPath;
	}
	
	public String getConfigurationPath() {
		return configurationPath;
	}
}
