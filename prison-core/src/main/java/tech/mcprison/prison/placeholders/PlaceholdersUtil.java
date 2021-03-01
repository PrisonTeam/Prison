package tech.mcprison.prison.placeholders;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PlaceholdersUtil {

	public static final double TIME_SECOND = 1.0;
	public static final double TIME_MINUTE = TIME_SECOND * 60.0;
	public static final double TIME_HOUR = TIME_MINUTE * 60.0;
	public static final double TIME_DAY = TIME_HOUR * 24.0;
	
	
	public static final List<String> prefixesBinary = new ArrayList<>();
	
	static {
		prefixesBinary.add( "" );
		prefixesBinary.add( "KB" );
		prefixesBinary.add( "MB" );
		prefixesBinary.add( "GB" );
		prefixesBinary.add( "TB" );
		prefixesBinary.add( "PB" );
		prefixesBinary.add( "EB" );
		prefixesBinary.add( "ZB" );
		prefixesBinary.add( "YB" );
	}
	
	
	/**
	 * <p>Formats seconds to 0d 0h 0m 0s.
	 * Input is in seconds.
	 * </p>
	 * 
	 * @param timeSec Time in seconds.
	 * @return
	 */
	public static String formattedTime( double timeSec ) {
    	StringBuilder sb = new StringBuilder();
    	
    	long days = (long)(timeSec / TIME_DAY);
    	timeSec -= (days * TIME_DAY);
    	if ( days > 0 ) {
    		sb.append( days );
    		sb.append( "d " );
    	}
    	
    	long hours = (long)(timeSec / TIME_HOUR);
    	timeSec -= (hours * TIME_HOUR);
    	if ( sb.length() > 0 || hours > 0 ) {
    		sb.append( hours );
    		sb.append( "h " );
    	}
    	
    	long mins = (long)(timeSec / TIME_MINUTE);
    	timeSec -= (mins * TIME_MINUTE);
    	if ( sb.length() > 0 || mins > 0 ) {
    		sb.append( mins );
    		sb.append( "m " );
    	}
    	
    	double secs = (double)(timeSec / TIME_SECOND);
    	timeSec -= (secs * TIME_SECOND);
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
	public static String formattedMetricSISize( double amount ) { 
		
		DecimalFormat dFmt = new DecimalFormat("#,##0.00");
		return formattedMetricSISize( amount, dFmt, " " );
	}
	
	public static String formattedMetricSISize( double amount, DecimalFormat dFmt, String spaces  ) { 
    	StringBuilder unit = new StringBuilder();
    	
    	amount = divBy1000( amount, unit, " kMGTPEZY" );
    	
    	String results = dFmt.format( amount ) + spaces + unit.toString();

		return results.trim();
	}
	
	public static String formattedKmbtSISize( double amount, DecimalFormat dFmt, String spaces  ) { 
		StringBuilder unit = new StringBuilder();
		
		amount = divBy1000( amount, unit, " KMBTqQsS" );
		
		String results = dFmt.format( amount ) + spaces + unit.toString();
		
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
	
	
	public static String formattedPrefixBinarySize( double amount ) { 
		
		DecimalFormat dFmt = new DecimalFormat("#,##0.00");
		return formattedIPrefixBinarySize( amount, dFmt, " " );
	}
	
	public static String formattedIPrefixBinarySize( double amount, DecimalFormat dFmt, String spaces  ) { 
    	StringBuilder unit = new StringBuilder();
    	
    	amount = divBy1024( amount, unit, 0 );
    	
    	String results = dFmt.format( amount ) + spaces + unit.toString();

		return results.trim();
	}
	
	private static double divBy1024( double amount, StringBuilder unit, int prefixesBinaryPos ) {
    	if ( prefixesBinary.size() == 0) {
    		// no prefixesBinary units have been defined, so exit returning the original amount:
    	}
    	else if ( amount <= 1024.0 || prefixesBinary.size() == (prefixesBinaryPos + 1)) {
    		unit.append( prefixesBinary.get( prefixesBinaryPos ) );
    	}
    	else {
    		// Div amount by 1000.0 and then recursively call this function while adding one to pos:
    		amount /= 1024.0;
    		amount = divBy1024( amount, unit, prefixesBinaryPos + 1 );
    	}
    	return amount;
	}

}
