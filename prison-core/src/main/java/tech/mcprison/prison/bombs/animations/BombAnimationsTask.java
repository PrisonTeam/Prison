package tech.mcprison.prison.bombs.animations;

import java.util.ArrayList;
import java.util.List;

import tech.mcprison.prison.bombs.MineBombData;
import tech.mcprison.prison.bombs.MineBombDetonateTask;
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
	
	private MineBombDetonateTask detonateBomb;
	
	private boolean detonated = false;
	private Object detonationLock;
	
	public BombAnimationsTask() {
		super();
		
		this.detonationLock = new Object();
		
		this.animators = new ArrayList<>();
	}
	
	
	public void animatorFactory( 
			// AnimationPattern animation, 
			MineBombData bomb, PrisonBlock pBlock, 
			ItemStack sItem, MineBombDetonateTask detonateBomb ) {
		
		this.detonateBomb = detonateBomb;
		
		ItemStack item = new ItemStack( sItem );
		
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
		{
			float yaw = 0;
			float pitch = 0;
			
			BombAnimationBounce ba = new BombAnimationBounce( bomb, 
					pBlock, item, this, 
					yaw, pitch );
			
			getAnimators().add( ba );
			submitTask();
		}
			
			break;
			
		case orbital:
		{
			float yaw = 0;
			float pitch = 0;
			
			BombAnimationOrbital ba = new BombAnimationOrbital( bomb, 
					pBlock, item, this, 
					yaw, pitch );
			
			getAnimators().add( ba );
			submitTask();
		}
		
		break;
		
		case orbitalEight:
		{
			float yaw = 0;
			float pitch = 0;
			float yawStep = 360f / 8f;
			
			for ( int i = 0; i < 8; i++ ) {
				BombAnimationOrbital ba = new BombAnimationOrbital( bomb, 
						pBlock, item, this, 
						yaw, pitch );
				
				getAnimators().add( ba );
				
				yaw += yawStep;
			}
			
			submitTask();
		}
		break;
			
		case starburst:
		{
			float yaw = 0;
			float pitch = 0;
			float yawStep = 360f / 16f;
			
			for ( int i = 0; i < 16; i++ ) {
				BombAnimationOrbital ba = new BombAnimationOrbital( bomb, 
						pBlock, item, this, 
						yaw, pitch );
				
				ba.setAlternateDirections( true );
				
				getAnimators().add( ba );
				
				yaw += yawStep;
			}
			
			submitTask();
		}
		break;
		
		case infinity:
		{
			float yaw = 0;
			float pitch = 0;
			
			BombAnimationInfinity ba = new BombAnimationInfinity( bomb, 
					pBlock, item, this, 
					yaw, pitch );
			
			getAnimators().add( ba );
			submitTask();
		}
			
			break;
			
		case infinityEight:
		{
			float yaw = 0;
			float pitch = 0;
			float yawStep = 360f / 8f;
			
			for ( int i = 0; i < 8; i++ ) {
				BombAnimationInfinity ba = new BombAnimationInfinity( bomb, 
						pBlock, item, this, 
						yaw, pitch );
				
				getAnimators().add( ba );
				
				yaw += yawStep;
			}
			
			submitTask();
		}
			
			break;
			
			
		case none:
		{
			float yaw = 0;
			float pitch = 0;
			
			BombAnimationNone ba = new BombAnimationNone( bomb, 
					pBlock, item, this, 
					yaw, pitch );
			
			getAnimators().add( ba );
			submitTask();
		}
		
		default:
			// Do nothing... do not submit task to run.
			break;
		}
		
		
		detonateBomb();
	}
	
	private void submitTask() {
		
		setTaskId( PrisonTaskSubmitter.runTaskTimer( this, 0L, 1L ) );
		
	}

	protected void cancel() {
		PrisonTaskSubmitter.cancelTask( getTaskId() );
	}
	
	private void detonateBomb() {
		
		// Need to  use a multi-layered synchronized lock since there may be
		// many animations ending at the same time, and we only want one to 
		// trigger the detonation.
		if ( !detonated ) {
			synchronized ( detonationLock ) {
				if ( !detonated ) {
					detonated = true;
					
					// Detonate the bomb using the callback:
					detonateBomb.runDetonation(); 
				}
			}
		}
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
