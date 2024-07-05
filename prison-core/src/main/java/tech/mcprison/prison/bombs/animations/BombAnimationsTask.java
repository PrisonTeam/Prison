package tech.mcprison.prison.bombs.animations;

import java.util.ArrayList;
import java.util.List;

import tech.mcprison.prison.bombs.MineBombData;
import tech.mcprison.prison.bombs.MineBombs.AnimationPattern;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.tasks.PrisonRunnable;
import tech.mcprison.prison.tasks.PrisonTaskSubmitter;

public class BombAnimationsTask
		implements PrisonRunnable {

	private int taskId;
	
	private List<BombAnimations> animators;
	
	public BombAnimationsTask() {
		super();
		
		this.animators = new ArrayList<>();
	}
	
	
	public void animatorFactory( 
			// AnimationPattern animation, 
			MineBombData bomb, PrisonBlock pBlock, ItemStack item ) {
		
		AnimationPattern animation = bomb.getAnimationPattern();
		
		
		
		if ( Output.get().isDebug() ) {
			String msg = String.format( 
					"### BombAnimationsTask.animmatorFactory : AnimationPattern: %s ", 
					animation.name()
					
					);
			Output.get().logInfo( msg );
		}
		
		
		switch ( animation ) {
		
		case bounce:
			
			break;
			
		case infinity:
		{
			float yaw = 0;
			float pitch = 0;
			
			BombAnimationInfinity bai = new BombAnimationInfinity( bomb, 
					pBlock, item, this, 
					yaw, pitch );
			
			getAnimators().add( bai );
			submitTask();
		}
			
			break;
			
		case infinityEight:
		{
			float yaw = 0;
			float pitch = 0;
			float yawStep = 360f / 8f;
			
			for ( int i = 0; i < 8; i++ ) {
				BombAnimationInfinity bai = new BombAnimationInfinity( bomb, 
						pBlock, item, this, 
						yaw, pitch );
				
				getAnimators().add( bai );
				
				yaw += yawStep;
			}
			
			submitTask();
		}
			
			break;
			
			
		case none:
		default:
			// Do nothing... do not submit task to run.
			break;
		}
	}
	
	private void submitTask() {
		
		setTaskId( PrisonTaskSubmitter.runTaskTimer( this, 0L, 1L ) );
		
	}

	protected void cancel() {
		PrisonTaskSubmitter.cancelTask( getTaskId() );
	}
	

	/**
	 * The animators will override this function.
	 */
	protected void initialize() {
	}
	
	
	@Override
	public void run() {
		
		for ( BombAnimations animator : getAnimators() ) {
			animator.step();
		}
		
	}


	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public List<BombAnimations> getAnimators() {
		return animators;
	}
	public void setAnimators(List<BombAnimations> animators) {
		this.animators = animators;
	}
	
}
