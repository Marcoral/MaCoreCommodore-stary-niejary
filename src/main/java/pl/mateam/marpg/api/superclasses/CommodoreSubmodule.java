package pl.mateam.marpg.api.superclasses;

public interface CommodoreSubmodule {
	void setup();
	default void shutdown() {}	//Hook
}
