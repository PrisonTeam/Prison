package tech.mcprison.prison.mines;

import tech.mcprison.prison.convert.ConversionAgent;
import tech.mcprison.prison.convert.ConversionResult;
import tech.mcprison.prison.output.Output;

/**
 * @author Faizaan A. Datoo
 */
public class MinesConversionAgent 
implements ConversionAgent 
{

    @Override
    public ConversionResult convert() {
    	
	    	Output.get().logWarn( "&7This version of prison is unable to convert older versions " +
	    			"to this release.  Please upgrade first to Prision v3.1.1, then " +
	    			"Prison v3.2.1, then Prison v3.2.11. Once prison is upgraded to " +
	    			"version v3.2.11 then it should be able to convert automatically to " +
	    			"Prison v3.3.0. When upgrading to all of these versions, all that is needed " +
	    			"is to just install the newer Prison jar file, and then start the server. " +
	    			"The configs will be converted for you.  Then 'stop' the server and " +
	    			"continue to the next version.  You may be able to skip v3.2.1, but it " +
	    			"may be safest to run that version for the incremental adjustments. " +
	    			"It also would not be a bad idea to may a copy of the prison plugin " +
	    			"directory betwen version upgrades: plugins/Prison/. " );
	    	
	    	Output.get().logWarn( "&7NOTE: This version of prison cannot process the older block types that " +
	    			"were used in the older versions of prison." );
	    	
	    	
	    	 return ConversionResult.failure(getName(),
	               "This version of prison cannot perform conversion upgrades. It skips too " +
	               "many versions. See WARNING in console, and install the suggested older " +
	               "versions of Prison to ensure all of the old data is updated correctly, " +
	               "with no losses.");
    }

    @Override
    public String getName() {
        return "Mines";
    }
}
