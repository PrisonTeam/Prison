package tech.mcprison.prison.autofeatures;

import java.util.ArrayList;
import java.util.List;

public class StringListNode
	extends ValueNode
{
    private static final long serialVersionUID = -1L;


    protected final List<String> value;

    public StringListNode(List<String> stringList) { 
    	super();
    	if ( stringList == null ) {
    		this.value = new ArrayList<>();
    	}
    	else {
    		this.value = stringList; 
    	}
    }
	
	public static StringListNode valueOf(List<String> stringList) 
	{ 
		return new StringListNode( stringList ); 
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append( String.join( ", ", getValue() ) );
		sb.insert( 0, "[" ).append("]");
		
		return sb.toString();
	}
	
	public List<String> getValue() {
		return value;
	}
	
	@Override
	public boolean isStringListNode() { return true; }
	
	@Override
	public NodeType getNodeType() {
		return NodeType.STRING_LIST;
	}
   
    
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (o == null) return false;
        if (o instanceof StringListNode) {
        	
        	final List<String> otherValue = ((StringListNode) o).getValue();
        	if ( getValue() == null && otherValue == null ) return true;
        	if ( getValue() != null ) {
        		return getValue().equals( otherValue );
        	}
        }
		return false;
	}
	
	@Override
	public int hashCode() {
        // same as hashCode that List uses
        return getValue().hashCode();
	}

}
