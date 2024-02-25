package tech.mcprison.prison.mines.commands;

import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.util.Text;

public class MinesCoreCommands
	extends MinesCommandMessages
{
	private Long confirmTimestamp;
	
	private String lastMineReferenced;
	private Long lastMineReferencedTimestamp;
	
	public MinesCoreCommands( String cmdGroup ) {
		super( cmdGroup );
	}
	
    protected boolean performCheckMineExists(CommandSender sender, String mineName) {
    	mineName = Text.stripColor( mineName );
        if (PrisonMines.getInstance().getMine(mineName) == null) {
            PrisonMines.getInstance().getMinesMessages().getLocalizable("mine_does_not_exist")
                .sendTo(sender);
            return false;
        }
        return true;
    }
    
	public Long getConfirmTimestamp()
	{
		return confirmTimestamp;
	}
	public void setConfirmTimestamp( Long confirmTimestamp )
	{
		this.confirmTimestamp = confirmTimestamp;
	}

	/**
	 * <p>This function will return the last mine reference to be used to fill in the
	 * <code>&lt;mine&gt;</code> reference within these commands.  After 30 minutes of 
	 * the last reference, this value will be reset to null and this function will then
	 * return the default mine place holder of <code>&lt;mine&gt;</code>.
	 * </p>
	 * 
	 * @return last mine referenced, or <code>&lt;mine&gt;</code>
	 */
	public String getLastMineReferenced()
	{
		if ( getLastMineReferencedTimestamp() != null &&
				System.currentTimeMillis() - getLastMineReferencedTimestamp() > (30 * 60 * 1000))
		{
			setLastMineReferenced( null );
		}
		return (lastMineReferenced == null ? "<mine>" : lastMineReferenced);
	}
	public void setLastMineReferenced( String lastMineReferenced )
	{
		lastMineReferenced( System.currentTimeMillis() );
		this.lastMineReferenced = lastMineReferenced;
	}

	public Long getLastMineReferencedTimestamp()
	{
		return lastMineReferencedTimestamp;
	}
	public void lastMineReferenced( Long lastMineReferencedTimestamp )
	{
		this.lastMineReferencedTimestamp = lastMineReferencedTimestamp;
	}
	
	public String formatStringPadRight( String text, int totalLength, Object... args ) {
		StringBuilder sb = new StringBuilder( String.format( text, args ));
		
		while ( sb.length() < totalLength ) {
			sb.append( " " );
		}
		
		return sb.toString();
	}

	
	protected String extractParameter( String key, String options ) {
		return extractParameter( key, options, true );
	}
	
	protected String extractParameter( String key, String options, boolean tryLowerCase ) {
		String results = null;
		int idx = options.indexOf( key );
		if ( idx != -1 ) {
			int idxEnd = options.indexOf( " ", idx );
			if ( idxEnd == -1 ) {
				idxEnd = options.length();
			}
			results = options.substring( idx, idxEnd );
		}
		else if ( tryLowerCase ) {
			// try again, but lowercase the key
			results = extractParameter( key.toLowerCase(), options, false );
		}
		return results;
	}
}
