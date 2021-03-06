package tech.mcprison.prison.spigot.sellall;

import com.cryptomorin.xseries.XMaterial;

public class SellAllBlockData {

	private XMaterial block;
	private double price;

	private boolean primary = false;
	
	
	public SellAllBlockData( XMaterial block, double price, boolean primary ) {
		super();

		this.block = block;
		this.price = price;
		
		this.primary = primary;
	}

	public SellAllBlockData( XMaterial block, double price ) {
		this( block, price, false );
	}

	public XMaterial getBlock() {
		return block;
	}
	public void setBlock( XMaterial block ) {
		this.block = block;
	}

	public double getPrice() {
		return price;
	}
	public void setPrice( double price ) {
		this.price = price;
	}

	public boolean isPrimary() {
		return primary;
	}
	public void setPrimary( boolean primary ) {
		this.primary = primary;
	}
	
}
