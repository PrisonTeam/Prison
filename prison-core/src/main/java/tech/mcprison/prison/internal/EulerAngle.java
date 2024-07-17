package tech.mcprison.prison.internal;

import java.text.DecimalFormat;

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
	
	public String toString() {
		DecimalFormat dFmt = new DecimalFormat( "#,##0.0000" );
		String fmt = String.format( 
				"EulerAngle: x: %s  y: %s  z: %s",
				dFmt.format( getX() ),
				dFmt.format( getY() ),
				dFmt.format( getZ() )
				);
		return fmt;
	}
	
	public EulerAngle add( double x, double y, double z ) {
		EulerAngle ea = new EulerAngle( x + getX(), y + getY(), z + getZ() );
		return ea;
	}
	
	public EulerAngle subtract( double x, double y, double z ) {
		EulerAngle ea = new EulerAngle( getX() - x , getY() - y, getZ() - z );
		return ea;
	}
	
	
	/**
	 * NOTE: These do not work.  Not sure what the angle should be, or what
	 * the starting angles should be, but it goes to zero pretty quickly.
	 * So do not use these functions. 
	 * 
	 * 
	 * The rotateAroundAxis is based upon the following post, reply number 11:
	 * https://www.spigotmc.org/threads/how-to-calculate-armorstand-arm-tip-location.331825/#post-3284591
	 * 
	 * @param angle
	 * @return
	 */
	public EulerAngle rotateAroundAxisX( double angle ) {
		EulerAngle ea = rotateAroundAxisX( getX(), getY(), getZ(), angle );
		
		setX( ea.getX() );
		setY( ea.getY() );
		setZ( ea.getZ() );
		
        return this;
    }
	
    public static EulerAngle rotateAroundAxisX( double x, double y, double z, double angle ) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        y = y * cos - z * sin;
        z = y * sin + z * cos;
        return new EulerAngle( x, y, z );
    }

	public EulerAngle rotateAroundAxisY( double angle ) {
		EulerAngle ea = rotateAroundAxisY( getX(), getY(), getZ(), angle );
		
		setX( ea.getX() );
		setY( ea.getY() );
		setZ( ea.getZ() );
		
//		Output.get().logInfo( toString() + "  angle: " + angle );
		
        return this;
    }
	
    public static EulerAngle rotateAroundAxisY( double x, double y, double z, double angle ) {
        angle = -angle;
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        x = x * cos + z * sin;
        z = x * -sin + z * cos;
        return new EulerAngle( x, y, z );
    }

	public EulerAngle rotateAroundAxisZ( double angle ) {
		EulerAngle ea = rotateAroundAxisZ( getX(), getY(), getZ(), angle );
		
		setX( ea.getX() );
		setY( ea.getY() );
		setZ( ea.getZ() );
		
        return this;
    }
	
    public EulerAngle rotateAroundAxisZ( double x, double y, double z, double angle ) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        x = x * cos - y * sin;
        y = x * sin + y * cos;
        return new EulerAngle( x, y, z );
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
	
	public double getX() {
		return x;
	}
	public EulerAngle setX( double x ) {
		this.x = x;
//		EulerAngle ea = new EulerAngle( x, getY(), getZ() );
		return this;
	}

	public double getY() {
		return y;
	}
	public EulerAngle setY( double y ) {
		this.y = y;
//		EulerAngle ea = new EulerAngle( getX(), y, getZ() );
		return this;
	}

	public double getZ() {
		return z;
	}
	public EulerAngle setZ( double z ) {
		this.z = z;
//		EulerAngle ea = new EulerAngle( getX() , getY(), z );
		return this;
	}
	
	
}
