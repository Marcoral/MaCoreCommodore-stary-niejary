package pl.mateam.marpg.engine.core.internal;

public enum ConfigPath {
	_DIRECTORY_SETUP("setup"),
	SETUP_DATABASE(_DIRECTORY_SETUP.getPath() + "/Database tables.yml"),
	
	_DIRECTORY_CONFIGURATION("configuration"),
	BANS(_DIRECTORY_CONFIGURATION.getPath() + "/Bans reasons.yml"),
	ENVIRONMENT(_DIRECTORY_CONFIGURATION.getPath() + "/Environment.yml"),
	TEXT(_DIRECTORY_CONFIGURATION.getPath() + "/Text.yml");
	
	private String path;
	private ConfigPath(String path) {
		this.path = path;
	}
	
	public String getPath() {
		return path;
	}
}
