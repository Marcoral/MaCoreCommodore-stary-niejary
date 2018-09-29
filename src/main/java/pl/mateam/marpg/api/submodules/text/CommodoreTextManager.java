package pl.mateam.marpg.api.submodules.text;

import pl.mateam.marpg.api.superclasses.CommodoreRuntimeException;

public interface CommodoreTextManager {
	String getNode(TextNode node);
	String getNode(String node);
	void registerNode(String nodeId, String text) throws NodeAlreadyExistsException;
	
	public static class NodeAlreadyExistsException extends CommodoreRuntimeException {
		private static final long serialVersionUID = 6921862107571700353L;
		private final String node;
		public NodeAlreadyExistsException(String node) {
			super("Text node " + node + " is already defined!");
			this.node = node;
		}
		
		public String getConflictingNode() {
			return node;
		}
	}
}
