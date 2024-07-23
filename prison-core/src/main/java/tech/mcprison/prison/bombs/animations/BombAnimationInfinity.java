package tech.mcprison.prison.bombs.animations;

import tech.mcprison.prison.bombs.MineBombData;
import tech.mcprison.prison.internal.EulerAngle;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.block.PrisonBlock;

public class BombAnimationInfinity
		extends BombAnimations {

	public BombAnimationInfinity( MineBombData bomb, 
									PrisonBlock sBombBlock, ItemStack item,
									BombAnimationsTask task,
									float entityYaw, float entityPitch ) {
		super( bomb, sBombBlock, item, task, entityYaw, entityPitch );
			
	}


	@Override
	public void stepAnimation()
	{
	
		double speed = getBomb().getAnimationSpeed() / 16d;
		
		setEulerAngleX( getEulerAngleX() + speed );
		setEulerAngleY( getEulerAngleY() + speed / 3);
		setEulerAngleZ( getEulerAngleZ() + speed / 5 );
		
		
		EulerAngle arm = new EulerAngle( getEulerAngleX(), getEulerAngleY(), getEulerAngleZ() );
		
		getArmorStand().setRightArmPose(arm);
	}

}
