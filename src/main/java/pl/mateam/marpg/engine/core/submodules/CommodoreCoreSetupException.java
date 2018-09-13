package pl.mateam.marpg.engine.core.submodules;

import pl.mateam.marpg.api.superclasses.CommodoreRuntimeException;

public class CommodoreCoreSetupException extends CommodoreRuntimeException {
	private static final long serialVersionUID = -7529158865004894771L;

	public CommodoreCoreSetupException() {
		super();
	}
	
	public CommodoreCoreSetupException(String message) {
		super(message);
	}
}
