package tech.mcprison.prison.util;

public class ConversionUtil
{

    public static int doubleToInt(Object d) {
        return d == null ? -1 : Math.toIntExact(Math.round((double) d));
    }
    
    public static long doubleToLong(Object d) {
    	return d == null ? -1 : Math.round((double) d);
    }
    
}
