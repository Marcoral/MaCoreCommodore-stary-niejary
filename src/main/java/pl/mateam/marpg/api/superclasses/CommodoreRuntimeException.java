package pl.mateam.marpg.api.superclasses;

public class CommodoreRuntimeException extends RuntimeException {
	private static final long serialVersionUID = -6694682231757707525L;

	public CommodoreRuntimeException() {
		super();
	}
	
	public CommodoreRuntimeException(String message) {
		super(message);
	}
}
