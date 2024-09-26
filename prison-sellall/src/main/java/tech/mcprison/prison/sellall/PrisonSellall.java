package tech.mcprison.prison.sellall;

import java.util.List;

import tech.mcprison.prison.localization.LocaleManager;
import tech.mcprison.prison.output.LogLevel;
import tech.mcprison.prison.output.Output;

public class PrisonSellall
	extends PrisonSellallMessages
{
	public static final String MODULE_NAME = "Sellall";
	
	private static PrisonSellall instance;
	
	private List<String> prisonStartupDetails;
	
    private LocaleManager localeManager;
	
	public PrisonSellall(String version) {
		super(MODULE_NAME, version, 3);
	}
	

    @Override
    public String getBaseCommands() {
    	return "/sellall";
    }
    
    /*
     * Methods
     */

    public static PrisonSellall getInstance() {
        return instance;
    }
    
    @Override 
    public void enable() {
        instance = this;

        this.localeManager = new LocaleManager(this, "lang/sellall");
        
    }
    

    /**
     * This function deferredStartup() will be called after the integrations have been
     * loaded.  
     * 
     */
    @Override
	public void deferredStartup() {
	}

    /**
     * <p>Do not save ranks upon server shutdown or plugin disable events.  The 
     * ranks should be saved every time there is a modification to them.
     * </p>
     */
    @Override 
    public void disable() {
    }
    
    

    private void logStartupMessageError( String message ) {
    	logStartupMessage( LogLevel.ERROR, message );
    
    }

    private void logStartupMessage( String message ) {
    	logStartupMessage( LogLevel.INFO, message );
    	
    }
    private void logStartupMessage( LogLevel logLevel, String message ) {
    	
    	Output.get().log( message, logLevel );
    	
    	getPrisonStartupDetails().add( message );
    }
    
    public List<String> getPrisonStartupDetails() {
    	return prisonStartupDetails;
    }
    public void setPrisonStartupDetails( List<String> prisonStartupDetails ){
    	this.prisonStartupDetails = prisonStartupDetails;
    }

    public LocaleManager getSellallMessages() {
        return localeManager;
    }
    
    /**
     * For modules that have elements, this will return the count.  If a module has no
     * elements, then it will return a -1.  Otherwise a zero would indicate that a module
     * should have elements, but it currently has none.
     * 
     * Example would be ranks and mines.  For these, if it returns a zero, then they have 
     * no ranks or mines defined.  If it return a -1 then the module is not active.
     * 
     * @return
     */
    public int getElementCount() {
    	int results = isEnabled() ? 0 : -1;
    	
    	// May need to hook in to the number of shop items... but that currently is not available through this module.
//    	if ( isEnabled() ) {
//    		results = get;
//    	}
    	
    	return results;
    }
    
}
