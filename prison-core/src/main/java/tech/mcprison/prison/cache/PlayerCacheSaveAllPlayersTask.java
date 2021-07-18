package tech.mcprison.prison.cache;

/**
 * <p>This class provide periodical saving of all player data.  
 * This ensures that it's at least stored every once in a while. 
 * The data is only saved if it is marked as dirty. 
 * </p>
 * 
 * <p>NOTICE: There is a possibility that there could be a timing issue 
 * with saving the data when the data is being changed.  This could be
 * solved with synchronization of all data points, but at the expense
 * of creating blocking for other threads, which could slow down response
 * time and cause lag.  This point is not "that" important since no data
 * will be lost since it would be updated in the next save.
 * But to help minimize the conflict, a simple use of removing the
 * dirty flag before starting the save.  The worst cause situation would 
 * be that the changes were save, but was marked as dirty.... or some 
 * data points were save from that transaction, but others were not. 
 * But neither of those matter, since everything will be "corrected" on 
 * the next save.
 * </p>
 * 
 * <p>It's important to understand where issues could occur, but data 
 * would not be lost, and issues will self-correct on the next save.
 * So it's important to always be able to save the data successfully.
 * </p>
 * 
 * <p>About every 10 to 15 minutes would be good.  After the server has
 * been running for a while and it appears to be stable, then you may
 * increase the time between saves.
 * </p>
 * 
 * <p>This class only saves the data. It does not recalculate anything.
 * </p>
 * 
 * @author RoyalBlueRanger
 *
 */
public class PlayerCacheSaveAllPlayersTask
		extends PlayerCacheRunnable
{

	@Override
	public void run()
	{
	
		PlayerCache pCache = PlayerCache.getInstance();
		
		for ( String key : pCache.getPlayers().keySet() )
		{
			PlayerCachePlayerData playerData = pCache.getPlayers().get( key );
			
			if ( playerData.isDirty() ) {
				
				playerData.setDirty( false );
				pCache.getCacheFiles().toJsonFile( playerData );
			}
		}
		
	}

}
