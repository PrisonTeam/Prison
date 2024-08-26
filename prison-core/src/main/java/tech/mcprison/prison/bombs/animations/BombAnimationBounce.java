package tech.mcprison.prison.bombs.animations;

import tech.mcprison.prison.bombs.MineBombData;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.util.Location;

public class BombAnimationBounce 
		extends BombAnimations {

	private double yOriginal = 0;
	private double counter = 0;
	
	public BombAnimationBounce(MineBombData bomb, 
			Location location,
			PrisonBlock sBombBlock,
			ItemStack item, BombAnimationsTask task,
			float entityYaw, float entityPitch) {
		super(bomb, location, sBombBlock, item, task, entityYaw, entityPitch);

		this.yOriginal = getArmorStand().getLocation().getY();
	}

	@Override
	public void stepAnimation() {

		double speed = getBomb().getAnimationSpeed() / 16d;

		counter += speed;
		
		if ( counter > 128d ) {
			counter -= 128d;
		}
		
		double bounce = 1.25 * Math.sin( counter );
		
		Location loc = new Location( getOriginalLocation() );
		
		// NOTICE!!!  Have to subtract 1 from the original location so the armorstand is 
		//            always TP'd to the same y value.  Otherwise it will bounce when it
		//            hits the ground.  This probably only affects spigot 1.8.x.
		//            This has something to do with the fact that the armorstand is being
		//            teleported.
		loc.setY( loc.getY() - 1 );
		
		loc.setY( yOriginal + bounce );
		
		getArmorStand().teleport( loc );
		
		
//		getArmorStand().getLocation().setY( yOriginal + bounce );
	}

}
