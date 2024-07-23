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
	
	public BombAnimationOrbital(MineBombData bomb, PrisonBlock sBombBlock, 
			ItemStack item, BombAnimationsTask task,
			float entityYaw, float entityPitch) {
		super(bomb, sBombBlock, item, task, entityYaw, entityPitch);

		this.angle = entityYaw;

		
		Vector vec = new Vector( 
						bomb.getAnimationOffset(), 0d, bomb.getAnimationOffset());
		getOriginalLocation().add( vec );
		
//		getOriginalLocation().setX( 
//					getOriginalLocation().getX() + bomb.getAnimationOffset());
//		getOriginalLocation().setZ( 
//					getOriginalLocation().getZ() + bomb.getAnimationOffset());
		
	}
	
	@Override
	public void stepAnimation() {

		if ( isAlternateDirections() ) {
			
			if ( getId() % 2 == 0 ) {
				angle += getBomb().getAnimationSpeed();
			}
			else {
				angle -= getBomb().getAnimationSpeed();
			}
			
		}
		else {
			angle += getBomb().getAnimationSpeed();
		}
		
		if ( angle > 360 ) {
			angle -= 360;
		}
		else if ( angle < -360 ) {
			angle += 360;
		}
		
		Vector vector = GeometricShapes.getPointsOnCircleXZ( angle, radius );
		
		// Location.add() creates a new instance of a Location and does not change
		// the original value:
		Location newLoc = getOriginalLocation().add(vector);
		getArmorStand().teleport( newLoc );
		
		
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

}
