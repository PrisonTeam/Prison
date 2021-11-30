package tech.mcprison.prison.util;

public class ConversionUtil
{

    public static int doubleToInt(Object d) {
        return Math.toIntExact(Math.round((double) d));
    }
    
    public static long doubleToLong(Object d) {
    	return Math.round((double) d);
    }
    
}
