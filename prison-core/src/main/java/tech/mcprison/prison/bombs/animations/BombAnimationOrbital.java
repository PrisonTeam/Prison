package tech.mcprison.prison.bombs.animations;

import tech.mcprison.prison.bombs.GeometricShapes;
import tech.mcprison.prison.bombs.MineBombData;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.util.Location;
import tech.mcprison.prison.util.Vector;

/*
 * NOTE: The whole rotateAroundAxisY does not work.
 * So for now, disable this option.
 */
public class BombAnimationOrbital extends BombAnimations {

	private double angle = 0.0;
	private double radius = 1.0;
	
	private boolean alternateDirections = false;
	
	private Location originalLocation;
	
//	private Location oLoc = null;

	public BombAnimationOrbital(MineBombData bomb, PrisonBlock sBombBlock, 
			ItemStack item, BombAnimationsTask task,
			float entityYaw, float entityPitch) {
		super(bomb, sBombBlock, item, task, entityYaw, entityPitch);

		this.angle = entityYaw;

		this.originalLocation = getArmorStand().getLocation();
		
		
	}
	
	@Override
	public void stepAnimation() {

		if ( isAlternateDirections() ) {
			
			if ( getId() % 2 == 0 ) {
				angle += 5;
			}
			else {
				angle -= 5;
			}
			
		}
		else {
			angle += 5;
		}
		
		if ( angle > 360 ) {
			angle -= 360;
		}
		else if ( angle < -360 ) {
			angle += 360;
		}
		
//		if ( oLoc == null ) {
//			oLoc = new Location( getArmorStand().getLocation() );
//		}

//		Location loc = getArmorStand().getLocation();
		Vector vector = GeometricShapes.getPointsOnCircleXZ( angle, radius );
		
		// Location.add() creates a new instance of a Location and does not change
		// the original value:
		Location newLoc = getOriginalLocation().add(vector);
		getArmorStand().teleport( newLoc );
		
		
//		oLoc.setYaw( (float) angle );
//		getArmorStand().teleport( oLoc );

		
		
//		Location loc = getArmorStand().getLocation();
//		loc.setDirection( vector );
		
//		float radians = (float) Math.toRadians(angle);
//		loc.setYaw( radians );
		
//		getArmorStand().teleport( loc );

		
//		DecimalFormat dFmt = new DecimalFormat( "#,##0.0000" );
//		Output.get().logInfo(
//				String.format(
//						"Orbital direction:  angle: %8s  x: %8s  y: %8s  z: %8s",
//						dFmt.format(angle),
//						dFmt.format( vector.getX() ),
//						dFmt.format( vector.getY() ),
//						dFmt.format( vector.getY() )
//				)
//			);
//		
		
		
		
		
//		double angle = 0.05;
//		Vector direction = getArmorStand().getLocation().getDirection();
//
//		setEulerAngleX( direction.getX() + angle);
//		
//		direction.setX( getEulerAngleX() );
//		
//		getArmorStand().getLocation().setDirection(direction);
//		
//		
//		Output.get().logInfo( "Orbital direction:  x: " + getEulerAngleX() + 
//					"  y: " + getEulerAngleY() +
//					"  z: " + getEulerAngleZ() );
		
		
//		angle += 5;

		//arm.rotateAroundAxisY( angle );
		
//		Output.get().logInfo( arm.toString() );
		
		//getArmorStand().setRightArmPose( arm );
		
		
//		EulerAngle arm = EulerAngle.rotateAroundAxisY( 
//					getEulerAngleX(), 
//					getEulerAngleY(), 
//					getEulerAngleZ(), 
//					angle );
//				
//		setEulerAngleX( arm.getX() );
//		setEulerAngleY( arm.getY() );
//		setEulerAngleZ( arm.getZ() );
		
//		double speed = 0.35;
//
//		setEulerAngleX( getEulerAngleX() + speed );
//		setEulerAngleY( getEulerAngleY() + speed );
////		setEulerAngleZ( getEulerAngleZ() + speed );
//		
//		
//		EulerAngle arm = new EulerAngle( getEulerAngleX(), getEulerAngleY(), getEulerAngleZ() );
		
//		getArmorStand().setRightArmPose(arm);
	}

	public double getAngle() {
		return angle;
	}
	public void setAngle(double angle) {
		this.angle = angle;
	}

	public double getRadius() {
		return radius;
	}
	public void setRadius(double radius) {
		this.radius = radius;
	}

	public boolean isAlternateDirections() {
		return alternateDirections;
	}
	public void setAlternateDirections(boolean alternateDirections) {
		this.alternateDirections = alternateDirections;
	}

	public Location getOriginalLocation() {
		return originalLocation;
	}
	public void setOriginalLocation(Location originalLocation) {
		this.originalLocation = originalLocation;
	}

}
