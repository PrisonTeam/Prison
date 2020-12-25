package tech.mcprison.prison.autofeatures;

import java.io.Serializable;

/**
 * Roughly based upon Jackson's class by the same name.
 *
 */
public abstract class ValueNode
	implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum NodeType {
		NULL,
		STRING,
		BOOLEAN,
		LONG,
		INTEGER,
		DOUBLE,
		STRING_LIST
		;
	}
	
	public boolean isNullNode() { return false; }

	public boolean isTextNode() { return false; }
	
	public boolean isBooleanNode() { return false; }
	
	public boolean isIntegerNode() { return false; }
	
	public boolean isLongNode() { return false; }
	
	public boolean isDoubleNode() { return false; }
	
	public boolean isStringListNode() { return false; }
	
	
    public abstract NodeType getNodeType();

    public abstract boolean equals(Object o);
    
    public abstract int hashCode();
    

}
