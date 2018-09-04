package pl.mateam.marpg.engine.core;

public enum ConfigPath {
	_DIRECTORY_CONFIGURATION("configuration"),
	GENERAL(_DIRECTORY_CONFIGURATION.getPath() + "/General.yml");
	
	private String path;
	private ConfigPath(String path) {
		this.path = path;
	}
	
	public String getPath() {
		return path;
	}
}
