package tech.mcprison.prison.autofeatures;

/**
 * Roughly based upon Jackson's class by the same name.
 *
 */
public class LongNode
		extends ValueNode {
	private static final long serialVersionUID = -1L;
	
	protected final long value;
	
    public LongNode(long v) { value = v; }

    public static LongNode valueOf(long l) { return new LongNode(l); }
	
	@Override
	public String toString() {
		return Long.toString( getValue() );
	}
	
    public long getValue() {
    	return value;
    }

    @Override
    public boolean isLongNode() { 
    	return true; 
    }
    
    @Override
    public NodeType getNodeType() {
        return NodeType.LONG;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (o == null) return false;
        if (o instanceof LongNode) {
            return ((LongNode) o).value == value;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return ((int) value) ^ (int) (value >> 32);
    }
}
