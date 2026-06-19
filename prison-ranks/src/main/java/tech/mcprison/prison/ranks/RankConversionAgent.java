package tech.mcprison.prison.ranks;

import tech.mcprison.prison.convert.ConversionAgent;
import tech.mcprison.prison.convert.ConversionResult;

/**
 * @author Faizaan A. Datoo
 */
public class RankConversionAgent implements ConversionAgent {

    @Override public ConversionResult convert() {
    	
	    	// NOTE this is obsolete:
	    	
	    	// commented out code was removed.  See history in git.
	
	    	return new ConversionResult("Rank Conversion Failure", ConversionResult.Status.Failure,
	               "To upgrade ranks to v3.1.1 format, please first upgrade to prison v3.1.1. " +
	               "Then upgrade to a newer version of prison.");
    }

    @Override public String getName() {
        return "Ranks";
    }

}
