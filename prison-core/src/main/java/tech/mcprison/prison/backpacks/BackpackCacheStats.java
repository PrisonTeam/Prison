package tech.mcprison.prison.backpacks;

public class BackpackCacheStats {

	private long startMs;
	
	private boolean enabled = false;
	
	private int loadBackpack = 0;
	private int unloadBackpack = 0;
	private int removeBackpack = 0;
	private int getBackpack = 0;

	private int submitDatabaseUpdate = 0;
	private int synchronizeBackpacks = 0;
	
	
	private Object lock1 = new Object();
	private Object lock2 = new Object();
	private Object lock3 = new Object();
	private Object lock4 = new Object();
	private Object lock5 = new Object();
	private Object lock6 = new Object();
	
	public BackpackCacheStats() {
		super();
		
		this.startMs = System.currentTimeMillis();
	}
	
	
	/**
	 * <p>If enabled, stats will be collected.
	 * </p>
	 * 
	 * @return
	 */
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled( boolean enabled ) {
		this.enabled = enabled;
	}
	public void toggleEnabled() {
		this.enabled = !this.enabled;
	}

	public String displayStats() {
		StringBuilder sb = new StringBuilder();
		
		sb.append( "BackpackCache stats: loadPlayer=" ).append( getLoadBackpack() )
			.append( " unloadPlayer=" ).append( getUnloadBackpack() )
			.append( " removePlayer=" ).append( getRemoveBackpack() )
			.append( " getPlayer=" ).append( getGetBackpack() )
			
			.append( " submitDatabaseUpdate=" ).append( getSubmitDatabaseUpdate() )
			.append( " synchronizeDatabase=" ).append( getSynchronizeBackpacks() )
			
			;
		
		return sb.toString();
	}

	
	public void incrementGetBackpacks() {
		if ( enabled ) {
			synchronized ( lock1 ) {
				getBackpack++;
			}
		}
	}
	public void incrementRemoveBackpacks() {
		if ( enabled ) {
			synchronized ( lock2 ) {
				removeBackpack++;
			}
		}
	}
	public void incrementLoadBackpacks() {
		if ( enabled ) {
			synchronized ( lock3 ) {
				loadBackpack++;
			}
		}
	}
	public void incrementUnloadBackpacks() {
		if ( enabled ) {
			synchronized ( lock4 ) {
				unloadBackpack++;
			}
		}
	}
	
	
	public void incrementSubmitDatabaseUpdate() {
		if ( enabled ) {
			synchronized ( lock5 ) {
				submitDatabaseUpdate++;
			}
		}
	}
	public void incrementSubmitSynchronizeBackpacks() {
		if ( enabled ) {
			synchronized ( lock6 ) {
				synchronizeBackpacks++;
			}
		}
	}
	


	public long getStartMs() {
		return startMs;
	}
	public void setStartMs(long startMs) {
		this.startMs = startMs;
	}

	public int getLoadBackpack() {
		return loadBackpack;
	}
	public void setLoadBackpack(int loadBackpack) {
		this.loadBackpack = loadBackpack;
	}

	public int getUnloadBackpack() {
		return unloadBackpack;
	}
	public void setUnloadBackpack(int unloadBackpack) {
		this.unloadBackpack = unloadBackpack;
	}

	public int getRemoveBackpack() {
		return removeBackpack;
	}
	public void setRemoveBackpack(int removeBackpack) {
		this.removeBackpack = removeBackpack;
	}

	public int getGetBackpack() {
		return getBackpack;
	}
	public void setGetBackpack(int getBackpack) {
		this.getBackpack = getBackpack;
	}

	public int getSubmitDatabaseUpdate() {
		return submitDatabaseUpdate;
	}
	public void setSubmitDatabaseUpdate(int submitDatabaseUpdate) {
		this.submitDatabaseUpdate = submitDatabaseUpdate;
	}

	public int getSynchronizeBackpacks() {
		return synchronizeBackpacks;
	}
	public void setSynchronizeBackpack(int synchronizeBackpacks) {
		this.synchronizeBackpacks = synchronizeBackpacks;
	}

}
