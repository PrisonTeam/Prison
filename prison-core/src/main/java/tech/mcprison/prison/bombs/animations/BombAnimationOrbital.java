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
	
	private Location oLoc = null;

	public BombAnimationOrbital(MineBombData bomb, PrisonBlock sBombBlock, 
			ItemStack item, BombAnimationsTask task,
			float entityYaw, float entityPitch) {
		super(bomb, sBombBlock, item, task, entityYaw, entityPitch);

	}
	
	@Override
	public void stepAnimation() {

		angle += 25;
		
		if ( angle > 360 ) {
			angle -= 360;
		}
		
		if ( oLoc == null ) {
			oLoc = new Location( getArmorStand().getLocation() );
		}

		Vector vector = GeometricShapes.getPointsOnCircleXZ( angle, radius );
		Location newLoc = new Location( oLoc ).add(vector);
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

}
