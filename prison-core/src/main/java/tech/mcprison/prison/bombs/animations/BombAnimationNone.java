package tech.mcprison.prison.bombs.animations;

import tech.mcprison.prison.bombs.MineBombData;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.block.PrisonBlock;

public class BombAnimationNone
		extends BombAnimations {

	public BombAnimationNone( MineBombData bomb, 
								PrisonBlock sBombBlock, ItemStack item,
								BombAnimationsTask task,
								float entityYaw, float entityPitch ) {
		super( bomb, sBombBlock, item, task, entityYaw, entityPitch );
			
	}
	

	@Override
	public void stepAnimation()
	{
		
	}

}
