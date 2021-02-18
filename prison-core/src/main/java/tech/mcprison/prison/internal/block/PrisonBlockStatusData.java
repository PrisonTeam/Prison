package tech.mcprison.prison.internal.block;

import tech.mcprison.prison.Prison;

public abstract class PrisonBlockStatusData {

	// blockName is more of an internal reference:
	private String blockName;
	
	private double chance;
	
	private int constraintMin;
	private int constraintMax;

	private int constraintExcludeTopLayers;
	private int constraintExcludeBottomLayers;
	
	private int blockCountOnReset;
	
	private long blockCountTotal;
	private long blockCountSession;
	private long blockCountUnsaved;
	
	
	
	public PrisonBlockStatusData( String blockName, double chance, long blockCountTotal ) {
		super();
		
		this.blockName = blockName;
		
		this.chance = chance;
		
		this.constraintMin = 0;
		this.constraintMax = 0;

		
		this.blockCountOnReset = 0;
		
		this.blockCountTotal = blockCountTotal;
		this.blockCountSession = 0;
		this.blockCountUnsaved = 0;
	}
	
	public void resetAfterSave() {
		blockCountUnsaved = 0;
	}

	public void incrementResetBlockCount() {
		blockCountOnReset++;
	}
	
	public void decrementResetBlockCount() {
		blockCountOnReset--;
	}
	
	public void incrementMiningBlockCount() {
		blockCountTotal++;
		blockCountSession++;
		blockCountUnsaved++;
	}
	
	public String toSaveFileFormat() {
		return getBlockName() + "-" + getChance() + "-" + getBlockCountTotal() + "-" + 
					getConstraintMin() + "-" + getConstraintMax();
	}
	
	
	public static PrisonBlock parseFromSaveFileFormat( String blockString ) {
		
		PrisonBlock results = null;
		
		String[] split = blockString.split("-");
		String blockTypeName = split[0];
		if ( blockTypeName != null ) {
			// The new way to get the PrisonBlocks:  
			//   The blocks return are cloned so they have their own instance:
			results = Prison.get().getPlatform().getPrisonBlock( blockTypeName );
			
			if ( results != null ) {
				
				double chance = split.length > 1 ? Double.parseDouble(split[1]) : 0;
				long blockCount = split.length > 2 ? Long.parseLong(split[2]) : 0;
				int constraintMin = split.length > 3 ? Integer.parseInt(split[3]) : 0;
				int constraintMax = split.length > 4 ? Integer.parseInt(split[4]) : 0;
				int constraintExcludeTopLayers = split.length > 5 ? Integer.parseInt(split[5]) : 0;
				int constraintExcludeBottomLayers = split.length > 6 ? Integer.parseInt(split[6]) : 0;
				
				results.setChance( chance );
				results.setBlockCountTotal( blockCount );
				results.setConstraintMin( constraintMin );
				results.setConstraintMax( constraintMax );
				results.setConstraintExcludeTopLayers( constraintExcludeTopLayers );
				results.setConstraintExcludeBottomLayers( constraintExcludeBottomLayers );
			}
		}
		
		return results;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append( getBlockName() ).append( "  chance= " ).append( getChance() )
					.append( "%" );
		
		return sb.toString();
	}
	
	public abstract boolean isAir();
	
	
	public String getBlockName(){
		return blockName;
	}
	public void setBlockName( String blockName ) {
		this.blockName = blockName;
	}

	public double getChance() {
		return chance;
	}
	public void setChance( double chance ) {
		this.chance = chance;
	}

	public int getResetBlockCount() {
		return blockCountOnReset;
	}
	public void setResetBlockCount( int resetBlockCount ) {
		this.blockCountOnReset = resetBlockCount;
	}

	public long getBlockCountTotal() {
		return blockCountTotal;
	}
	public void setBlockCountTotal( long blockCountTotal ) {
		this.blockCountTotal = blockCountTotal;
	}

	public long getBlockCountSession() {
		return blockCountSession;
	}
	public void setBlockCountSession( long blockCountSession ) {
		this.blockCountSession = blockCountSession;
	}

	public long getBlockCountUnsaved() {
		return blockCountUnsaved;
	}
	public void setBlockCountUnsaved( long blockCountUnsaved ) {
		this.blockCountUnsaved = blockCountUnsaved;
	}

	public int getConstraintMin() {
		return constraintMin;
	}
	public void setConstraintMin( int constraintMin ) {
		this.constraintMin = constraintMin;
	}

	public int getConstraintMax() {
		return constraintMax;
	}
	public void setConstraintMax( int constraintMax ) {
		this.constraintMax = constraintMax;
	}

	public int getConstraintExcludeTopLayers() {
		return constraintExcludeTopLayers;
	}
	public void setConstraintExcludeTopLayers( int constraintExcludeTopLayers ) {
		this.constraintExcludeTopLayers = constraintExcludeTopLayers;
	}

	public int getConstraintExcludeBottomLayers() {
		return constraintExcludeBottomLayers;
	}
	public void setConstraintExcludeBottomLayers( int constraintExcludeBottomLayers ) {
		this.constraintExcludeBottomLayers = constraintExcludeBottomLayers;
	}

}
