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
	private double radiusDelta = 0;
	
	private boolean alternateDirections = false;
	
	public BombAnimationOrbital(MineBombData bomb, Location location,
			PrisonBlock sBombBlock, 
			ItemStack item, BombAnimationsTask task,
			float entityYaw, float entityPitch) {
		super(bomb, location, sBombBlock, item, task, entityYaw, entityPitch);

		this.angle = entityYaw;

		
		Vector vec = new Vector( 
						bomb.getAnimationOffset(), 0d, bomb.getAnimationOffset());
		getOriginalLocation().add( vec );
		
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
		
		double radi = radius + 
				(radiusDelta == 0 ? 0 :
					radiusDelta * Math.sin( angle / 2d ));
		
		double spinVal = angle * -7d;
//		Vector spin = new Vector( spinVal, 0, 0 );
		
		
		Vector vector = GeometricShapes.getPointsOnCircleXZ( angle, radi );
//		Vector vector = GeometricShapes.getPointsOnCircleXZ( angle, radius );
		
		// Location.add() creates a new instance of a Location and does not change
		// the original value:
		Location newLoc = getOriginalLocation().add(vector);
		
		// NOTICE!!!  Have to subtract 1 from the original location so the armorstand is 
		//            always TP'd to the same y value.  Otherwise it will bounce when it
		//            hits the ground.  This probably only affects spigot 1.8.x.
		//            This has something to do with the fact that the armorstand is being
		//            teleported.
		newLoc.setY( newLoc.getY() - 1 );
		
		// Spin the held item?
		newLoc.setYaw( (float) spinVal );
//		newLoc.setDirection( spin );

		getArmorStand().teleport( newLoc );
		
		
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

	public double getRadiusDelta() {
		return radiusDelta;
	}
	public void setRadiusDelta(double radiusDelta) {
		this.radiusDelta = radiusDelta;
	}

	public boolean isAlternateDirections() {
		return alternateDirections;
	}
	public void setAlternateDirections(boolean alternateDirections) {
		this.alternateDirections = alternateDirections;
	}

}
