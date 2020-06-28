package tech.mcprison.prison.autofeatures;

/**
 * Roughly based upon Jackson's class by the same name.
 *
 */
public class DoubleNode
		extends ValueNode {
	private static final long serialVersionUID = -1L;
	
	protected final double value;
	
	public DoubleNode(double v) { value = v; }
	
	public static DoubleNode valueOf(double d) { return new DoubleNode(d); }
	
	@Override
	public String toString() {
		return Double.toString( getValue() );
	}
	
	public double getValue() {
		return value;
	}
	
	@Override
	public boolean isDoubleNode() { return true; }
	
	@Override
	public NodeType getNodeType() {
		return NodeType.DOUBLE;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (o == null) return false;
        if (o instanceof DoubleNode) {
            // We must account for NaNs: NaN does not equal NaN, therefore we have
            // to use Double.compare().
            final double otherValue = ((DoubleNode) o).value;
            return Double.compare(value, otherValue) == 0;
        }
		return false;
	}
	
	@Override
	public int hashCode() {
        // same as hashCode Double.class uses
        long l = Double.doubleToLongBits(value);
        return ((int) l) ^ (int) (l >> 32);
	}
}
