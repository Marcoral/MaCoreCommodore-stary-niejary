package pl.mateam.marpg.api.superclasses;

public enum LogLevel {
	NONE(0),
	ERROR(1),
	WARNING(2),
	FULL(3),
	DEBUG(4);
	
	private int level;
	private LogLevel(int level) {
		this.level = level;
	}
	
	public int getLevel() {
		return level;
	}
}
