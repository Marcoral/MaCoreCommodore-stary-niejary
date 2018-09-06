package pl.mateam.marpg.engine.core;

public enum ConfigPath {
	_DIRECTORY_CONFIGURATION("configuration"),
	ENVIRONMENT(_DIRECTORY_CONFIGURATION.getPath() + "/Environment.yml");
	
	private String path;
	private ConfigPath(String path) {
		this.path = path;
	}
	
	public String getPath() {
		return path;
	}
}
