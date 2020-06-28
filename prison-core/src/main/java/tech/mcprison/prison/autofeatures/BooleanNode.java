package tech.mcprison.prison.autofeatures;

/**
 * Roughly based upon Jackson's class by the same name.
 *
 */
public class BooleanNode
		extends ValueNode {
	private static final long serialVersionUID = -1L;

	public final static BooleanNode TRUE = new BooleanNode(true);
    public final static BooleanNode FALSE = new BooleanNode(false);

    private final boolean value;

    protected BooleanNode(boolean v) { value = v; }


    public static BooleanNode getTrue() { return TRUE; }
    public static BooleanNode getFalse() { return FALSE; }

    public static BooleanNode valueOf(boolean b) { return b ? TRUE : FALSE; }

	
	@Override
	public String toString() {
		return Boolean.toString( getValue() );
	}
	
    public boolean getValue() {
        return value;
    }
    
    @Override
    public boolean isBooleanNode() { 
    	return true; 
    }
    
	@Override
	public NodeType getNodeType() {
		return NodeType.BOOLEAN;
	}


    @Override
    public int hashCode() {
        return value ? 3 : 1;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (o == null) return false;
        if (!(o instanceof BooleanNode)) {
            return false;
        }
        return (value == ((BooleanNode) o).value);
    }

}
