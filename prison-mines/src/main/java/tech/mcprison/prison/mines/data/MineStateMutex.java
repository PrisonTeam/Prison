package tech.mcprison.prison.mines.data;

public class MineStateMutex
{
	private MineState mineState;
	private int mineStateSn = 0;
	
	private boolean cloned = false;

	public enum MineState {
		LOCKED,
		UNLOCKED;
	}
	
	public MineStateMutex() {
		
		this.mineState = MineState.UNLOCKED;
		
		this.mineStateSn = 0;
		
		this.cloned = false;
	}
	
	private MineStateMutex( MineStateMutex cloneSource ) {
		
		this.mineState = cloneSource.getMineState();
		
		this.mineStateSn = cloneSource.getMineStateSn();
		
		this.cloned = true;
	}
	
	public void setMineStateResetStart() {
		
		synchronized ( this ) {
			
			this.mineState = MineState.LOCKED;
			this.mineStateSn++;
		}
		
	}
	
	public void setMineStateResetFinished() {
		
		synchronized ( this ) {
			
			this.mineStateSn--;

			// If the state serial number is zero, then that means 
			// this can now be unlocked.  Otherwise if non-zero it
			// must remain locked.
			if ( mineStateSn == 0 ) {
				
				this.mineState = MineState.UNLOCKED;
			}
			
		}
		
	}
	
	public boolean isMinable() {
		boolean results = false;
		
		synchronized ( this ) {
			
			if ( getMineState() == MineState.UNLOCKED ) {
				
				results = true;
			}
		}
		
		return results;
	}
	
	public boolean isValidState( MineStateMutex mutex ) {
		boolean results = false;
		
		synchronized ( this ) {
			
			if ( getMineState() == MineState.UNLOCKED && 
					getMineStateSn() == mutex.getMineStateSn() ) {
				
				results = true;
			}
		}
		
		return results;
	}

	public MineStateMutex clone() {
		MineStateMutex results = new MineStateMutex( this );
		
		return results;
	}
	
	public MineState getMineState() {
		return mineState;
	}

	public int getMineStateSn() {
		return mineStateSn;
	}

	public boolean isCloned() {
		return cloned;
	}
	
}
