package tech.mcprison.prison.autofeatures;

/**
 * Roughly based upon Jackson's class by the same name.
 *
 */
public class NullNode
		extends ValueNode
{
    private static final long serialVersionUID = -1L;

    public final static NullNode instance = new NullNode();

    public static NullNode getInstance() { return instance; }
    
    public boolean isNull() {
    	return true;
    }
	
	@Override
	public String toString() {
		return Boolean.toString( true );
	}
	
    @Override
    public boolean isNullNode() { return true; }
    
    @Override
    public NodeType getNodeType() {
        return NodeType.NULL;
    }

    @Override
    public boolean equals(Object o) {
        return o == null || 
        		o == this || 
        		(o instanceof NullNode);
    }

    @Override
    public int hashCode() {
        return -13;
    }

}
