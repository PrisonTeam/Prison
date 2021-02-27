package tech.mcprison.prison.ranks.top;

public class RankPlayerBalance {
	
	/**
	 * <p>This is the refresh interval that if exceeded, will indicate that
	 * the currency should be refreshed.  This prevents the currency from 
	 * being updated too frequently.
	 * </p> 
	 * 
	 * <p>If this is being updated through mining autosell or sellall, then 
	 * the use of addBalance() will prevent the need to update from the
	 * economy plugin.  That's one reason why the refresh interval can 
	 * be so long.
	 * </p>
	 */
	public static final long REFRESH_INTERVAL_MS = 60000; // 60 seconds
	
	/**
	 * <p>The default currency is usually represented within Rank objects
	 * as either null (should be null) or an empty String.  The keys for
	 * TreeMaps cannot be null, so this provides an internal representation
	 * for just for this class and the collection that stores it.  It has
	 * no meaning outside of this localized use of it.
	 * </p>
	 */
	public static final String DEFAULT_CURRENCY = "-default-"; 

	private String currency;
	private double balance;
	
	private long timestamp;
	
	public RankPlayerBalance( String currency ) {
		super();
		
		this.currency = currency;
		this.balance = 0.0d;
		
		// setup timestamp so isFreshBalance() will return true right away:
		this.timestamp = System.currentTimeMillis() - REFRESH_INTERVAL_MS - 1000;
	}
	
	public RankPlayerBalance( String currency, double balance ) {
		this( currency );
		
		this.balance = balance;
		
		this.timestamp = System.currentTimeMillis();
	}

	public String getCurrency() {
		return currency;
	}
	public void setCurrency( String currency ) {
		this.currency = currency;
	}

	public boolean isRefreshBalance() {
		long ts = System.currentTimeMillis();
		
		return ( ts - REFRESH_INTERVAL_MS > getTimestamp() );
	}
	
	/**
	 * <p>This adds the amount to the balance of the player.  If 
	 * @param amount
	 * @return
	 */
	public double addBalance( double amount ) {
		double newBalance = getBalance() + amount;
		
		setBalance( newBalance );
		
		return newBalance;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance( double balance ) {
		
		// Set current timestamp to mark update:
		setTimestamp( System.currentTimeMillis() );
		
		this.balance = balance;
	}

	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp( long timestamp ) {
		this.timestamp = timestamp;
	}
	
}
