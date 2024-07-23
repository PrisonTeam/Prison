package tech.mcprison.prison.bombs.animations;

import tech.mcprison.prison.bombs.MineBombData;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.block.PrisonBlock;

public class BombAnimationNone
		extends BombAnimations {

	/**
	 * This constructor will generate an armor stand without holding an item.
	 * This is ideal for a holographic display that is not moving.
	 * 
	 * @param bomb
	 * @param sBombBlock
	 * @param task
	 */
	public BombAnimationNone( MineBombData bomb, 
								PrisonBlock sBombBlock,
								BombAnimationsTask task ) {
		super( bomb, sBombBlock, task );
			
	}
	
	
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
