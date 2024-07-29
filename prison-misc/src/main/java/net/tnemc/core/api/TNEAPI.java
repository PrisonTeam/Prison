package net.tnemc.core.api;

import java.math.BigDecimal;

public class TNEAPI {

	public boolean hasPlayerAccount( String playerUUID ) {
		return true;
	}
	
	public boolean hasHoldings( String playerUUID, String worldName, 
			String currency, BigDecimal amount ) {
		return true;
	}
	
	
	public boolean setHoldings( String playerUUID, String worldName, 
			String currency, BigDecimal amount ) {
		return true;
	}
	
	
	public BigDecimal getHoldings( String playerUUID, String worldName, 
			String currency ) {
		return BigDecimal.ZERO;
	}
	
	
	public TNEAPI getDefaultCurrency() {
		return this;
	}

	public String getIdentifier() {
		return "";
	}


	public TNEAPI addHoldings( String playerUUID, String worldName, 
			String currency, BigDecimal amount, String source ) {
		return this;
	}
	
	
	public TNEAPI removeHoldings( String playerUUID, String worldName, 
			String currency, BigDecimal amount, String source ) {
		return this;
	}
		
	public boolean isSuccessful() {
		return true;
	}
	
}
