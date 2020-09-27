package tech.mcprison.prison.mines.data;

import tech.mcprison.prison.util.BlockType;

public class MineTargetBlock
{
	private MineTargetBlockKey blockKey;
	
	private BlockType blockType;
	
	public MineTargetBlock( BlockType blockType, int x, int y, int z ) {
		super();
		
		this.blockKey = new MineTargetBlockKey( x, y, z );
		this.blockType = blockType;
	}

	public BlockType getBlockType()
	{
		return blockType;
	}
	public void setBlockType( BlockType blockType )
	{
		this.blockType = blockType;
	}

	public MineTargetBlockKey getBlockKey()
	{
		return blockKey;
	}
	public void setBlockKey( MineTargetBlockKey blockKey )
	{
		this.blockKey = blockKey;
	}
}
