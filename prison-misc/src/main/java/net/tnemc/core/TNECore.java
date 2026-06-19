package net.tnemc.core;

public class TNECore {


	public static TNECore eco() {
		return new TNECore();
	}

	public TNECore currency() {
		return this;
	}
	
	public TNECore findCurrency( String currency ) {
		return this;
	}
	
	public boolean isPresent() {
		return true;
	}
}
