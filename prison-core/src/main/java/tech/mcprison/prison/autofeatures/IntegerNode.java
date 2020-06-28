package tech.mcprison.prison.autofeatures;

public class IntegerNode
		extends ValueNode {

	private static final long serialVersionUID = -1L;
	
	protected final int value;
	
    public IntegerNode(int v) { value = v; }

    public static IntegerNode valueOf(int i) { return new IntegerNode(i); }
	
	@Override
	public String toString() {
		return Integer.toString( getValue() );
	}
	
    public int getValue() {
    	return value;
    }

    @Override
    public boolean isIntegerNode() { 
    	return true; 
    }
    
    @Override
    public NodeType getNodeType() {
        return NodeType.INTEGER;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (o == null) return false;
        if (o instanceof IntegerNode) {
            return ((IntegerNode) o).value == value;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return ((int) value) ^ (int) (value >> 32);
    }
}
