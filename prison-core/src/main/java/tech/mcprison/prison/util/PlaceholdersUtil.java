package tech.mcprison.prison.util;

import java.text.DecimalFormat;

public class PlaceholdersUtil {

	public static final double TIME_SECOND = 1.0;
	public static final double TIME_MINUTE = TIME_SECOND * 60.0;
	public static final double TIME_HOUR = TIME_MINUTE * 60.0;
	public static final double TIME_DAY = TIME_HOUR * 24.0;
	
	
	public static String formattedTime( double time ) {
    	StringBuilder sb = new StringBuilder();
    	
    	long days = (long)(time / TIME_DAY);
    	time -= (days * TIME_DAY);
    	if ( days > 0 ) {
    		sb.append( days );
    		sb.append( "d " );
    	}
    	
    	long hours = (long)(time / TIME_HOUR);
    	time -= (hours * TIME_HOUR);
    	if ( sb.length() > 0 || hours > 0 ) {
    		sb.append( hours );
    		sb.append( "h " );
    	}
    	
    	long mins = (long)(time / TIME_MINUTE);
    	time -= (mins * TIME_MINUTE);
    	if ( sb.length() > 0 || mins > 0 ) {
    		sb.append( mins );
    		sb.append( "m " );
    	}
    	
    	double secs = (double)(time / TIME_SECOND);
    	time -= (secs * TIME_SECOND);
    	DecimalFormat dFmt = new DecimalFormat("#0");
    	sb.append( dFmt.format( secs ));
    	sb.append( "s " );
    	
		return sb.toString();
	}
	
	/**
	 * This is a simple a simple way to reduce down large numbers and apply a single digit
	 * suffix based upon metric prefixes.
	 * 
	 * "", k, M, G, T, P, E, Z, Y
	 * 
	 * Using: https://en.wikipedia.org/wiki/Metric_prefix
	 * 
	 * @param amount
	 * @return
	 */
	public static String formattedSize( double amount ) {
    	StringBuilder unit = new StringBuilder();
    	
    	DecimalFormat dFmt = new DecimalFormat("#,##0.00");
    	
    	amount = divBy1000( amount, unit, " kMGTPEZY" );
    	
    	String results = dFmt.format( amount ) + " " + unit.toString();

		return results.trim();
	}
	
	private static double divBy1000( double amount, StringBuilder unit, String units ) {
    	if ( amount <= 1000.0 || units.length() == 1 ) {
    		unit.append( units.subSequence( 0, 1 ) );
    	}
    	else {
    		// Div amount by 1000.0 and remove the first character of the units:
    		amount /= 1000.0;
    		units = units.substring( 1 );
    		amount = divBy1000( amount, unit, units );
    	}
    	return amount;
	}
}
