package pl.mateam.marpg.api.superclasses;

public enum Priority {
	LOWEST(0),
	LOW(1),
	NORMAL(2),
	HIGH(3),
	HIGHEST(4);
	
	private int level;
	private Priority(int level) {
		this.level = level;
	}
	
	public int getLevel() {
		return level;
	}
}