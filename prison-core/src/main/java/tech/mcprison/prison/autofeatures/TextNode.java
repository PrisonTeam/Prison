package tech.mcprison.prison.autofeatures;

/**
 * Roughly based upon Jackson's class by the same name.
 *
 */
public class TextNode
	extends ValueNode
{
    private static final long serialVersionUID = -1L;

    final static TextNode EMPTY_STRING_NODE = new TextNode("");

    protected final String value;

    public TextNode(String v) { 
    	super();
    	value = v; 
    }
	
	@Override
	public String toString() {
		return getValue();
	}
	
    /**
     * Factory method that should be used to construct instances.
     * For some common cases, can reuse canonical instances: currently
     * this is the case for empty Strings, in future possible for
     * others as well. If null is passed, will return null.
     *
     * @return Resulting {@link TextNode} object, if <b>v</b>
     *   is NOT null; null if it is.
     */
    public static TextNode valueOf(String v)
    {
        if (v == null) {
            return null;
        }
        if (v.length() == 0) {
            return EMPTY_STRING_NODE;
        }
        return new TextNode(v);
    }

    public String getValue() {
    	return value;
    }
    
    @Override
    public NodeType getNodeType() {
        return NodeType.STRING;
    }
    
    @Override
    public boolean isTextNode() {
    	return true;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (o == null) return false;
        if (o instanceof TextNode) {
            return ((TextNode) o).value.equals(value);
        }
        return false;
    }
    
    @Override
    public int hashCode() { return value.hashCode(); }

}
