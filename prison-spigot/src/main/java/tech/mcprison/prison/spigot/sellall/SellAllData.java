package tech.mcprison.prison.spigot.sellall;

import java.text.DecimalFormat;
import java.util.List;

import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.game.SpigotPlayer;

/**
 * <p>This is a simple class that contains the times that have been sold 
 * and the amount that they were sold for.
 * </p>
 * 
 * @author Blue
 *
 */
public class SellAllData {
	
	private PrisonBlock prisonBlock;
	private int quantity;
	private double transactionAmount;
	private boolean itemsSold;

	public SellAllData( PrisonBlock pBlock, int quantity, double transactionAmount ) {
		super();
		
		this.itemsSold = false;

		this.prisonBlock = pBlock;
		this.quantity = quantity;
		this.transactionAmount = transactionAmount;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		DecimalFormat dFmt = new DecimalFormat( "#,##0.00" );
		sb.append( getPrisonBlock().getBlockName() )
			.append( ":" )
			.append( getQuantity() )
			.append( ":" )
			.append( dFmt.format(getTransactionAmount()) );
		
		if ( isItemsSold() ) {
			sb.append( ":SOLD" );
		}
		
		return sb.toString();
	}
	
	
    public static void debugItemsSold( List<SellAllData> soldItems, SpigotPlayer sPlayer, double multiplier ) {
        if ( Output.get().isDebug() ) {
        	String report = SellAllData.itemsSoldReport( soldItems, sPlayer, multiplier );
        	Output.get().logInfo( report );
        }
    }

    public static String itemsSoldReport( List<SellAllData> soldItems, SpigotPlayer sPlayer, double multiplier ) {
    	StringBuilder sb = new StringBuilder();
    	StringBuilder sbItems = new StringBuilder();
    	
    	double totalAmount = 0;
    	int itemCount = 0;
    	for (SellAllData soldItem : soldItems) {
    		if ( soldItem != null ) {
    			
    			totalAmount += soldItem.getTransactionAmount();
    			itemCount += soldItem.getQuantity();
    			
    			if ( sbItems.length() > 0 ) {
    				
    				sbItems.append( ", " );
    			}
    			sbItems.append( soldItem.toString() );
    		}
    	}
    	
    	DecimalFormat dFmt = new DecimalFormat( "#,##0.00" );
    	DecimalFormat iFmt = new DecimalFormat( "#,##0" );
    	
    	sb.append( "Transaction log: " )
    		.append( sPlayer.getName() )
    		.append( " multiplier: " )
    		.append( dFmt.format(multiplier) )
    		.append( " ItemStacks: " )
    		.append( soldItems.size() )
    		.append( " ItemCount: " )
    		.append( iFmt.format(itemCount) )
    		.append( " TotalAmount: " )
    		.append( dFmt.format( totalAmount ))
    		.append( " [" )
    		.append( sbItems )
    		.append("]");
    	
    	return sb.toString();
	}
	
	public PrisonBlock getPrisonBlock() {
		return prisonBlock;
	}
	public void setPrisonBlock(PrisonBlock prisonBlock) {
		this.prisonBlock = prisonBlock;
	}

	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public boolean isItemsSold() {
		return itemsSold;
	}
	public void setItemsSold(boolean itemsSold) {
		this.itemsSold = itemsSold;
	}
	
}
