package tech.mcprison.prison.internal;

public class EulerAngle {

	private double x;
	private double y;
	private double z;
	
	public EulerAngle( double x, double y, double z ) {
		super();
		
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public EulerAngle add( double x, double y, double z ) {
		EulerAngle ea = new EulerAngle( x + getX(), y + getY(), z + getZ() );
		return ea;
	}
	
	public EulerAngle subtract( double x, double y, double z ) {
		EulerAngle ea = new EulerAngle( getX() - x , getY() - y, getZ() - z );
		return ea;
	}
	
	
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getZ() {
		return z;
	}
	
	/**
	 * https://math.oxford.emory.edu/site/cs171/generatingHashCodes/
	 */
	public int hashCode() {
		int h = 17;
		h = 31 * h + ((Double) getX()).hashCode();
		h = 31 * h + ((Double) getY()).hashCode();
		h = 31 * h + ((Double) getZ()).hashCode();
		
		return h;
	}
	
	public EulerAngle setX( double x ) {
		EulerAngle ea = new EulerAngle( x, getY(), getZ() );
		return ea;
	}
	public EulerAngle setY( double y ) {
		EulerAngle ea = new EulerAngle( getX(), y, getZ() );
		return ea;
	}
	public EulerAngle setZ( double z ) {
		EulerAngle ea = new EulerAngle( getX() , getY(), z );
		return ea;
	}
	
	
}
