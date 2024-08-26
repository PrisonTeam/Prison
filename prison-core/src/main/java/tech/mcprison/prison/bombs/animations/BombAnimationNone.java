package tech.mcprison.prison.bombs.animations;

import tech.mcprison.prison.bombs.MineBombData;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.util.Location;

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
								Location location,
								PrisonBlock sBombBlock,
								BombAnimationsTask task ) {
		super( bomb, location, sBombBlock, task );
			
	}
	
	
	public BombAnimationNone( MineBombData bomb, 
			Location location,
			PrisonBlock sBombBlock, ItemStack item,
			BombAnimationsTask task,
			float entityYaw, float entityPitch ) {
		super( bomb, location, sBombBlock, item, task, entityYaw, entityPitch );
		
	}
	

	/**
	 * Since this animation is "none", there is no movement to this one.
	 * 
	 * This should be used for displaying the custom name.
	 */
	@Override
	public void stepAnimation()
	{
		
	}

}
