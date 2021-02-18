package tech.mcprison.prison.internal.block;

public abstract class PrisonBlockStatusData {

	// blockName is more of an internal reference:
	private String blockName;
	
	private double chance;
	
	private int contraintMin;
	private int contraintMax;

	
	private int blockCountOnReset;
	
	private long blockCountTotal;
	private long blockCountSession;
	private long blockCountUnsaved;
	
	
	
	public PrisonBlockStatusData( String blockName, double chance, long blockCountTotal ) {
		super();
		
		this.blockName = blockName;
		
		this.chance = chance;
		
		this.contraintMin = 0;
		this.contraintMax = 0;

		
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
	
	public void incrementMiningBlockCount() {
		blockCountTotal++;
		blockCountSession++;
		blockCountUnsaved++;
	}
	
	public String toSaveFileFormat() {
		return getBlockName() + "-" + getChance() + "-" + getBlockCountTotal() + "-" + 
					getContraintMin() + "-" + getContraintMax();
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

	public int getContraintMin() {
		return contraintMin;
	}
	public void setContraintMin( int contraintMin ) {
		this.contraintMin = contraintMin;
	}

	public int getContraintMax() {
		return contraintMax;
	}
	public void setContraintMax( int contraintMax ) {
		this.contraintMax = contraintMax;
	}

}
