package tech.mcprison.prison.spigot.compat;

/**
 * <p>This class is used to store test results of block tests.
 * Currently only bukkit (spigot) are using this, but could
 * be extended and used by other tests.
 * </p>
 * 
 * <p>The field maxData was used with the intentions of capturing the
 * highest magic number that could be used with a material.  That magic
 * number would have to be used on an ItemStack to get different variations,
 * but unfortunately, this failed as a test since any value could be used
 * since it appears to be the client that has the actual mapping to valid
 * types.
 * The magic numbers related data is not included here since there is 
 * no way to generate an ItemStack with an arbitrary data value and 
 * test that it's valid.
 * </p>
 *
 */
public class BlockTestStats {

	private int countItems = 0;
	private int countBlocks = 0;
	
	private int materialSize = 0;
	
	private short maxData = 0;
	
	public void addCountItems() {
		countItems++;
	}
	public void addCountBlocks() {
		countBlocks++;
	}
	public void addMaxData( short data ) {
		if ( data > maxData ) {
			maxData = data;
		}
	}
	
	@Override
	public String toString() {
		return "Materials = " + getMaterialSize() + 
				" countBlocks = " + getCountBlocks() +
				" countItems = " + getCountItems();
		// The magic numbers related data is not included here since there is
		// no way to generate an ItemStack with an arbitrary data value and
		// test that it's valid.
		//		+ " max data = " + getMaxData();
	}
	
	public int getCountItems()
	{
		return countItems;
	}
	public void setCountItems( int countItems )
	{
		this.countItems = countItems;
	}
	
	public int getCountBlocks()
	{
		return countBlocks;
	}
	public void setCountBlocks( int countBlocks )
	{
		this.countBlocks = countBlocks;
	}

	public int getMaterialSize()
	{
		return materialSize;
	}
	public void setMaterialSize( int materialSize )
	{
		this.materialSize = materialSize;
	}
	
	public short getMaxData()
	{
		return maxData;
	}
	public void setMaxData( short maxData )
	{
		this.maxData = maxData;
	}
	
}
