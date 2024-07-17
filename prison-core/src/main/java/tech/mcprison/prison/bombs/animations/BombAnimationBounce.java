package tech.mcprison.prison.bombs.animations;

import tech.mcprison.prison.bombs.MineBombData;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.util.Location;

public class BombAnimationBounce 
		extends BombAnimations {

	private double yOriginal = 0;
	private double counter = 0;
	
	public BombAnimationBounce(MineBombData bomb, PrisonBlock sBombBlock, 
			ItemStack item, BombAnimationsTask task,
			float entityYaw, float entityPitch) {
		super(bomb, sBombBlock, item, task, entityYaw, entityPitch);

		this.yOriginal = getArmorStand().getLocation().getY();
	}

	@Override
	public void stepAnimation() {

		double speed = 0.35;

		counter += speed;
		
		if ( counter > 128d ) {
			counter -= 128d;
		}
		
		double bounce = 1.25 * Math.sin( counter );
		
		Location loc = new Location( getArmorStand().getLocation() );
		loc.setY( yOriginal + bounce );
		
		getArmorStand().teleport( loc );
		
//		getArmorStand().getLocation().setY( yOriginal + bounce );
	}

}
