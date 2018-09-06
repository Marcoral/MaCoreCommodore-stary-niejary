package pl.mateam.marpg.api.modules.server.worlds;

public enum CommodoreWorld {
	KARHAN("Karhan");
	
	private String directoryName;
	private CommodoreWorld(String directoryName) {
		this.directoryName = directoryName;
	}
	
	public String getDirectoryName() {
		return directoryName;
	}
}
